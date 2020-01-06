package agh.dp;

import agh.dp.facade.RoleWithPermissionsFacade;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

public class Logging extends EmptyInterceptor {

    private static final long serialVersionUID = 1L;
    // Define a static logger

    @Override
    public boolean onSave(
            Object entity,
            Serializable id,
            Object[] state,
            String[] propertyNames,
            Type[] types) {
        System.out.println("");
        return false;
    }
    // Logging SQL statement
    @Override
    public String onPrepareStatement(String sql) {
        System.out.println(sql);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {

            String username = ((UserDetails)principal).getUsername();
            System.out.println(username);
        } else {

            String username = principal.toString();
            System.out.println(username);
        }
        return super.onPrepareStatement(sql);
    }

}