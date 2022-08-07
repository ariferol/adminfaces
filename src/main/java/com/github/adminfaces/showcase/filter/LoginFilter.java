/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.showcase.filter;

import com.github.adminfaces.showcase.model.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Bu filter ile auth olan user credential bilgisi yoksa session close ediyoruz.
 *
 * @author aerol
 */
//@WebFilter(filterName = "LoginFilter", urlPatterns = {"/sample-context-url/*"})
public class LoginFilter implements Filter {

    private boolean isRedirectLogin=false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Logger.getLogger(LoginFilter.class.getName()).log(Level.INFO, "LoginFilter init methoduna girdi ");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String url = req.getRequestURI();
        User user = (User) req.getSession().getAttribute("valided_user");
        String loginLink = "/sample-context-url/login.xhtml";
        String homePageLink = "/sample-context-url/pages/index.xhtml";
        if (user == null) {
            if (url.contains("logout")) {
                res.sendRedirect(req.getContextPath() + loginLink);
            } else if(!isRedirectLogin){
//                chain.doFilter(request, response);
                res.sendRedirect(req.getContextPath() + loginLink);
                isRedirectLogin = true;
            }
            chain.doFilter(request, response);
        } else {
            if (url.contains("login")) {
                res.sendRedirect(req.getContextPath() +homePageLink);
            } else if (url.contains("logout")) {
                req.getSession().invalidate();
                res.sendRedirect(req.getContextPath() + loginLink);
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
        Logger.getLogger(LoginFilter.class.getName()).log(Level.INFO, "LoginFilter destroy methoduna girdi ");
    }
}
