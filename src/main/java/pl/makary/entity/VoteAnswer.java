package pl.makary.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class VoteAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    boolean isPositive;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private Answer answer;
}
