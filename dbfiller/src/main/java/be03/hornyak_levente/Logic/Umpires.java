package be03.hornyak_levente.Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import be03.hornyak_levente.DataBaseFiller.UmpireTable;
import be03.hornyak_levente.Factory.LogicQueries;
import be03.hornyak_levente.Factory.NameFactory;
import be03.hornyak_levente.Models.Umpire;

public class Umpires {
    private List<Umpire> umpireList;

    public Umpires() {
        this.umpireList = generate_umpireList();
        generate_emailAddresses();
        generate_phoneNumbers();
    }

    private List<Umpire> generate_umpireList(){
        Integer numberOfTeams = LogicQueries.count_numberOfTeams();
        List<Umpire> umpireList = new ArrayList<>();
        NameFactory nameFactory = new NameFactory("Vezetéknevek.txt", "Férfi keresztnevek.txt", "Női keresztnevek.txt");
        Random rnd = new Random();
        for (int i = 0; i < rnd.nextInt(numberOfTeams/2, numberOfTeams); i++) {
            String oneName = nameFactory.getSurnames().get(rnd.nextInt(nameFactory.getSurnames().size())) + " " + nameFactory.getFirstNamesMixed().get(rnd.nextInt(nameFactory.getFirstNamesMixed().size()));
            Umpire oneUmpire = new Umpire(oneName);
            umpireList.add(oneUmpire);
        }
        return umpireList;
    }

    private void generate_emailAddresses(){
        for (int i = 0; i < this.umpireList.size(); i++) {
            String oneEmail = this.umpireList.get(i).getUmpireName().replace(" ", ".") + "@gmail.com";
            this.umpireList.get(i).setEmailAddress(oneEmail);
        }
    }

    private void generate_phoneNumbers(){
        Random rnd = new Random();
        for (int i = 0; i < this.umpireList.size(); i++) {
            String onePhoneNumber = "06-1-XXX-XXXX";
            for (int j = 0; j < onePhoneNumber.length(); j++) {
                onePhoneNumber = onePhoneNumber.replaceFirst("X", String.valueOf(rnd.nextInt(10)));
            }
            this.umpireList.get(i).setPhoneNumber(onePhoneNumber);
        }
    }

    public List<Umpire> getUmpireList() {
        return umpireList;
    }

    

    
}
