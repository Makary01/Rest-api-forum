package pl.makary.util;

import io.swagger.annotations.ApiParam;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


import org.springframework.security.core.userdetails.User;
public class CurrentUser extends User {
    private final pl.makary.entity.User user;
    public CurrentUser(String username, String password,
                       Collection<? extends GrantedAuthority> authorities,
                       pl.makary.entity.User user) {
        super(username, password, authorities);
        this.user = user;
    }
    public pl.makary.entity.User getUser() {return user;}
}
