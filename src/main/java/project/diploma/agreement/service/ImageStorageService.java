package project.diploma.agreement.service;

import org.springframework.web.multipart.MultipartFile;
import project.diploma.agreement.domain.ImageDB;
import project.diploma.agreement.dto.MessageResponseDto;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public interface ImageStorageService {

    ImageDB store(MultipartFile file, String username) throws IOException;

    ImageDB getFile(String id);

    Stream<ImageDB> getAllFiles();

    Optional<ImageDB> getImageByUsername(String username);

    MessageResponseDto deleteById(String id);

}
