package project.diploma.agreement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.diploma.agreement.domain.EStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class SolutionDto {

    private Integer id;
    private String text;
    private Integer mark;
    private Boolean returnFlag;
    private LocalDateTime dateTime;
    private EStatus status;
    private List<CommentDto> comments;
    private List<ResponseFileDto> files;
}
