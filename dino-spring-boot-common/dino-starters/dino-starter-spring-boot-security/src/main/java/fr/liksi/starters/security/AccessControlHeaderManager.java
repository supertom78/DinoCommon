package fr.liksi.starters.security;

import fr.liksi.utils.HeaderUtils;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

public class AccessControlHeaderManager implements AuthorizationManager<RequestAuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        return new AuthorizationDecision(HeaderUtils.getParcFromRequest(context.getRequest()).isPresent());
    }
}
