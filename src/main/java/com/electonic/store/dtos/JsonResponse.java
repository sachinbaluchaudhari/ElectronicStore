package com.electonic.store.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class JsonResponse {
    private long timestamp;
    private HttpStatus status;
    private String message;
}
