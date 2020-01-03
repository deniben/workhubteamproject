package com.spring.configuration;

import org.apache.tika.Tika;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.spring.component.BlockedUserInterceptor;
import com.spring.component.IpControlInterceptor;
import com.spring.component.UserContext;
import com.spring.service.IpControlService;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.spring")
public class ApplicationConfig implements WebMvcConfigurer {

    private final IpControlService ipControlService;
    private final UserContext userContext;

    @Autowired
    public ApplicationConfig(IpControlService ipControlService, UserContext userContext) {
        this.ipControlService = ipControlService;
        this.userContext = userContext;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IpControlInterceptor(ipControlService));
        registry.addInterceptor(new BlockedUserInterceptor(userContext));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Tika tika() {
        return new Tika();
    }


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);
        return mapper;
    }

}
