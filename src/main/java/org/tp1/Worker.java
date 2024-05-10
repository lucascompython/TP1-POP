package org.tp1;

public class Worker {
    public static int count = 0;
    private final String name;
    private final int id;
    private final Role role;
    private float salary;
    private final Contact contact;


    public Worker(String name, Role role, float salary, Contact contact) {
        this.id = ++count;
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.contact = contact;
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

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public void print() {
//        System.out.println("Worker [id=" + id + ", name=" + name + ", role=" + role + ", salary=" + salary + "]");
        System.out.println("Worker [id=" + id + ", name=" + name + ", role=" + role + ", salary=" + salary + ", contact=" + contact + "]");
    }

}
