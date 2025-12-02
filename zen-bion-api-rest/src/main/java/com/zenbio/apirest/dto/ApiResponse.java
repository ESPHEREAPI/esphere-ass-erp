/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zenbio.apirest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author USER01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
     private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private Integer status;
    private LocalDateTime timestamp;
    private Integer count; // ✅ nouveau champ

    // Constructeur de base
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        if (data instanceof List<?>) {
            this.count = ((List<?>) data).size();
        }
    }

    // Méthode de succès générique
    public static <T> ApiResponse<T> success(T data, String message) {
        Integer count = (data instanceof List<?>) ? ((List<?>) data).size() : null;
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .status(200)
                .success(true)
                .count(count)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Méthode "created"
    public static <T> ApiResponse<T> created(T data, String message) {
        Integer count = (data instanceof List<?>) ? ((List<?>) data).size() : null;
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .status(201)
                .success(true)
                .count(count)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Méthode d’erreur
    public static <T> ApiResponse<T> error(String message, Integer status) {
        return ApiResponse.<T>builder()
                .data(null)
                .message(message)
                .status(status)
                .success(false)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> conflict(String message) {
        return error(message, 409);
    }
}
