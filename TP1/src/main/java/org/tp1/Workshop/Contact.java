package org.tp1.Workshop;

public record Contact(String email, int phone) {
    public static boolean validateEmail(String email) {
        return email.matches(
                "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
    }

    public static boolean validatePhone(String phone) {
        return phone.matches("^\\d{9}$");
    }
}
