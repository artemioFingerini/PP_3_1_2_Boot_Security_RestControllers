package ru.kata.spring.boot_security.demo.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {

    private final UserRepository usersRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository usersRepository, RoleRepository roleRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }


    @Transactional(readOnly = true)
    public User getById(Long id) {
        return usersRepository.getById(id);
    }


    @Transactional
    public void saveUser(@Valid User user, List<Long> roleIds) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        for (Long roleId : roleIds) {
            Optional<Role> role = roleRepository.findById(roleId);
            user.addRole(role.orElse(null));
        }
        usersRepository.save(user);
    }


    @Transactional
    public void deleteUserById(Long id) {
        User user = usersRepository.getById(id);
        usersRepository.delete(user);
    }

        @Transactional
        public void updateUser(@Valid User user, List<Long> roleIds) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().clear();
        for (Long roleId : roleIds) {
            Optional<Role> role = roleRepository.findById(roleId);
            user.addRole(role.orElse(null));
        }
        usersRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) {
        return usersRepository.findByUsername(username);
    }


}

