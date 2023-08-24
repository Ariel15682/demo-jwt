package com.example.demojwt.security.jwt;

import java.io.IOException;
import com.example.demojwt.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.util.StringUtils;


/**
 * Filters incomings request and installs a Spring Security principal if a header corresponding to a valid  user is found
 * Se ejecuta por cada peticion entrante con el fin  de validar el Token JWT
 * en caso de que lo sea se añade el contexto para indicar que un usuario esta autenticado
 */
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    //public static final String BEARER = "Bearer ";

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    /**
     * Same contract as for {@Code doFilter}, but guaranteed to be
     * Just invoked once per request within a single request thread.
     * See {@Link #shouldNotFilterAsyncDispatch()} for details
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the</p>
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//        throws ServletException, IOException {
//        try {
//            String jwt = parseJwt(request);
//            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
//                String username = jwtUtils.getUserNameFromJwtToken(jwt);
//
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }catch (Exception e) { logger.error("Cannot set user authentication: {}", e); }
//
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch (Exception e) { logger.error("Cannot set user authentication: {}", e); }

        filterChain.doFilter(request, response);
    }

    /**
     * A partir de una cabecera Authorization  extrae el Token
     * @param request
     * @return String
     */
//     private String parseJwt(HttpServletRequest request) {
//         String headerAuth = request.getHeader("Authorization");
//
//         if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
//         return headerAuth.substring(7); //return headerAuth.substring(BEARER.lenght());
//         }
//
//     return null;
//     }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        return jwt;
    }
}
