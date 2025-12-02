/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zen.sin.bio.orassbio.exception;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author USER01
 */
@Getter
@Setter
public class ApiException extends RuntimeException {
    private String code;
    private String message;
    private String details;

    public ApiException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ApiException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public ApiException(String code, String message, String details) {
        super(message);
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public ApiException(String code, String message, String details, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.details = details;
    }
    
}
