package pl.makary.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class DeleteUserRequest {

    @NotNull
    @Size(min = 5, max=60, message = "Password have to be 5 to 60 charters long")
    private String password;
}
