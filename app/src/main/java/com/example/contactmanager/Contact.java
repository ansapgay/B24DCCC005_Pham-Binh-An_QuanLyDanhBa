package com.example.contactmanager;

import java.io.Serializable;

public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String phone;
    private String group;
    private String gender;
    private boolean isFavorite;
    private String note;

    public Contact(String name, String phone, String group, String gender,
                   boolean isFavorite, String note) {
        this.name = name;
        this.phone = phone;
        this.group = group;
        this.gender = gender;
        this.isFavorite = isFavorite;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGroup() {
        return group;
    }

    public String getGender() {
        return gender;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getNote() {
        return note;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getInitial() {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }

        String[] parts = name.trim().split("\\s+");
        String firstLetter = parts[parts.length - 1].substring(0, 1);
        return firstLetter.toUpperCase(java.util.Locale.ROOT);
    }
}
