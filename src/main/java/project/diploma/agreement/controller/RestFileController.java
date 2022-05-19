package project.diploma.agreement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import project.diploma.agreement.domain.FileDB;
import project.diploma.agreement.domain.User;
import project.diploma.agreement.dto.MessageResponseDto;
import project.diploma.agreement.dto.ResponseFileDto;
import project.diploma.agreement.exception.NotFoundException;
import project.diploma.agreement.repository.UserRepository;
import project.diploma.agreement.service.FileStorageService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@CrossOrigin
public class RestFileController {

    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        FileDB fileDB = fileStorageService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> deleteFile(@PathVariable String id) {
        return new ResponseEntity<>(fileStorageService.deleteById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<ResponseFileDto>> getListFiles(@RequestParam Integer taskId, @RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден", HttpStatus.OK));
        List<ResponseFileDto> files = fileStorageService.getFilesByUserAndTaskId(taskId, user.getId()).stream().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();
            return new ResponseFileDto(
                    dbFile.getId(),
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }
}
