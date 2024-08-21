package be03.hornyak_levente.Factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeamLists {
    private List<String> csapatnevek = new ArrayList<>();         //-2
    private List<String> cimek = new ArrayList<>();               //-1
    private List<String> jatekNapok = new ArrayList<>();          //0
    private List<String> jatekIdopontok = new ArrayList<>();      //0
    private List<String> labdak = new ArrayList<>();              //2
    private List<String> emailek = new ArrayList<>();             //extra
    private List<String> telefonszamok = new ArrayList<>();       //extra

    public TeamLists(String sourceFile) {
        List<String> processedTxt = Processor.readCimjegyzek(sourceFile);

        Random rnd = new Random();

        for (int i = 0; i < processedTxt.size(); i++) {
            String oneLine = processedTxt.get(i);
            String regex = "[0-9]:[0-9]{2}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(oneLine);
            if (matcher.find()) {
                String csapatNev = processedTxt.get(i-2);
                // System.out.println(csapatNev);
                this.csapatnevek.add(csapatNev);

                String cim = processedTxt.get(i-1);
                // System.out.println(cim);
                this.cimek.add(cim);

                String jatekNap = processedTxt.get(i).split(" ")[0];
                this.jatekNapok.add(jatekNap);
                // System.out.println(jatekNap);

                String jatekIdopont = processedTxt.get(i).split(" ")[1];
                this.jatekIdopontok.add(jatekIdopont);
                // System.out.println(jatekIdopont);

                String labda = processedTxt.get(i+2);
                this.labdak.add(labda);
                // System.out.println(processedTxt.get(i+2));

                String email = csapatNev.replace(" ", "_").replace("-", "").replace(".", "-") + "@gmail.com";
                this.emailek.add(email);
                // System.out.println(email);

                String telefonszam = "06-" + rnd.nextInt(0, 10) + "0-";
                for (int j = 0; j < 7; j++) {
                    telefonszam += rnd.nextInt(0, 10);
                    if (j==2){
                        telefonszam += "-";
                    }
                }
                this.telefonszamok.add(telefonszam);
                // System.out.println(telefonszam + "\n");
            }
        }
    }

    public List<String> getCsapatnevekLista() {
        return csapatnevek;
    }

    public List<String> getCimekLista() {
        return cimek;
    }

    public List<String> getJatekNapok() {
        return jatekNapok;
    }

    public List<String> getJatekIdopontok() {
        return jatekIdopontok;
    }

    public List<String> getLabdak() {
        return labdak;
    }

    public List<String> getEmailek() {
        return emailek;
    }

    public List<String> getTelefonszamok() {
        return telefonszamok;
    }

    
}
