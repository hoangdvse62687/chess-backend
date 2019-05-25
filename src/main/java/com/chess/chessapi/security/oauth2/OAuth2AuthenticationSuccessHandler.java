package com.chess.chessapi.security.oauth2;

import com.chess.chessapi.config.AppProperties;
import com.chess.chessapi.constant.AppRole;
import com.chess.chessapi.exception.BadRequestException;
import com.chess.chessapi.security.TokenProvider;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.services.UserService;
import com.chess.chessapi.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final String REGISTRATION_URI = "/user/registration";
    private static final String HOME_LEARNER_URI = "/learner/home";
    private static final String HOME_INSTRUCTOR_URI = "/instructor/home";
    private static final String HOME_ADMIN_URI = "/admin/home";
    private TokenProvider tokenProvider;

    private AppProperties appProperties;

    @Autowired
    private UserService userService;

    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;


    @Autowired
    OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider, AppProperties appProperties,
                                       HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.tokenProvider = tokenProvider;
        this.appProperties = appProperties;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
        String targetUrl = "";

        UserPrincipal currentUser = userService.getCurrentUser();
        String role = currentUser.getAuthorities().toString();
        if(role.contains(AppRole.ROLE_REGISTRATION)){
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

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
