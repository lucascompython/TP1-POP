package org.tp1;

import com.alibaba.fastjson2.JSON;
import org.tp1.Workshop.Client;
import org.tp1.Workshop.Repair;
import org.tp1.Workshop.Worker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

    private static <T> List<T> readJsonFile(Class<T> clazz, String filename) {
        Path path = Paths.get(filename);
        try {
            String json = Files.readString(path);
            var list = JSON.parseArray(json, clazz);
            if (list == null) {
                list = new ArrayList<T>();
            }
            return list;
        } catch (NoSuchFileException e) {
            try {
                Files.createFile(path);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

    public static List<Client> readClients() {
        return readJsonFile(Client.class, "clients.json");
    }

    public static List<Worker> readWorkers() {
        return readJsonFile(Worker.class, "workers.json");
    }

    public static List<Repair> readRepairs() {
        return readJsonFile(Repair.class, "repairs.json");
    }

    private static void writeJsonFile(List<?> list, String filename) {
        String json = JSON.toJSONString(list);
        try {
            Files.writeString(Paths.get(filename), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeClients(List<Client> clients) {
        writeJsonFile(clients, "clients.json");
    }

    public static void writeWorkers(List<Worker> workers) {
        writeJsonFile(workers, "workers.json");
    }

    public static void writeRepairs(List<Repair> repairs) {
        writeJsonFile(repairs, "repairs.json");
    }

}
