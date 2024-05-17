package org.tp1;

public record Contact(String email, int phone) {
    public Contact {
        if (!validateEmail(email))
            throw new IllegalArgumentException("Invalid email");
        if (!validatePhone(phone))
            throw new IllegalArgumentException("Invalid phone");
    }

    public static boolean validateEmail(String email) {
        return email.matches(
                "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
    }

    public static boolean validatePhone(int phone) {
        return phone > 99999999 && phone < 1000000000;
    }
}
