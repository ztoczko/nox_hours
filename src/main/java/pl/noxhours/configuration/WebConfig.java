package pl.noxhours.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig {

        @Bean
        public WebMvcConfigurer forwardToLogin() {
            return new WebMvcConfigurer() {
                @Override
                public void addViewControllers(ViewControllerRegistry registry) {

                    registry.addViewController("/login").setViewName(
                            "login");

                }
            };
        }
    }

