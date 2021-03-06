package com.sandbox_maven.package3.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {
    @Pointcut("execution(* com.sandbox_maven.package3.services.BookService.get*(..))")
    public void allGetMethods() {}

    @Pointcut("execution(* com.sandbox_maven.package3.services.BookService.add*(..))")
    public void allAddMethods() {}
}
