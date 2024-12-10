package com.example.canteen.exception;


import com.example.canteen.config.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler (value = Exception.class)
    ResponseEntity<ApiResponse>  handlingRuntimeException(Exception exception){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNKNOWN.getCode());
        apiResponse.setMessage(ErrorCode.UNKNOWN.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }


    @ExceptionHandler (value = AppExeception.class)
    ResponseEntity<ApiResponse>  handlingRuntimeException(AppExeception exception){
       ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(apiResponse);
    }
}
