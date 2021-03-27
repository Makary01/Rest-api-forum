package pl.makary;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        res.setStatus(403);
        res.setContentType("application/json");
        PrintWriter printWriter = res.getWriter();
        printWriter.print("{" +
                "\"status\":" + "403" + ",\n"
                + "\"error\":" + "\"Forbidden\"" + ",\n"
                + "\"msg:\":\"" + e.getMessage() + "\"" + "\n"
                + "}");
        printWriter.flush();
    }
}
