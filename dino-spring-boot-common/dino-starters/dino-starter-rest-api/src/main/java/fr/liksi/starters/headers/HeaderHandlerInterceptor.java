package fr.liksi.starters.headers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class HeaderHandlerInterceptor implements HandlerInterceptor {

    private final HeadersHolder headersHolder;

    public HeaderHandlerInterceptor(HeadersHolder headersHolder) {
        this.headersHolder = headersHolder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        headersHolder.setHeadersFromRequest(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        headersHolder.clear();
    }
}
