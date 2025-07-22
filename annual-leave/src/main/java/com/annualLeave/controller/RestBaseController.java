// src/main/java/com/annualLeave/controller/RestBaseController.java
package com.annualLeave.controller;

import com.annualLeave.model.RootEntity;

public class RestBaseController {

    public <T> RootEntity<T> ok(T data) {
        return RootEntity.ok(data);
    }

    public <T> RootEntity<T> error(String message) {
        return RootEntity.error(message);
    }
}
