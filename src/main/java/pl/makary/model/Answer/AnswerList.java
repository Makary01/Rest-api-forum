package pl.makary.model.Answer;

import lombok.Data;

import java.util.List;

@Data
public class AnswerList {
    private List<AnswerModel> answers;
    private Integer pageNumber;
    private Integer totalPages;
}
