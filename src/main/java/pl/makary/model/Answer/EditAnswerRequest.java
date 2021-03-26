package pl.makary.model.Answer;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EditAnswerRequest {

    @NotNull
    @Size(min=10, max = 10000)
    private String content;
}
