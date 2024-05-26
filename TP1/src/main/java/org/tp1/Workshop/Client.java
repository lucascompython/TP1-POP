package org.tp1.Workshop;

public final class Client {
    public static int count = 0;
    private String name;
    private final int id;
    private NIF nif;
    private final Contact contact;

    public Client(String name, NIF nif, Contact contact) {
        this.id = ++count;
        this.name = name;
        this.nif = nif;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public NIF getNIF() {
        return nif;
    }

    public Contact getContact() {
        return contact;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNIF(NIF nif) {
        this.nif = nif;
    }

    public void print() {
        System.out.println("Client [id=" + id + ", name=" + name + ", nif=" + nif + ", contact=" + contact + "]");
    }

}
