package com.quoll.da.filter;

import com.alibaba.fastjson.JSONObject;
import com.quoll.da.pojo.JwtUtils;
import com.quoll.da.pojo.Result;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = request.getRequestURL().toString();
        System.out.println("Filter url: " + url);

        if (url.contains("login")) {
            System.out.println("Filter login");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String jwt = request.getHeader("token");

        if (!StringUtils.hasLength(jwt)) {
            System.out.println("Filter token is empty");
            Result result = Result.error("Unauthenticated");
            String unauth = JSONObject.toJSONString(result);
            response.getWriter().write(unauth);
            return;
        }

        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Filter token is invalid");
            Result result = Result.error("Unauthenticated");
            String unauth = JSONObject.toJSONString(result);
            response.getWriter().write(unauth);
            return;
        }

        System.out.println("Filter token is valid");
        filterChain.doFilter(request, response);
    }
}
