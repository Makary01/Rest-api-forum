package pl.makary.exception;

public class IncorrectPasswordException extends ValidationException{
    public IncorrectPasswordException(){
        this.message = "password incorrect";
        this.objectName = "password";
    }

}
