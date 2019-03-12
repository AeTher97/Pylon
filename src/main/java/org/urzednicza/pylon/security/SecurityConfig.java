package org.urzednicza.pylon.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xerces.internal.parsers.SecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.urzednicza.pylon.security.filters.BasicFilter;
import org.urzednicza.pylon.services.TaskService;
import org.urzednicza.pylon.services.UserDetails;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
        private UserDetails userDetails;
        private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(UserDetails userDetails, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetails = userDetails;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private CharacterEncodingFilter buildCharacterEncodingFilter(){
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    private BasicFilter buildBasicFilter(AuthenticationManager authenticationManager){
        return new BasicFilter(authenticationManager);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder);
    }


    @Override
    public void configure(HttpSecurity http)throws Exception{

        http.cors().and().csrf().disable().authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(buildCharacterEncodingFilter(), CsrfFilter.class)
                .addFilter(buildBasicFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
