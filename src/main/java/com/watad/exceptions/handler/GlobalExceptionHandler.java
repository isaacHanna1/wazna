package com.watad.exceptions.handler;

import com.watad.dto.response.ApiErrorResponse;
import com.watad.exceptions.ProfileException;
import com.watad.exceptions.QrCodeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(QrCodeException.class)
    public ResponseEntity<ApiErrorResponse> hanldeQRException(QrCodeException ex){
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse("QR ERROR ", ex.getMessage() ,  HttpStatus.BAD_REQUEST.value());
        return  new ResponseEntity<>(apiErrorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> hanldeSqlIntegrityConstraint(SQLIntegrityConstraintViolationException ex){
        String message  = detemineTheMessage(ex);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse("ERROR ", message ,  HttpStatus.BAD_REQUEST.value());
        return  new ResponseEntity<>(apiErrorResponse,HttpStatus.BAD_REQUEST);
    }


//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiErrorResponse> handleException( Exception ex){
//        ApiErrorResponse apiErrorResponse = new ApiErrorResponse("Global ERROR " , ex.getMessage() ,HttpStatus.BAD_REQUEST.value());
//        return new ResponseEntity<>(apiErrorResponse,HttpStatus.BAD_REQUEST);
//    }

    private String  detemineTheMessage(SQLIntegrityConstraintViolationException ex){

        if(ex.getMessage().contains("attendance.PRIMARY")){
            return "This QR code has already been scanned.";
        }
        if(ex.getMessage().contains("user.user_name")) {
            return "Oops! It looks like this phone number is already saved. ";
        }
        return "Check Isaac this Exception not handled "+ex.getMessage();
    }

    @ExceptionHandler(ProfileException.class)
    public String handleProfileException(ProfileException ex, Model model) {
        model.addAttribute("errorMessage", "Profile Error: " + ex.getMessage());
        return "error";
    }

}

