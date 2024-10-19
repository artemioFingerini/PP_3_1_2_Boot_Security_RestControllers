package ru.kata.spring.boot_security.demo.entities;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Имя не должно быть пустым")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё]+$", message = "Имя может содержать только буквы")
    private String firstName;

    @Column(name = "lastname")
    @NotBlank(message = "Фамилия не должна быть пустой")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё]+$", message = "Фамилия может содержать только буквы")
    private String lastName;


    @Column(name = "email")
    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Введите корректный адрес электронной почты")
    private String email;

    @Column(name = "username")
    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Пароль не должен быть пустым")
    private String password;


    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @NotEmpty(message = "Должна быть выбрана хотя бы одна роль")
    private Set<Role> roles = new HashSet<>();


    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }
    public User(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public void addRole(Role role) {
        this.roles.add(role);
    }
}

