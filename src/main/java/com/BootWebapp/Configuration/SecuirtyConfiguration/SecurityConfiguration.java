package com.BootWebapp.Configuration.SecuirtyConfiguration;

import com.BootWebapp.CustomeSecurity.UserServicesForAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity()
public class SecurityConfiguration  {

    @Autowired
    private UserServicesForAuthentication servicesForAuthentication;

    @Autowired
    private DataSource dataSource;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(@Autowired PasswordEncoder passwordEncoder){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(servicesForAuthentication);

        return daoAuthenticationProvider;

    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   @Autowired CsrfTokenRepository csrfTokenRepository) throws Exception {

        http
                .csrf(csrfConfig -> csrfConfig
                        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/login"))
                        .csrfTokenRepository(csrfTokenRepository)
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )

                .authorizeHttpRequests(authRequests -> authRequests
                        .requestMatchers("/").hasRole("USER")
                        .requestMatchers("/login.html","/Collaborativetaskboardimages/icon.png","/CSS/login.css",
                                "/user/CSRF/getToken","/user/failed/login").permitAll()
                        .anyRequest()
                        .authenticated())

                .formLogin(formConfig -> formConfig
                        .loginPage("/user/myLogin")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/user/taskHub",true)
                        .failureForwardUrl("/user/failed/login")
                        .permitAll())


                .logout(logoutConfig -> logoutConfig
                        .logoutUrl("/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
                        .logoutSuccessUrl("/user/myLogin")
                        .deleteCookies("JSESSIONID","remember-me")
                        .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL))))

                .securityContext((securityContext) -> securityContext
                        .securityContextRepository(
                                new DelegatingSecurityContextRepository(
                                        new HttpSessionSecurityContextRepository()
                                )
                        )
                )

                .rememberMe(rememberMeConfig -> rememberMeConfig
                        .rememberMeServices(
                                new PersistentTokenBasedRememberMeServices("mazharRehanaAyman650",
                                        servicesForAuthentication,
                                        jdbcTokenRepository()
                                )
                        )
                );

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository(){
        return new HttpSessionCsrfTokenRepository();
    }

    public JdbcTokenRepositoryImpl jdbcTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

}
