/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zen.sin.bio.orassbio.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 *
 * @author USER01
 */
@Getter
public class InvalidFormatException extends RuntimeException {
    
    private final String errorCode;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final transient Object[] params;
    
    public InvalidFormatException(String message, Object... params) {
        super(message);
        this.errorCode = "INVALID_FORMAT";
        this.params = params;
    }
    
}
