package musico.services.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import musico.services.user.models.UserProfileDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping(path = "/user/profile")
public class ProfileController {

    private final KafkaTemplate<String, UserProfileDTO> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, UserProfileDTO, UserProfileDTO> replyingKafkaTemplate;

    public ProfileController(@Qualifier("kafkaTemplateAuthProcess") KafkaTemplate<String, UserProfileDTO> kafkaTemplate, ReplyingKafkaTemplate<String, UserProfileDTO, UserProfileDTO> replyingKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    @PostMapping(path = "/create")
    @PreAuthorize("hasAuthority('SCOPE_user')")
    public String createProfile(@RequestBody UserProfileDTO profileDTO, HttpServletRequest principal) {
        // Get User ID from principal
        profileDTO.setUserId(principal.getUserPrincipal().getName());
        kafkaTemplate.send("profile-creation", profileDTO);
        return "Profile created";
    }

    @GetMapping(path = "/get")
    @PreAuthorize("hasAuthority('SCOPE_user')")
    public String getProfile(@RequestBody UserProfileDTO profileDTO, HttpServletRequest principal) throws ExecutionException, InterruptedException, TimeoutException {
        log.info("Principal: {}", principal);
        profileDTO.setUserId(principal.getUserPrincipal().getName());
        ProducerRecord<String, UserProfileDTO> record = new ProducerRecord<>("profile-get", profileDTO);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "profile-get-response".getBytes()));
        RequestReplyFuture<String, UserProfileDTO, UserProfileDTO> future = replyingKafkaTemplate.sendAndReceive(record);
        future.getSendFuture().get(10, java.util.concurrent.TimeUnit.SECONDS);
        log.info("Sent: {}", record.value()) ;
        try{
            ConsumerRecord<String, UserProfileDTO> response = future.get(10, java.util.concurrent.TimeUnit.SECONDS);
            log.info("Response: {}", response.value());
        }catch (Exception e){
            log.error("Error: {}", e.getMessage());
        }
        return "Profile retrieved";
    }

}
