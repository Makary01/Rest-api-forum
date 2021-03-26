package pl.makary.exception;

public class IncorrectPostIdException extends ValidationException{
    public IncorrectPostIdException(){
        this.message = "Incorrect post id";
        this.objectName = "Id";
    }
}
