package musico.services.auth.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import musico.services.auth.dto.AuthKafkaDTO;
import musico.services.auth.services.ValidationService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyTypedMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@AllArgsConstructor
public class UsersController {

    private final ValidationService validationService;
    private final ReplyingKafkaTemplate<String, AuthKafkaDTO, AuthKafkaDTO> replyingKafkaTemplate;


    public static class UserRequest {
        public String username;
        public String password;
        public String email;

        @Override
        public String toString() {
            return "UserRequest{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(
            @RequestBody UserRequest body
    ) {
        log.info("Received request: {}", body.toString());
//        if (!validationService.validateSignup(body.username, body.email, body.password)) {
//            return ResponseEntity.badRequest().body("Invalid user data");
//        }
        AuthKafkaDTO KafkaDTO = AuthKafkaDTO.builder()
                .username(body.username)
                .password(body.password)
                .email(body.email)
                .id(UUID.randomUUID())
                .ACTION("register")
                .ERROR_CODE(-1)
                .build();
        RequestReplyTypedMessageFuture<String, AuthKafkaDTO,AuthKafkaDTO> response = replyingKafkaTemplate.sendAndReceive(
                MessageBuilder.withPayload(KafkaDTO)
                        .setHeader(KafkaHeaders.TOPIC, "registration")
                        .setHeader(KafkaHeaders.REPLY_TOPIC, "auth-reply")
                        .build()
                , new ParameterizedTypeReference<>() {
                }
        );

        try {
            AuthKafkaDTO responseDTO = response.get(10, TimeUnit.SECONDS).getPayload();
            return ResponseEntity.ok("User registered successfully - code: " + responseDTO.ERROR_CODE);

        } catch (Exception e) {
            log.error("Error while registering user", e);
            return ResponseEntity.badRequest().body("Error while registering user");
        }
    }
}
