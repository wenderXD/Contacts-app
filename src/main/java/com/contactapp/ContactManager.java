package com.contactapp;

import java.util.*;


public class ContactManager implements ContactService {
    private final List<Contact> contacts = new ArrayList<>();
    private final ContactRepository repository;

    public ContactManager() {
        this(new JsonContactRepository());
    }

    public ContactManager(ContactRepository repository) {
        this.repository = repository;
        loadContactsFromStorage();
    }

    private void loadContactsFromStorage() {
        List<Contact> loaded = repository.loadContacts();
        contacts.addAll(loaded);
        if (!loaded.isEmpty()) {
            System.out.println("✓ Loaded " + loaded.size() + " contacts from database");
        }
    }

    private void saveToStorage() {
        repository.saveContacts(new ArrayList<>(contacts));
    }

    public void addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
        if (findContactById(contact.id()).isPresent()) {
            throw new IllegalArgumentException("Contact with ID " + contact.id() + " already exists");
        }
        contacts.add(contact);
        saveToStorage();
    }


    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts);
    }

    public Optional<Contact> findContactById(String id) {
        return contacts.stream()
                .filter(c -> c.id().equals(id))
                .findFirst();
    }


    public boolean deleteContact(String id) {
        boolean removed = contacts.removeIf(c -> c.id().equals(id));
        if (removed) {
            saveToStorage();
        }
        return removed;
    }

    public boolean updateContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
        
        int index = -1;
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).id().equals(contact.id())) {
                index = i;
                break;
            }
        }
        
        if (index >= 0) {
            contacts.set(index, contact);
            saveToStorage();
            return true;
        }
        return false;
    }
}
