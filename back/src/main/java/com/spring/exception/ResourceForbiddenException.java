package com.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Object forbidden")
public class ResourceForbiddenException   extends RuntimeException{
    private Integer status = 403;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    private static final long serialVersionUID = 1L;

    public ResourceForbiddenException(String message) {
        super(message);
    }

}


