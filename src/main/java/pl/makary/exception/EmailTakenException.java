package pl.makary.exception;

public class EmailTakenException extends ValidationException{
    public EmailTakenException(){
        this.message = "email already taken";
        this.objectName = "email";
    }
}
