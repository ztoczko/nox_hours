package pl.noxhours.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public NoxAuthenticationSuccessHandler getSuccessHandler() {
        return new NoxAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//TODO obczaić csrf
        http.csrf().disable().authorizeRequests()
                //TODO - dodać jakąś stronę tytułową?? jeśli nie to wywalić anta na "/"
                .antMatchers("/admin/**", "/clients/delete/**", "/clients/*/case/delete/**").hasAuthority("ADMIN")
                .antMatchers("/clients/*/rate/**").hasAuthority("RATES")
                .antMatchers("/", "/login", "/reset/**", "/error").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll().failureUrl("/login?error=true").successHandler(getSuccessHandler())
                .and()
                .logout().logoutUrl("/logout").deleteCookies("nox_hours_persistence_login").invalidateHttpSession(true).clearAuthentication(true).permitAll().logoutSuccessUrl("/login?logout=true")
                .and()
                .rememberMe().key("nox_hours_persistence_login").tokenValiditySeconds(60 * 60 * 24 * 14).userDetailsService(userDetailsService).authenticationSuccessHandler(getSuccessHandler());
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }
}
