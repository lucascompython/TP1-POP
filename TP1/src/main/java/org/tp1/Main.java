package org.tp1;

import org.tp1.Menu.MainMenu;
import org.tp1.Workshop.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson2.JSON;

public final class Main {
    public static void main(String[] args) {

        List<Worker> workers = JsonUtils.readWorkers();
        List<Client> clients = JsonUtils.readClients();
        List<Repair> repairs = JsonUtils.readRepairs();
        new MainMenu(workers, clients, repairs);

    }
}