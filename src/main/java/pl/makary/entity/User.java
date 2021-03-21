package pl.makary.entity;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import pl.makary.validators.annotation.PasswordConstraint;
import pl.makary.validators.annotation.UsernameConstraint;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Where(clause = "status = 1")
@SQLDelete(sql = "UPDATE user SET status = 0 WHERE id = ?")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @UsernameConstraint
    private String username;

    @PasswordConstraint
    private String password;

    @Email
    private String email;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @NotNull
    private int status;

    @CreatedDate
    private LocalDateTime created;

    @NotNull
    private LocalDateTime lastOnline;
}
