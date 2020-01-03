package com.spring.component;

import com.spring.dto.OAuth2UserDto;
import com.spring.service.implementations.PropertiesService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestTemplate;

public class EmailOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private static RestTemplate restTemplate;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		String uri = userRequest.getClientRegistration().getProviderDetails()
				.getUserInfoEndpoint()
				.getUri();
		String token = userRequest.getAccessToken().getTokenValue();


		if(userRequest.getClientRegistration().getRegistrationId().equals(PropertiesService.GITHUB_CLIENT)) {
			return requestGithubEmail(uri, token);
		}

		return requestGoogleData(uri, token);

	}

	public OAuth2UserDto requestGithubEmail(String uri, String token) {

		try {

			String emailResponse = httpGet(uri + PropertiesService.GITHUB_USER_INFO_EMAIL_SEGMENT + "?access_token=" + token);
			JSONParser jsonParser = new JSONParser();
			JSONArray jsonArray = (JSONArray) jsonParser.parse(emailResponse);

			if(jsonArray.size() > 0) {

				OAuth2UserDto userDto = null;

				for(Object object : jsonArray) {
					JSONObject jsonObject = (JSONObject) jsonParser.parse(object.toString());
					if((Boolean) jsonObject.get(PropertiesService.GITHUB_PRIMARY_FIELD)) {
						userDto = new OAuth2UserDto((String) jsonObject.get(PropertiesService.GITHUB_EMAIL_FIELD));
						break;
					}
				}

				if(userDto != null) {
					String profileResponse = httpGet(uri + "?access_token=" + token);
					JSONObject jsonObject = (JSONObject) jsonParser.parse(profileResponse);
					userDto = parseGithubProfile(userDto, jsonObject);
				}

				return userDto;
			}

		} catch(Exception e) {
			logger.error(e.getMessage());
			return null;
		}

		throw new RuntimeException("Github account without email");
	}

	private OAuth2UserDto requestGoogleData(String uri, String token) {

		try {

			String responseStr = httpGet(uri + "?access_token=" + token);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(responseStr);

			String name = (String) jsonObject.get("name");

			OAuth2UserDto oAuth2UserDto = new OAuth2UserDto((String) jsonObject.getOrDefault("email", name));
			oAuth2UserDto.setPhotoUrl((String) jsonObject.get("picture"));
			oAuth2UserDto.setFirstName((String) jsonObject.get("given_name"));
			oAuth2UserDto.setLastName((String) jsonObject.get("family_name"));
			oAuth2UserDto.setNickname(oAuth2UserDto.getFirstName().toLowerCase() + "." + oAuth2UserDto.getLastName().toLowerCase());

			String sub = (String) jsonObject.get("sub");
			oAuth2UserDto.setId(Long.parseLong(sub.substring(sub.length() / 2)));

			return oAuth2UserDto;
		} catch(ParseException e) {
			return null;
		}
	}

	public OAuth2UserDto parseGithubProfile(OAuth2UserDto userDto, JSONObject jsonObject) {

		if(jsonObject.get("id") != null) {
			userDto.setId(((Integer) jsonObject.get("id")).longValue());
		}

		if(jsonObject.get("avatar_url") != null) {
			userDto.setPhotoUrl((String) jsonObject.get("avatar_url"));
		}

		if(jsonObject.get("login") != null) {
			userDto.setNickname((String) jsonObject.get("login"));
		}

		if(jsonObject.get("name") != null) {
			String name = (String) jsonObject.get("name");
			if(name.matches(".+\\s.+")) {

				String [] names = name.split("\\s");
				String lastName = "";

				for (int i = 0; i < names.length; i++) {
					if(i == 0) {
						userDto.setFirstName(names[i]);
						continue;
					}
					lastName += names[i];
					if(i < names.length - 1) {
						lastName += " ";
					}
				}
				userDto.setLastName(lastName);
			} else {
				userDto.setFirstName("User");
				userDto.setLastName(name);
			}
		}

		return userDto;
	}

	private String httpGet(String uri) {
		if(restTemplate == null) {
			restTemplate = new RestTemplate();
		}
		return restTemplate.getForObject(uri, String.class);
	}
}
