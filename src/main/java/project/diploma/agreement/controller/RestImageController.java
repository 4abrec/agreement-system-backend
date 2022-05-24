package project.diploma.agreement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import project.diploma.agreement.domain.ImageDB;
import project.diploma.agreement.dto.MessageResponseDto;
import project.diploma.agreement.dto.ResponseImageDto;
import project.diploma.agreement.service.ImageStorageService;

import java.util.Optional;


@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@CrossOrigin
public class RestImageController {

    private final ImageStorageService imageStorageService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponseDto> uploadImage(@RequestParam("image") MultipartFile file,
                                                          @RequestParam String username) {
        String message = "";
        try {
            imageStorageService.store(file, username);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponseDto(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponseDto(message));
        }
    }

    @GetMapping()
    public ResponseEntity<ResponseImageDto> getUserImage(@RequestParam String username) {

        Optional<ImageDB> optionalImage = imageStorageService.getImageByUsername(username);

        ImageDB imageDB = null;

        if (optionalImage.isPresent()) {
            imageDB = optionalImage.get();
        }
        assert imageDB != null;
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/images/")
                .path(imageDB.getId())
                .toUriString();

        ResponseImageDto imageDto = new ResponseImageDto(
                imageDB.getId(),
                imageDB.getName(),
                fileDownloadUri,
                imageDB.getType(),
                imageDB.getData().length);

        return ResponseEntity.status(HttpStatus.OK).body(imageDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
       ImageDB fileDB = imageStorageService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }

}
