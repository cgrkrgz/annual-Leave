// src/main/java/com/annualLeave/model/RootEntity.java
package com.annualLeave.model;

import java.time.LocalDateTime;

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

    
    // burası hic bir yerde donmuyor eger boyle donulmek ıstenırse cagırılır.
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
