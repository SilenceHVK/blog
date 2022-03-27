package me.hvkcoder.spring.custom.annotation;

import me.hvkcoder.spring.custom.BeanDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author h_vk
 * @since 2022/3/27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
  String value() default BeanDefinition.SCOPE_SINGLETON;
}
