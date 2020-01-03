package com.spring.dto;

import com.spring.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.lang.reflect.Field;
import java.util.*;

public class OAuth2UserDto implements OAuth2User {

	Long id;

    String name;

    String photoUrl;

    String firstName;

    String lastName;

    String nickname;
    
    public OAuth2UserDto(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(Role.USER);
    }

    @Override
    public Map<String, Object> getAttributes() {

    	Map<String, Object> attributes = new HashMap<>();

    	List<String> fileds = Arrays.asList("id", "firstName", "lastName", "nickname", "photoUrl");

    	for (String filedName : fileds) {

    		try {

			    Field field = getClass().getDeclaredField(filedName);

			    if(field.get(this) != null) {
				    attributes.put(field.getName(), field.get(this));
			    }

		    } catch(Exception e) {}

	    }

    	return attributes;
    }

    @Override
    public String getName() {
        return name;
    }

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
