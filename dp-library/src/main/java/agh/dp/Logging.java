package agh.dp;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth);
        System.out.println("Caught save method");
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