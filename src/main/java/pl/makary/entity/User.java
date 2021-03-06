package pl.makary.entity;

import io.swagger.annotations.ApiParam;
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
    @ApiParam(hidden = true)
    private Long id;

    @Column(nullable = false, unique = true)
    @UsernameConstraint
    @ApiParam(hidden = true)
    private String username;

    @PasswordConstraint
    @ApiParam(hidden = true)
    private String password;

    @Email
    @ApiParam(hidden = true)
    private String email;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ApiParam(hidden = true)
    private Set<Role> roles;

    @NotNull
    @ApiParam(hidden = true)
    private int status;

    @CreatedDate
    @ApiParam(hidden = true)
    private LocalDateTime created;

    @NotNull
    @ApiParam(hidden = true)
    private LocalDateTime lastOnline;

    public User() {
    }

    public User(Long id, String username, String password, @Email String email, Set<Role> roles, @NotNull int status, LocalDateTime created, @NotNull LocalDateTime lastOnline) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.status = status;
        this.created = created;
        this.lastOnline = lastOnline;
    }
}
