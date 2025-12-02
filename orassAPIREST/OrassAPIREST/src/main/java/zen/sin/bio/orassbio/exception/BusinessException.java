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
class BusinessException extends ApiException {
    public BusinessException(String message) {
        super("BUSINESS_ERROR", message);
    }

    public BusinessException(String message, Throwable cause) {
        super("BUSINESS_ERROR", message, cause);
    }

    public BusinessException(String message, String details) {
        super("BUSINESS_ERROR", message, details);
    }
    
}
