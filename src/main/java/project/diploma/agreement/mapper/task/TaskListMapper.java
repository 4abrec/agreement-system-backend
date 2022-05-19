package project.diploma.agreement.mapper.task;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import project.diploma.agreement.domain.Task;
import project.diploma.agreement.dto.TaskDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = TaskMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TaskListMapper {

    List<TaskDto> toDtoList(List<Task> models);

    List<Task> toModelList(List<TaskDto> taskDtos);
}
