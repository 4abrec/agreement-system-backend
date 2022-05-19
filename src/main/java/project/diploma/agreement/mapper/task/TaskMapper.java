package project.diploma.agreement.mapper.task;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import project.diploma.agreement.domain.Task;
import project.diploma.agreement.dto.TaskDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TaskMapper {

    TaskDto toDto(Task task);

    Task toModel(TaskDto taskDto);
}
