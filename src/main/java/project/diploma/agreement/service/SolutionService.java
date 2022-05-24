package project.diploma.agreement.service;

import org.springframework.web.bind.annotation.RequestParam;
import project.diploma.agreement.dto.MessageResponseDto;
import project.diploma.agreement.dto.ResponseFileDto;

import javax.transaction.Transactional;
import java.util.List;

public interface SolutionService {

    List<ResponseFileDto> getFilesBySolutionId(Integer solutionId);

    @Transactional
    void deleteById(Integer id);

    MessageResponseDto save(Integer taskId, String username, String text);

    MessageResponseDto update(Integer taskId, Integer solutionId, String text, String username);

    MessageResponseDto agreementSolution(Integer solId, Integer mark, String comment, Boolean isAgree, String username);
}
