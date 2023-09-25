package com.example.demojwt.security.config;

import com.example.demojwt.security.jwt.AuthEntryPointJwt;
import com.example.demojwt.security.jwt.AuthTokenFilter;
import com.example.demojwt.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.security.config.Customizer;
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.http.HttpMethod;


/**
 *  Clase para la configuracion de seguridad Spring Security
 */
@Configuration
//@EnableWebSecurity // Permite a Spring aplicar esta configuracion a la configuraicon de seguridad global
@EnableMethodSecurity
// (securedEnabled = true,
// jsr250Enabled = true,
// prePostEnabled = true) // by default
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {

    @Value("${spring.h2.console.path}")
    private String h2ConsolePath;

//  private static final String[] AUTH_WHITE_LIST = {
//            "/**"
//  };

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() { return new AuthTokenFilter(); }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//
//        return authProvider;
//    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //auth.authenticationProvider(daoAuthenticationProvider());
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Cross-site Request Forgery CSRF
        // CORS (Cross-Origin-Resource-Sharing)
        http
                // Enable CORS default
                //.cors(Customizer.withDefaults()) // (disable) .cors(cors -> cors.disable())
                //Disable CSRF
                //.csrf((protection) -> protection.ignoringRequestMatchers(h2ConsolePath))
                .csrf(AbstractHttpConfigurer::disable) //.csrf(csrf -> csrf.disable())
                //Enable CSRF
                //.csrf(Customizer.withDefaults())
                //.csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                // Set unauthorized requests exception handler
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                // Set session management to stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Set permission solo JWT
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                //.requestMatchers(HttpMethod.POST,"/api/auth/signin/**").permitAll()
                                .requestMatchers("/api/auth/signup/**").permitAll()
                                .requestMatchers("/api/auth/signin/**").permitAll()
                                .requestMatchers("/api/auth/signout/**").permitAll()
                                .requestMatchers(antMatcher(h2ConsolePath + "/**")).permitAll()
                                //.requestMatchers("/v3/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**").permitAll() // Swagger 3
                                //.requestMatchers(AUTH_WHITE_LIST).permitAll()
                                .anyRequest().authenticated()); //el resto esta capado

        // This will allow frames with same origin which is much more safe
        // fix H2 database console: Refused to display ' in a frame because it set 'X-Frame-Options' to 'deny'
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)); // (Lambda) (frameOption -> frameOption.sameOrigin()));

//       http.authenticationProvider(daoAuthenticationProvider());

        // Add JWT token filter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher(h2ConsolePath));
//    }

    /**
     * Configuracion global de CORS para toda la aplicacion
     */
    // Prohibir que se acceda a nuestro Backend desde determinados dominios y solo desde el que queremos
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:4200", "https://angular-springboot1-beta.vercel.app"));
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(List.of("Access-Control-Allow-Origin", "X-Requested with", "Origin", "Content-Type", "Accept", "Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}