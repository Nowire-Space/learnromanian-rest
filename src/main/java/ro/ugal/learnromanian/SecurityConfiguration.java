package ro.ugal.learnromanian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import ro.ugal.learnromanian.util.Enum;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String REALM = "LearnRomania Auth REALM";

    @Autowired
    private ApplicationAuthenticationProvider authProvider;

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.anonymous().disable();

        http.csrf().disable().authorizeRequests()
                .antMatchers("/professor/**")
                .hasAnyRole(Enum.Role.PROFESSOR)
                .and().httpBasic().realmName(REALM)
                .authenticationEntryPoint(getBasicAuthEntryPoint());


        http.csrf().disable().authorizeRequests()
                .antMatchers("/student/**").hasAnyRole(Enum.Role.PROFESSOR, Enum.Role.STUDENT).and()
                .httpBasic().realmName(REALM)
                .authenticationEntryPoint(getBasicAuthEntryPoint());
    }

    @Bean
    public AuthenticationEntryPoint getBasicAuthEntryPoint(){
        return new AuthenticationEntryPoint();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/course/**");
        web.ignoring().antMatchers("/hello");
        web.ignoring().antMatchers("/user/registration/**");

//        web.ignoring().antMatchers("/user/verify/**");
//        web.ignoring().antMatchers("/user/password/**");
//        web.ignoring().antMatchers("/version/**");
        web.ignoring().antMatchers("/login/*");

    }
}
