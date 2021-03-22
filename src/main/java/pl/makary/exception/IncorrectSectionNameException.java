package pl.makary.exception;

public class IncorrectSectionNameException extends ValidationException {
    public IncorrectSectionNameException() {
        this.message = "Incorrect section name";
        this.objectName = "sectionName";
    }
}
