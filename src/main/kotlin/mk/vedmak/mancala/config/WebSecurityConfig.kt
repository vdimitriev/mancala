package mk.vedmak.mancala.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var userDetailsService: PlayerDetailsService

    @Bean
    fun bCryptPasswordEncoder():BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(http: HttpSecurity?) {
        http!!.headers().frameOptions().disable()
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/resources/**", "/WEB-INF/jsp/*", "/registration", "/login", "/").permitAll()
                .antMatchers("/h2-console").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/game/**", "/games/**").hasAuthority("PLAYER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/games")
                .failureUrl("/login?error")
                .permitAll()
                .and()
                .logout()
                .permitAll()
    }
}