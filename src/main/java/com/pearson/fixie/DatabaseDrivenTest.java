package com.pearson.fixie;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface DatabaseDrivenTest {
    String defaultPackage() default "";
    String[] fixtures() default {};
    Class<? extends EntityPostProcessor>[] postProcessors() default {};
}