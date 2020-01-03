package com.spring.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class RegistrationDto {

    @NotBlank(message = "Please, enter your email address")
    @Email(message = "Email has to be real")
    private String username;

    @NotBlank(message = "Password can not be empty")
    @Length(min = 6, message = "Minimum password length is 6")
    private String password;

    @NotBlank(message = "Confirm password please")
    private String repeatedPassword;

    public RegistrationDto() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    public boolean isPasswordConfirmed() {

        if (password == null || repeatedPassword == null) {
            return false;
        }

        return this.password.equals(this.repeatedPassword);
    }
}
