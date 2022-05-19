package project.diploma.agreement.mapper.user;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import project.diploma.agreement.domain.User;
import project.diploma.agreement.dto.UserDto;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserDto toDto(User user);

    User toModel(UserDto userDto);
}
