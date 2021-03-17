package pl.makary.exception;

public class UsernameTakenException extends ValidationException{
    public UsernameTakenException(){
        this.message = "username already taken";
        this.objectName = "username";
    }
}
