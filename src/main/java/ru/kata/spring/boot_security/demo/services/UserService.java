package ru.kata.spring.boot_security.demo.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.exception_handling.NoSuchUserException;
import ru.kata.spring.boot_security.demo.exception_handling.UsernameAlreadyExistsException;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
        return usersRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserException("User with ID " + id + " not found"));
    }


    @Transactional
    public User saveUser(@Valid User user) {
        if (usersRepository.findByUsername(user.getUsername())!=null) {
            throw new UsernameAlreadyExistsException("Имя пользователя " + user.getUsername() + " занято, выберите другое имя!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            Role existingRole = roleRepository.findById(role.getId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(existingRole);
        }

        user.setRoles(roles);
        usersRepository.save(user);
        return user;
    }


    @Transactional
    public void deleteUserById(Long id) {
        User user = usersRepository.getById(id);
        usersRepository.delete(user);
    }
    @Transactional
    public User updateUser(Long id, @Valid User updatedUser) {
        User existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserException("User with ID " + id + " not found"));
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (usersRepository.findByUsername(updatedUser.getUsername()) != null &&
                !existingUser.getUsername().equals(updatedUser.getUsername())) {
            throw new UsernameAlreadyExistsException("Имя пользователя " + updatedUser.getUsername() + " занято, выберите другое имя!");
        }

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setRoles(updatedUser.getRoles());
        return usersRepository.save(existingUser);
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

    @Transactional
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

}

