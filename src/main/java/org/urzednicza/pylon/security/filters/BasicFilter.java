package org.urzednicza.pylon.security.filters;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.urzednicza.pylon.config.Config;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class BasicFilter extends BasicAuthenticationFilter {


    public BasicFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
        System.out.println("lolz");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException{
        String header = req.getHeader("Authorization");

        if(header== null) {
            chain.doFilter(req, res);
            return;
        }

        if(header.equals(Config.get("password")))
        {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("Artanis",null,new ArrayList<GrantedAuthority>()));
            chain.doFilter(req,res);
        }
        else {
            SecurityContextHolder.getContext().setAuthentication(null);
            chain.doFilter(req,res);
        }
    }
}
