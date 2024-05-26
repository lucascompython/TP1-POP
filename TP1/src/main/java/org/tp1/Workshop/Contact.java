package org.tp1.Workshop;

public final class Contact {
    private String email;
    private int phone;

    public Contact(String email, int phone) {
        this.email = email;
        this.phone = phone;
    }

    public static boolean validateEmail(String email) {
        return email.matches(
                "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
    }

    public static boolean validatePhone(String phone) {
        return phone.matches("^\\d{9}$");
    }

    public String getEmail() {
        return email;
    }

    public int getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Contact[" +
                "email=" + email + ", " +
                "phone=" + phone + ']';
    }

}
