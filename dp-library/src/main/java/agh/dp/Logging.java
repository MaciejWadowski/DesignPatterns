package agh.dp;

import agh.dp.facade.RoleWithPermissionsFacade;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

@Configuration
@Aspect
public class Logging {

    /**
     * Test pointcuts to catch Repository methods
     */
    @Pointcut("execution(* *.save(..))")
    public void catchSave() {}

    @Before("catchSave()")
    public void caughtMethod() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {

            String username = ((UserDetails)principal).getUsername();
            System.out.println(username);
            System.out.println(1);
        } else {

            String username = principal.toString();
            System.out.println(username);
            System.out.println(2);
        }
        System.out.println("Caught save method");
        int roleId = 1;
    }

    @Pointcut("execution(* *.findById(..))")
    public void catchFindById() {}

    @Before("catchFindById()")
    public void caughtFindByIdMethod() {
        System.out.println("Caught find by id method");
    }

    @Pointcut("execution(* *.setFirstName(String))")
    public void catchNameChanged() {}

    @Before("catchNameChanged()")
    public void caughtSetFirstName() {
        System.out.println("CAUGHT");
    }
}