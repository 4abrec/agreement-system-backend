package project.diploma.agreement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import project.diploma.agreement.domain.*;
import project.diploma.agreement.dto.MessageResponseDto;
import project.diploma.agreement.dto.ResponseFileDto;
import project.diploma.agreement.exception.NotFoundException;
import project.diploma.agreement.repository.*;
import project.diploma.agreement.service.FileStorageService;
import project.diploma.agreement.service.SolutionService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolutionServiceImpl implements SolutionService {

    private final SolutionRepository solutionRepository;
    private final TaskRepository taskRepository;
    private final FileDBRepository fileDBRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public List<ResponseFileDto> getFilesBySolutionId(Integer solutionId) {
        Solution solution = solutionRepository.getById(solutionId);
        List<FileDB> fileDBList = fileDBRepository.findBySolution_Id(solutionId);
        return fileDBList.stream().map(dbFile -> {
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
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        solutionRepository.deleteById(id);
    }

    @Override
    public MessageResponseDto save(Integer taskId, String username, String text) {
        Task task = taskRepository.getById(taskId);
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден", HttpStatus.OK));
        Solution solution = new Solution(text, 0, false, LocalDateTime.now(), EStatus.ON_INSPECTION, task, author);
        solutionRepository.save(solution);
        List<FileDB> files = fileStorageService.getFilesByUserAndTaskId(taskId, author.getId());
        for (FileDB file : files) {
            file.setSolution(solution);
            fileDBRepository.save(file);
        }
        return new MessageResponseDto("Решение сохранено");
    }

    @Override
    public MessageResponseDto agreementSolution(Integer solId, Integer mark, String comment,
                                                Boolean isAgree, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден", HttpStatus.BAD_REQUEST));

        Solution solution = solutionRepository.getById(solId);

        if (!comment.isBlank()) {
            Comment com = new Comment(comment, LocalDateTime.now(), user, solution);
            commentRepository.save(com);
        }
        
        solution.setMark(mark);
        if (isAgree) {
            solution.setStatus(EStatus.AGREED);
        } else
            solution.setStatus(EStatus.NEED_TO_FINALIZE);
        solutionRepository.save(solution);
        return new MessageResponseDto("Сохранено");
    }
}
