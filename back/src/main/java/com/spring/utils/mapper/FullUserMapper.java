package com.spring.utils.mapper;

import com.spring.entity.Profile;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.dto.FullUserDto;
import com.spring.entity.User;
import com.spring.enums.Role;

@Component
public class FullUserMapper implements DTOMapper<User, FullUserDto> {

    private final ModelMapper modelMapper;

    private final TypeMap<Profile, FullUserDto> toDto;

    @Autowired
    public FullUserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        toDto = modelMapper.createTypeMap(Profile.class, FullUserDto.class)
                .addMappings(x -> x.skip(FullUserDto::setUserId));
    }

	@Override
	public User toEntity(FullUserDto dto) {
		return modelMapper.map(dto, User.class);
	}

	public FullUserDto toDto(User user) {
        if (user == null) {
            return null;
        }
		boolean isAdmin = user.isAdmin();
        FullUserDto fullUserDto = new FullUserDto();

        if (user.getProfile() != null) {
            fullUserDto = toDto.map(user.getProfile());
        }
        fullUserDto.setUsername(user.getUsername());
        fullUserDto.setRole(isAdmin ? Role.ADMIN : Role.USER);
        fullUserDto.setBlocked(!user.getActive() && user.getProfile() != null);
        fullUserDto.setUserId(user.getId());

        return fullUserDto;
    }

}
