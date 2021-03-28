package pl.makary.model.comment;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AddCommentRequest {
    @NotNull
    @Size(min = 8, max = 8000)
    private String content;
}
