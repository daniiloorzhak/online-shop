package ru.oorzhak.accountservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.oorzhak.accountservice.exception.NoAuthenticationHeader;
import ru.oorzhak.accountservice.util.JwtUtil;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtTokenFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String header = Optional.of(request.getHeader("Authorization")).orElseThrow(NoAuthenticationHeader::new);

            String jwt = header.substring(7);

            if (!jwtUtil.isJwtValid(jwt)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid JWT");
                filterChain.doFilter(request, response);
                return;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.getUsernameFromJwtToken(jwt));

            var authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (IOException e) {
            System.out.println("Filter IOException");
            throw new RuntimeException(e);
        } catch (ServletException e) {
            System.out.println("Filter ServletException");
            throw new RuntimeException(e);
        } catch (UsernameNotFoundException e) {
            System.out.println("Filter UsernameNotFoundException");
            throw new RuntimeException(e);
        } catch (NoAuthenticationHeader | NullPointerException ignored) {

        } catch (IndexOutOfBoundsException ignored) {

        }

        filterChain.doFilter(request, response);
    }
}
