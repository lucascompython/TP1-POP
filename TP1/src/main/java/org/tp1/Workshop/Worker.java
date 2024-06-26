package org.tp1.Workshop;

public final class Worker {
    public static int count = 0;
    private String name;
    private final int id;
    private Role role;
    private int age;
    private float salary;
    private final Contact contact;

    public Worker(String name, Role role, float salary, Contact contact, int age) {
        this.id = ++count;
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.contact = contact;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public float getSalary() {
        return salary;
    }

    public Contact getContact() {
        return contact;
    }

    public int getAge() {
        return age;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void print() {
        System.out.println("Worker [id=" + id + ", name=" + name + ", role=" + role + ", salary=" + salary
                + ", contact=" + contact + "]");
    }

}
