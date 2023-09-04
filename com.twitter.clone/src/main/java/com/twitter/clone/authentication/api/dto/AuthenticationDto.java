package com.twitter.clone.authentication.api.dto;

public class AuthenticationDto {
    public record LoginDto(String email, String password){ }
    public record SignupDto(String email, String UserName , String password){ }
}
