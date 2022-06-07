package cz.muni.ics.web.config;

import cz.muni.ics.web.utils.ViewLinks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring security configuration class for Web module
 *
 * @author Jozef Cibík
 */

@Slf4j
@Configuration
@EnableWebSecurity
@PropertySource("classpath:application-web.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    ////    Admin disabled
//    private final String ADMIN_ROLE = "ADMIN";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(
                        "/js/**",
                        "/css/**",
                        "/img/**",
                        "/plug-in/**",
                        "/webjars/**",
                        "/static/**",

                        ViewLinks.IMPORT + "/*",
                        ViewLinks.IMPORT + "/**",
                        ViewLinks.ABOUT,
                        ViewLinks.FILEMANAGER + "/**")
                .permitAll()

////    Admin disabled
//                .antMatchers("/admin/**")
//                .access("hasRole('ADMIN')")
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .defaultSuccessUrl("/admin/index")
//                .usernameParameter("username").passwordParameter("password")
//                .successForwardUrl("/admin/index")
//                .permitAll()
//                .and()
//                .logout()
//                .invalidateHttpSession(false)
//                .permitAll()
                .and()
                .exceptionHandling();

    }

////    Admin disabled
//    @Autowired
//    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("admin")).roles(ADMIN_ROLE);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
