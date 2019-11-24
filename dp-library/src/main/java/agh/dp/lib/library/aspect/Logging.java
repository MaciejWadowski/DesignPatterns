package agh.dp.lib.library.aspect;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
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
        System.out.println("Caught save method");
    }

    @Pointcut("execution(* *.findById(Long))")
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

    @Pointcut("execution(* *(..))")
    public void catchThemAll() {}

    @Before("catchThemAll()")
    public void catchingAll() {
        System.out.println("lelele");
    }

}