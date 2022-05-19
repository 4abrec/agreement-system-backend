package project.diploma.agreement.mapper.user;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import project.diploma.agreement.domain.User;
import project.diploma.agreement.dto.UserDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserListMapper {

    List<UserDto> toDtoList(List<User> models);

    List<User> toModelList(List<UserDto> userDtos);
}
