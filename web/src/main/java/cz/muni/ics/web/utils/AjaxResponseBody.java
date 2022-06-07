package cz.muni.ics.web.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

@Data
@NoArgsConstructor
public class AjaxResponseBody {

    Object data;

    HttpStatus code;

    String redirectURL;

    public AjaxResponseBody(HttpServletResponse response){
        this.setData("");
        this.setCode(HttpStatus.resolve(response.getStatus()));
    }
}
