package org.tp1;

public record Contact(String email, int phone) {
    public Contact {
        if (!validateEmail(email)) throw new IllegalArgumentException("Invalid email");
    }

    public static boolean validateEmail(String email) {
        return email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
    }
}
