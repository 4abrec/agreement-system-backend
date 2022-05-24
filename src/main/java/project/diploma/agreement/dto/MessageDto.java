package project.diploma.agreement.dto;

import lombok.Data;
import project.diploma.agreement.domain.Message;

@Data
public class MessageDto {
    private Message message;
    private UserDto user;

    public MessageDto(Message message, UserDto user) {
        this.message = message;
        this.user = user;
    }
}
