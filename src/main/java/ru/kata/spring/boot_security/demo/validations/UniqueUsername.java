package ru.kata.spring.boot_security.demo.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserNameUniqueValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
    String message() default "Пользователь с таким именем уже существует";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

