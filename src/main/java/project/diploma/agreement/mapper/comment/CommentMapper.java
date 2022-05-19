package project.diploma.agreement.mapper.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.diploma.agreement.domain.Comment;
import project.diploma.agreement.dto.CommentDto;
import project.diploma.agreement.mapper.user.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;

    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getDateTime(), userMapper.toDto(comment.getUser()));
    }

    public List<CommentDto> toDtoList(List<Comment> comments) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment: comments) {
            commentDtoList.add(toDto(comment));
        }

        return commentDtoList;
    }
}
