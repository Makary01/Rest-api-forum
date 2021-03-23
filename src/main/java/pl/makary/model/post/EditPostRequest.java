package pl.makary.model.post;

import lombok.Data;
import pl.makary.validators.annotation.ContentConstraint;
import pl.makary.validators.annotation.TitleConstraint;

import javax.validation.constraints.NotNull;

@Data
public class EditPostRequest {
    @TitleConstraint
    private String title;

    @ContentConstraint
    private String content;

    @NotNull
    private String sectionName;
}
