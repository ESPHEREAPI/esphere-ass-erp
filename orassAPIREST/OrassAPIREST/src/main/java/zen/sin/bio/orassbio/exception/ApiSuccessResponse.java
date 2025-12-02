/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package zen.sin.bio.orassbio.exception;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
/**
 *
 * @author USER01
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
class ApiSuccessResponse<T> {
    
    private String code;
    private String message;
    private T data;
    private String traceId;
    private LocalDateTime timestamp;

    public static <T> ApiSuccessResponse<T> success(String message, T data) {
        return ApiSuccessResponse.<T>builder()
                .code("SUCCESS")
                .message(message)
                .data(data)
                .traceId(java.util.UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
    
}
