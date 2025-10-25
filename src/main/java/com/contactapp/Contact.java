package com.contactapp;


public record Contact(
        String id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String address,
        String createdDate
) {

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
