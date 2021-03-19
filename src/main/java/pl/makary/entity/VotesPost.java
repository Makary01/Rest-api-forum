package pl.makary.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class VotesPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    @ManyToOne
    private Post post;
}
