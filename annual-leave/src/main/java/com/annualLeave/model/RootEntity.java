package com.annualLeave.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RootEntity<T> {
    private boolean success;
    private String message;
    private T data;
    
    // Hata detayı için metadata
    private String hostName;
    private String path;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    private int statusCode;

    public static <T> RootEntity<T> ok(T data) {
        RootEntity<T> root = new RootEntity<>();
        root.setSuccess(true);
        root.setMessage("İşlem başarılı");
        root.setData(data);
        root.setTimestamp(LocalDateTime.now());
        return root;
    }

    public static <T> RootEntity<T> error(String message) {
        RootEntity<T> root = new RootEntity<>();
        root.setSuccess(false);
        root.setMessage(message);
        root.setTimestamp(LocalDateTime.now());
        return root;
    }

    public static <T> RootEntity<T> errorWithData(String message, T data, String hostName, String path, int statusCode) {
        RootEntity<T> root = new RootEntity<>();
        root.setSuccess(false);
        root.setMessage(message);
        root.setData(data);
        root.setHostName(hostName);
        root.setPath(path);
        root.setTimestamp(LocalDateTime.now());
        root.setStatusCode(statusCode);
        return root;
    }
}