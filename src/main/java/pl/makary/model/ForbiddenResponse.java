package pl.makary.model;

public class ForbiddenResponse extends ErrorResponse {


    public ForbiddenResponse(String msg) {
        super(msg);
        this.error = "Forbidden";
        this.status = 403;
    }
}
