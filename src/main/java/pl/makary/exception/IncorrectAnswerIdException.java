package pl.makary.exception;

public class IncorrectAnswerIdException extends ValidationException{
    public IncorrectAnswerIdException(){
        this.message = "Incorrect answer id";
        this.objectName = "answerId";
    }
}
