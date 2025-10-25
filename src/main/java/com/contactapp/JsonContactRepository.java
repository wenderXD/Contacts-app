package com.contactapp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.file.*;
import java.util.*;


public class JsonContactRepository implements ContactRepository {
    private static final String DB_FILE = "contacts.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path dbPath;

    public JsonContactRepository() {
        this.dbPath = Paths.get("data", DB_FILE);
        ensureDataDirectoryExists();
    }

    public JsonContactRepository(String dbFilePath) {
        this.dbPath = Paths.get(dbFilePath);
        ensureDataDirectoryExists();
    }

    private void ensureDataDirectoryExists() {
        try {
            if (!Files.exists(dbPath.getParent())) {
                Files.createDirectories(dbPath.getParent());
            }
        } catch (IOException e) {
            System.err.println("Failed to create data directory: " + e.getMessage());
        }
    }

    @Override
    public void saveContacts(List<Contact> contacts) {
        try {
            String json = gson.toJson(contacts);
            Files.writeString(dbPath, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Contacts saved to " + dbPath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving contacts: " + e.getMessage());
        }
    }

    @Override
    public List<Contact> loadContacts() {
        try {
            if (!Files.exists(dbPath)) {
                return new ArrayList<>();
            }

            String json = Files.readString(dbPath);
            if (json.trim().isEmpty()) {
                return new ArrayList<>();
            }

            List<Contact> contacts = gson.fromJson(json, new TypeToken<List<Contact>>() {}.getType());
            return contacts != null ? contacts : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error loading contacts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean exists() {
        return Files.exists(dbPath);
    }

    @Override
    public void clear() {
        try {
            Files.deleteIfExists(dbPath);
            System.out.println("Database cleared");
        } catch (IOException e) {
            System.err.println("Error clearing database: " + e.getMessage());
        }
    }

    public String getDatabasePath() {
        return dbPath.toAbsolutePath().toString();
    }
}
