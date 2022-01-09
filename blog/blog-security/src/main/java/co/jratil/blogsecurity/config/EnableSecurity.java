package co.jratil.blogsecurity.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author jun
 * @version 1.0
 * @created 2022/1/8 20:37
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SecurityAutoConfiguration.class)
public @interface EnableSecurity {
}
