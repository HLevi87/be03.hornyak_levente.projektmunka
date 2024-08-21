package be03.hornyak_levente.Factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Processor {
    public static List<String> readCimjegyzek(String path) {
        File file = new File(path);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file, "Windows-1250");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<String> raw_cimjegyzek = new ArrayList<>();

        while (scanner.hasNextLine()){
            raw_cimjegyzek.add(scanner.nextLine());
        }
        scanner.close();
        return raw_cimjegyzek;
    }
}
