package project.diploma.agreement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import project.diploma.agreement.domain.FileDB;
import project.diploma.agreement.domain.Solution;
import project.diploma.agreement.domain.Task;
import project.diploma.agreement.domain.User;
import project.diploma.agreement.dto.MessageResponseDto;
import project.diploma.agreement.repository.FileDBRepository;
import project.diploma.agreement.repository.SolutionRepository;
import project.diploma.agreement.service.FileStorageService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileDBRepository fileDBRepository;
    private final SolutionRepository solutionRepository;

    @Override
    public FileDB store(MultipartFile file, Integer userId, Integer taskId) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes(), userId, taskId);
        return fileDBRepository.save(fileDB);
    }

    @Override
    public FileDB getFile(String id) {
        return fileDBRepository.findById(id).get();
    }

    @Override
    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

    @Override
    public List<FileDB> getFilesByUserAndTaskId(Integer taskId, Integer userId){
        return fileDBRepository.findAll().stream().filter(file -> file.getTaskId().equals(taskId)
                && file.getUserId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public MessageResponseDto deleteById(String id) {
        fileDBRepository.deleteById(id);
        return new MessageResponseDto("Файл успешно удален");
    }
}
