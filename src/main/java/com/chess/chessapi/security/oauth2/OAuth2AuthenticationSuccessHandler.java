package com.chess.chessapi.security.oauth2;

import com.chess.chessapi.config.AppProperties;
import com.chess.chessapi.constant.AppRole;
import com.chess.chessapi.model.JsonResult;
import com.chess.chessapi.security.TokenProvider;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.util.CookieUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final String REGISTRATION_URI = "/user/registration";
    private static final String HOME_LEARNER_URI = "/learner/home";
    private static final String HOME_INSTRUCTOR_URI = "/instructor/home";
    private static final String HOME_ADMIN_URI = "/admin/home";
    private TokenProvider tokenProvider;

    private AppProperties appProperties;

    private ObjectMapper mapper;

    @Autowired
    private UserService userService;

    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;


    @Autowired
    OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider, AppProperties appProperties,
                                       HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                                       ObjectMapper mapper) {
        this.tokenProvider = tokenProvider;
        this.appProperties = appProperties;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        mapper.writeValue(writer, new JsonResult(null,targetUrl));
        writer.flush();
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);


        String targetUrl = "";

        UserPrincipal currentUser = userService.getCurrentUser();
        String role = currentUser.getAuthorities().toString();
        if(redirectUri.isPresent()){
            targetUrl = redirectUri.get();
        }else if(role.contains(AppRole.ROLE_REGISTRATION)){
            targetUrl = REGISTRATION_URI;
        }else if(role.contains(AppRole.ROLE_LEARNER)){
            targetUrl = HOME_LEARNER_URI;
        }else if(role.contains(AppRole.ROLE_INSTRUCTOR)){
            targetUrl = HOME_INSTRUCTOR_URI;
        }else if(role.contains(AppRole.ROLE_ADMIN)){
            targetUrl = HOME_ADMIN_URI;
        }


        String token = tokenProvider.createToken(authentication);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
