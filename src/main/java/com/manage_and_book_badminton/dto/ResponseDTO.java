package com.manage_and_book_badminton.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;
    private int status;
    private String timestamp;
}
