package com.spring.configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.spring.component.EmailOAuth2UserService;
import com.spring.constants.SwaggerConstants;
import com.spring.entity.User;
import com.spring.enums.Role;
import com.spring.exception.RestException;
import com.spring.exception.ValidationException;
import com.spring.filter.JwtAuthenticationFilter;
import com.spring.filter.JwtAuthorizationFilter;
import com.spring.service.JwtService;
import com.spring.service.ProfileService;
import com.spring.service.UserService;
import com.spring.service.implementations.PropertiesService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    private final PropertiesService propertiesService;
    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ProfileService profileService;

    @Autowired
    public SecurityConfig(PropertiesService propertiesService, JwtService jwtService,
                          UserService userService, PasswordEncoder passwordEncoder, ProfileService profileService) {
        this.propertiesService = propertiesService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.profileService = profileService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .authorizeRequests()
                .antMatchers("/admin", "/admin/**").hasAuthority(Role.ADMIN.name())
                .antMatchers("/", "/registration", "/registration/**",
                        "/recover-password", "/recover-password/**", "companies/top-employee",
                        "companies/top-employer", "/project", "/access").permitAll()
                .antMatchers(HttpMethod.GET, "/companies", "/companies/**").permitAll()
                .antMatchers(HttpMethod.GET, "/projects", "/projects/**").permitAll()
                .antMatchers(SwaggerConstants.SWAGGER_ENDPOINTS.toArray(new String[]{})).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtService))
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtService))
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/authorization")
                .and()
                .successHandler(authenticationSuccessHandler())
                .userInfoEndpoint()
                .userService(oAuth2UserService())
                .and()
                .authorizedClientService(clientService())
                .clientRegistrationRepository(clientRegistrationRepository());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService());

        return authenticationProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration = corsConfiguration.applyPermitDefaultValues();

        corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.addExposedHeader("filename");

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }


    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
                Optional<User> userOptional = userService.findByUsername(authentication.getName());
                User user;

                if (!userOptional.isPresent()) {
                    user = userService.createUser((OAuth2User) authentication.getPrincipal());
                    try {
                        profileService.useForeignAccount((OAuth2User) authentication.getPrincipal());
                    } catch (ValidationException v) {
                        LOGGER.warn(v.getMessage(), v);
                    }
                } else {
                    user = userOptional.get();
                    if (!user.getActive()) {
                        response.sendRedirect(propertiesService.getClientUrl() + "/login?blocked=true");
                        return;
                    }
                }

                response.sendRedirect(propertiesService.getClientUrl() + "/authorization/oauth2?token=" +
                        jwtService.generateToken(user.getUsername(),
                                user.getRoles().stream().map(Role::name).collect(Collectors.toSet())));
            }
        };
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new EmailOAuth2UserService();
    }

    @Bean
    public OAuth2AuthorizedClientService clientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        LOGGER.debug("in clientRegistrationRepository() method");
        List<String> clients = Arrays.asList(PropertiesService.GITHUB_CLIENT, PropertiesService.GOOGLE_CLIENT);

        return new InMemoryClientRegistrationRepository(clients.stream()
                .map(this::getClientRegistration)
                .collect(Collectors.toList()));
    }

    private ClientRegistration getClientRegistration(String client) {
        ClientRegistration.Builder builder;

        if (client.equals(PropertiesService.GOOGLE_CLIENT)) {
            builder = CommonOAuth2Provider.GOOGLE
                    .getBuilder(client)
                    .scope("email", "profile");
        } else if (client.equals(PropertiesService.GITHUB_CLIENT)) {
            builder = CommonOAuth2Provider.GITHUB
                    .getBuilder(client)
                    .scope("user:email");
        } else {
            throw new RestException("Invalid client name");
        }

        Map<String, String> credentials = propertiesService.getOAuthClientCredentials(client);

        return builder
                .clientId(credentials.get(PropertiesService.OAUTH2_CLIENT_ID))
                .clientSecret(credentials.get(PropertiesService.OAUTH2_CLIENT_SECRET))
                .build();
    }

}
