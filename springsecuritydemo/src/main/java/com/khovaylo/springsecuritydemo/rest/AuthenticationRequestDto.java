package com.khovaylo.springsecuritydemo.rest;

import lombok.Data;

/**
 * @author Pavel Khovaylo
 */
@Data
public class AuthenticationRequestDto {
    private String email;
    private String password;
}
