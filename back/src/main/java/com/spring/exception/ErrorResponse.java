package com.spring.exception;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
@XmlRootElement(name = "error")
public class ErrorResponse {
    private Date timestamp;
    private String message;
    private String details;

    ErrorResponse(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

}
