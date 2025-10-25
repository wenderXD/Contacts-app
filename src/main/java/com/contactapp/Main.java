package com.contactapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.UUID;


public class Main {
    private static final ContactService contactService = new ContactManager();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        System.out.println("Welcome to Contact Manager Application!");
        boolean running = true;

        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addNewContact();
                case "2" -> viewAllContacts();
                case "3" -> findContact();
                case "4" -> updateContact();
                case "5" -> deleteContact();
                case "6" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n========== Contact Manager Menu ==========");
        System.out.println("1. Add a new contact");
        System.out.println("2. View all contacts");
        System.out.println("3. Find contact by ID");
        System.out.println("4. Update contact");
        System.out.println("5. Delete contact");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addNewContact() {
        System.out.println("\n--- Add New Contact ---");
        
        String id = UUID.randomUUID().toString().substring(0, 8);
        
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine().trim();
        
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine().trim();
        
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine().trim();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Enter address: ");
        String address = scanner.nextLine().trim();
        
        String createdDate = LocalDateTime.now().format(dateFormatter);
        
        Contact contact = new Contact(id, firstName, lastName, phoneNumber, email, address, createdDate);
        
        try {
            contactService.addContact(contact);
            System.out.println("✓ Contact added successfully! ID: " + id);
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private static void viewAllContacts() {
        System.out.println("\n--- All Contacts ---");
        
        var contacts = contactService.getAllContacts();
        
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
        } else {
            System.out.printf("%-10s %-20s %-15s %-30s %-30s%n", 
                    "ID", "Full Name", "Phone", "Email", "Address");
            System.out.println("=".repeat(115));
            
            for (Contact contact : contacts) {
                System.out.printf("%-10s %-20s %-15s %-30s %-30s%n",
                        contact.id(),
                        contact.getFullName(),
                        contact.phoneNumber(),
                        contact.email(),
                        contact.address());
            }
        }
    }

    private static void findContact() {
        System.out.println("\n--- Find Contact ---");
        System.out.print("Enter contact ID: ");
        String id = scanner.nextLine().trim();
        
        var contact = contactService.findContactById(id);
        
        if (contact.isPresent()) {
            Contact c = contact.get();
            System.out.println("\n✓ Contact found:");
            displayContactDetails(c);
        } else {
            System.out.println("✗ Contact not found.");
        }
    }

    private static void updateContact() {
        System.out.println("\n--- Update Contact ---");
        System.out.print("Enter contact ID to update: ");
        String id = scanner.nextLine().trim();
        
        var existingContact = contactService.findContactById(id);
        
        if (existingContact.isEmpty()) {
            System.out.println("✗ Contact not found.");
            return;
        }
        
        Contact old = existingContact.get();
        
        System.out.print("Enter new first name (current: " + old.firstName() + "): ");
        String firstName = scanner.nextLine().trim();
        if (firstName.isEmpty()) firstName = old.firstName();
        
        System.out.print("Enter new last name (current: " + old.lastName() + "): ");
        String lastName = scanner.nextLine().trim();
        if (lastName.isEmpty()) lastName = old.lastName();
        
        System.out.print("Enter new phone number (current: " + old.phoneNumber() + "): ");
        String phoneNumber = scanner.nextLine().trim();
        if (phoneNumber.isEmpty()) phoneNumber = old.phoneNumber();
        
        System.out.print("Enter new email (current: " + old.email() + "): ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) email = old.email();
        
        System.out.print("Enter new address (current: " + old.address() + "): ");
        String address = scanner.nextLine().trim();
        if (address.isEmpty()) address = old.address();
        
        Contact updatedContact = new Contact(id, firstName, lastName, phoneNumber, email, address, old.createdDate());
        
        if (contactService.updateContact(updatedContact)) {
            System.out.println("✓ Contact updated successfully!");
        }
    }

    private static void deleteContact() {
        System.out.println("\n--- Delete Contact ---");
        System.out.print("Enter contact ID to delete: ");
        String id = scanner.nextLine().trim();
        
        if (contactService.deleteContact(id)) {
            System.out.println("Contact deleted successfully!");
        } else {
            System.out.println("Contact not found.");
        }
    }

    private static void displayContactDetails(Contact contact) {
        System.out.println("  ID:          " + contact.id());
        System.out.println("  Full Name:   " + contact.getFullName());
        System.out.println("  Phone:       " + contact.phoneNumber());
        System.out.println("  Email:       " + contact.email());
        System.out.println("  Address:     " + contact.address());
        System.out.println("  Created:     " + contact.createdDate());
    }
}
