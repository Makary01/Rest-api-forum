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
import java.util.UUID;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(unique = true,columnDefinition = "BINARY(16)")
    private UUID id;

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
