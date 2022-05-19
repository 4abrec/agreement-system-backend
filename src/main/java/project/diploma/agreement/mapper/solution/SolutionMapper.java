package project.diploma.agreement.mapper.solution;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.diploma.agreement.domain.Solution;
import project.diploma.agreement.dto.ResponseFileDto;
import project.diploma.agreement.dto.SolutionDto;
import project.diploma.agreement.mapper.comment.CommentMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SolutionMapper {

    private final CommentMapper commentMapper;

    public SolutionDto toDto(Solution solution, List<ResponseFileDto> files) {
        return new SolutionDto(solution.getId(), solution.getText(), solution.getMark(), solution.getReturnFlag(),
                solution.getDateTime(), solution.getStatus(), commentMapper.toDtoList(solution.getComment()), files);
    }

}
