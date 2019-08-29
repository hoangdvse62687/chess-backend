package com.chess.chessapi.security;

import com.chess.chessapi.constants.AppMessage;
import com.chess.chessapi.exceptions.AccessDeniedException;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromToken(jwt);

                UserDetails userDetails;
                HttpSession session = request.getSession();
                //check user login is on session
                if(session.getAttribute(userId.toString()) != null){
                    userDetails = (UserDetails) session.getAttribute(userId.toString());
                }else{
                    userDetails = customUserDetailsService.loadUserById(userId);
                    //set sesson
                    session.setAttribute(userId.toString(),userDetails);
                }
                if(!session.isNew()){
                    response.setHeader("X-Auth-Token",request.getHeader("X-Auth-Token"));
                }

                UserPrincipal userPrincipal = (UserPrincipal) userDetails;
                if(userPrincipal.isStatus()){
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else {
                    throw new AccessDeniedException(AppMessage.PERMISSION_DENY_MESSAGE);
                }
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        response.setHeader("Access-Control-Expose-Headers", "X-Auth-Token");
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String chessToken = request.getHeader("Authorization");
        if (StringUtils.hasText(chessToken) && chessToken.startsWith("Chess ")) {
            return chessToken.substring(6, chessToken.length());
        }
        return null;
    }
}
