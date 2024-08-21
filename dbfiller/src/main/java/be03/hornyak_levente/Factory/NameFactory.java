package be03.hornyak_levente.Factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class NameFactory {
    private List<String> surnames;
    private List<String> maleNames;
    private List<String> femaleNames;
    private List<String> firstNamesMixed;

    public NameFactory(String vezetknevek, String ferfiNevek, String noiNevek) {
        this.surnames = read_nameList(vezetknevek);
        this.maleNames = read_nameList(ferfiNevek);
        this.femaleNames = read_nameList(noiNevek);
        this.firstNamesMixed = mix_firstNames();
    }

    private List<String> read_nameList(String path){
        List<String> names = new ArrayList<>();
        try {
            File nameFile = new File(path);
            Scanner nameScanner = new Scanner(nameFile, "Windows-1250");
            while (nameScanner.hasNextLine()) {
                names.add(nameScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return names;
    }

    private List<String> mix_firstNames(){
        List<String> firstNamesMixed = new ArrayList<>();
        firstNamesMixed.addAll(maleNames);
        firstNamesMixed.addAll(femaleNames);
        return firstNamesMixed;
    }
    

    public void list_Names(){
        System.out.println("Vezetéknevek:");
        for (int i = 0; i < this.surnames.size(); i++) {
            System.out.println(this.surnames.get(i));
        }
    }

    public void count_occurrences(){
        int cnt = 1;
        boolean newName = true;
        for (int i = 1; i < this.surnames.size(); i++) {
            newName = true;
            for (int j = 0; j < i-1; j++) {
                if (surnames.get(i).equals(surnames.get(j))) {
                    newName = false;
                    break;
                }
            }
            if (newName) {
                cnt++;
            }
        }
        System.out.println("Vezetéknevek száma: " + cnt);

        cnt = 1;
        for (int i = 1; i < this.maleNames.size(); i++) {
            newName = true;
            for (int j = 0; j < i-1; j++) {
                if (maleNames.get(i).equals(maleNames.get(j))) {
                    newName = false;
                    break;
                }
            }
            if (newName) {
                cnt++;
            }
        }
        System.out.println("Férfinevek száma: " + cnt);

        cnt = 1;
        for (int i = 1; i < this.femaleNames.size(); i++) {
            newName = true;
            for (int j = 0; j < i-1; j++) {
                if (femaleNames.get(i).equals(femaleNames.get(j))) {
                    newName = false;
                    break;
                }
            }
            if (newName) {
                cnt++;
            }
        }
        System.out.println("Női nevek száma: " + cnt);

    }



    public List<String> getSurnames() {
        return surnames;
    }



    public List<String> getMaleNames() {
        return maleNames;
    }



    public List<String> getFemaleNames() {
        return femaleNames;
    }


    public List<String> getFirstNamesMixed() {
        return firstNamesMixed;
    }

}
