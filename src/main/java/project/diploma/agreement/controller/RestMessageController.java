package project.diploma.agreement.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.diploma.agreement.dto.MessageDto;
import project.diploma.agreement.dto.MessageResponseDto;
import project.diploma.agreement.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
@CrossOrigin
public class RestMessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<MessageDto>> getAllChatMessage(@RequestParam String senderUsername,
                                                              @RequestParam String recipientUsername) {
        return new ResponseEntity<>(messageService.getAllChatMessages(senderUsername, recipientUsername), HttpStatus.OK);
    }

    @GetMapping("/send")
    public ResponseEntity<MessageResponseDto> sendMessage(@RequestParam String senderUsername,
                                                          @RequestParam String recipientUsername,
                                                          @RequestParam String text) {
        return new ResponseEntity<>(messageService.sendMessage(senderUsername,recipientUsername, text), HttpStatus.OK);
    }
}
