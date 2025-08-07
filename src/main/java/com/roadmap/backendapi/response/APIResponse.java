package com.roadmap.backendapi.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * APIResponse class represents a standard response structure for API responses.
 * It contains a message and data object to be returned in the response.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private String message;
    private Object data;
}
