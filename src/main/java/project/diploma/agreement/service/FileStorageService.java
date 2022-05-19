package project.diploma.agreement.service;

import org.springframework.web.multipart.MultipartFile;
import project.diploma.agreement.domain.FileDB;
import project.diploma.agreement.dto.MessageResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public interface FileStorageService {

    FileDB store(MultipartFile file, Integer userId, Integer taskId) throws IOException;

    FileDB getFile(String id);

    Stream<FileDB> getAllFiles();

    List<FileDB> getFilesByUserAndTaskId(Integer taskId, Integer userId);

    MessageResponseDto deleteById(String id);
}
