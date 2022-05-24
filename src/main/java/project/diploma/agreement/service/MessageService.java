package project.diploma.agreement.service;

import project.diploma.agreement.dto.MessageDto;
import project.diploma.agreement.dto.MessageResponseDto;

import java.util.List;

public interface MessageService {

    List<MessageDto> getAllChatMessages(String senderUsername, String recipientUsername);

    MessageResponseDto sendMessage(String senderUsername, String recipientUsername, String text);
}
