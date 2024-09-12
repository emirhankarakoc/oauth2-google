package com.karakoc.sofra.exceptions.general;

import com.karakoc.sofra.exceptions.RestException;
import lombok.Getter;
import org.springframework.http.HttpStatus;


public class ForbiddenException extends RestException {
    @Getter
    private final HttpStatus httpStatus;

    public ForbiddenException(String msg) {
        super(msg);
        this.httpStatus = HttpStatus.FORBIDDEN;
    }

}

