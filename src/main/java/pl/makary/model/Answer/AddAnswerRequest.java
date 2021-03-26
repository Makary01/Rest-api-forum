package pl.makary.model.Answer;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class AddAnswerRequest {

    @NotNull
    private UUID postId;

    @NotNull
    @Size(min=10, max = 10000)
    private String content;
}
