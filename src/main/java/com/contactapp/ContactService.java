package com.contactapp;

import java.util.List;
import java.util.Optional;


public interface ContactService {

    void addContact(Contact contact);

  
    List<Contact> getAllContacts();

    Optional<Contact> findContactById(String id);


    boolean deleteContact(String id);


    boolean updateContact(Contact contact);
}
