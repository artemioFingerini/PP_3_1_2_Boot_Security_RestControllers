package ru.kata.spring.boot_security.demo.validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UserNameUniqueValidator implements ConstraintValidator<UniqueUsername, String> {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null) {
            return true;
        }
        User user = userRepository.findByUsername(username);
        return user == null;
    }
}
