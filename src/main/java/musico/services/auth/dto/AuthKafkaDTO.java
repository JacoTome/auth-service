package musico.services.auth.dto;

import lombok.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthKafkaDTO{
        private UUID id;
        private String ACTION;
        public String username;
        public String password;
        public String email;
        public Integer ERROR_CODE;


}

