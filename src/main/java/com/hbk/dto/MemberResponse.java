package com.hbk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponse {
    private String token;
    private String firstName;
    private String lastName;
}