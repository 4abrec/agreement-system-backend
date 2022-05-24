package project.diploma.agreement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.diploma.agreement.domain.Message;
import project.diploma.agreement.domain.User;
import project.diploma.agreement.dto.MessageDto;
import project.diploma.agreement.dto.MessageResponseDto;
import project.diploma.agreement.exception.NotFoundException;
import project.diploma.agreement.mapper.user.UserMapper;
import project.diploma.agreement.repository.MessageRepository;
import project.diploma.agreement.service.MessageService;
import project.diploma.agreement.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UserService userService;
    private final MessageRepository messageRepository;
    private final UserMapper userMapper;

    @Override
    public List<MessageDto> getAllChatMessages(String senderUsername, String recipientUsername) {
        User sender = userService.findByUsername(senderUsername)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден", HttpStatus.BAD_REQUEST));
        List<MessageDto> messageDtoList = new ArrayList<>();

        List<Message> allMessages = messageRepository.findAll().stream()
                .filter(message ->
                        (message.getSenderUser().equals(sender) || message.getSenderUser().getUsername().equals(recipientUsername))
                        && (message.getRecipientUser().equals(recipientUsername) || message.getRecipientUser().equals(sender.getUsername()))
                )
                .sorted(Comparator.comparing(Message::getLocalDateTime))
                .collect(Collectors.toList());

        for (Message message : allMessages) {
            messageDtoList.add(new MessageDto(message, userMapper.toDto(message.getSenderUser())));
        }
        return messageDtoList;
    }

    @Override
    public MessageResponseDto sendMessage(String senderUsername, String recipientUsername, String text) {
        User sender = userService.findByUsername(senderUsername)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден", HttpStatus.BAD_REQUEST));
        messageRepository.save(new Message(text, LocalDateTime.now(), sender, recipientUsername));
        return new MessageResponseDto("Сообщение отправлено");
    }
}
