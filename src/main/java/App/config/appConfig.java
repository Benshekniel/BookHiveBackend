package App.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class appConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//Enable no username and password method
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll() // ✅ allow everything
//                )
//                .csrf(csrf -> csrf.disable()) // ✅ disable CSRF using lambda
//                .formLogin(form -> form.disable()) // ✅ disable login form
//                .httpBasic(httpBasic -> httpBasic.disable()); // ✅ disable HTTP Basic
//
//        return http.build();
//    }



//Enabling username and password to handle
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {
//        UserDetails user = User.withUsername("loki")
//                .password(encoder.encode("password123"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeHttpRequests()
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic(); // Enable basic authentication (Postman will prompt)
//
//        return http.build();
//    }
}
