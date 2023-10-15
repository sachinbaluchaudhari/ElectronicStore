package com.electonic.store.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    private Logger logger=LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String requestHeader = request.getHeader("Authorization");
        logger.info("request header: {}",requestHeader);
        String username=null;
        String token=null;
        if (requestHeader!=null && requestHeader.startsWith("Bearer"))
        {
             token = requestHeader.substring(7);
             try {
                 username = jwtHelper.getUsernameFromToken(token);
             }catch (IllegalArgumentException e)
             {
                 logger.info("Illegal argument while fetching username!!");
                 e.printStackTrace();

             }catch (ExpiredJwtException e)
             {
                 logger.info("Given Jwt token expired!");
                 e.printStackTrace();
             }catch (MalformedJwtException e)
             {
                 logger.info("Some changes has done in token!! Invalid token");
                 e.printStackTrace();
             }catch (Exception e)
             {
                 logger.info("Exception occured!!");
                 e.printStackTrace();
             }


        }else {
            logger.info("invalid request header!!");
        }
        if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = jwtHelper.validateToken(token, userDetails);
            if (validateToken)
            {
                //set authentication in securityContextHolder
                UsernamePasswordAuthenticationToken  authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else {
                logger.info("Validation failed!!");
            }
        }
        filterChain.doFilter(request,response);
    }
}
