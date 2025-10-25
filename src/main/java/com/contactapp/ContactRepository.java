package com.contactapp;

import java.util.List;


public interface ContactRepository {

    void saveContacts(List<Contact> contacts);



    List<Contact> loadContacts();

    boolean exists();

    void clear();
}
