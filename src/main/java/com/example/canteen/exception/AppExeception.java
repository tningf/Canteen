package com.example.canteen.exception;

import com.example.canteen.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AppExeception extends RuntimeException{
    private ErrorCode errorCode;

    public AppExeception(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
