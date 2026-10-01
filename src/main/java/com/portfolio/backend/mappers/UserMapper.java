package com.portfolio.backend.mappers;

import com.portfolio.backend.dto.UserDTO;
import com.portfolio.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface UserMapper {
    @Mapping(target = "id", expression = "java(user.getId().toString())")
    UserDTO toDTO(User user);
}
