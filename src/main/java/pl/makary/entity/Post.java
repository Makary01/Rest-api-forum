package pl.makary.entity;


import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import pl.makary.validators.annotation.ContentConstraint;
import pl.makary.validators.annotation.TitleConstraint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "user_sequence"),
                    @Parameter(name = "initial_value", value = "4"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(unique = true)
    private Long id;

    @TitleConstraint
    private String title;

    @ContentConstraint
    private String content;

    @NotNull
    @CreatedBy
    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Section section;

    @NotNull
    @CreatedDate
    private LocalDateTime created;

    private LocalDateTime edited;

    @NotNull
    private Integer rating;

    @NotNull
    private Integer popularity;

    @NotNull
    private Integer status;
}
