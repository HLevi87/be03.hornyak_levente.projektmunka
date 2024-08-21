package be03.hornyak_levente.UI;

import java.io.File;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import be03.hornyak_levente.Logic.MenuLogic;
import be03.hornyak_levente.Models.IndividualMatch;
import be03.hornyak_levente.Models.Player;
import be03.hornyak_levente.Models.Round;
import be03.hornyak_levente.Models.Team;
import be03.hornyak_levente.Models.TeamMatch;

public class MainMenuUI {
    private MenuLogic menuLogic;
    private Scanner scanner;
    private Integer seasonID;

    public MainMenuUI() {
        this.menuLogic = new MenuLogic();
        this.scanner = new Scanner(System.in);
    }

    //--------------------------------------------------------------------------------------------------
    //Menü és subMenü KEZELŐ METÓDUSOK
    //Főmenü
    public void start_menu(){
        Boolean remainInLoop = true;
        while (remainInLoop) {
            display_mainMenu();
            Integer userInput = select_menuItem();
            switch (userInput) {
                case 1:{
                    //Bajnokság állása
                    subMenu_FM1_bajnoksagAllasa();
                    break;
                }
                case 2: {
                    //Bejelentkezés kapcsolattartóként
                    ContactPersonUIs contactPersonUI = new ContactPersonUIs();
                    contactPersonUI.enter_contactPersonLevel();
                    break;
                }
                case 3: {
                    //Bejelentkezés adminként
                    AdminUI adminUI = new AdminUI();
                    adminUI.enter_adminLevel();
                    break;
                }
                case 0: {
                    remainInLoop = false;
                    break;
                }
                
            
                default:
                    throw new WrongMenuItem("\n\nA(z) " + userInput + " nincs menüponthoz rendelve. Indítsa újra!\n");
            }
        }
    }

    //FM1 = Bajnokság Állása
    private void subMenu_FM1_bajnoksagAllasa(){
        List<String> seasonList = this.menuLogic.request_seasonList();
        Boolean remainInLoop = true;
        while (remainInLoop) {
            boolean correctSeason = false;
            while (!correctSeason) {
                //Eddigi szezonok kirítása
                System.out.println("Eddig szezonok:");
                for (int i = 0; i < seasonList.size(); i++) {
                    System.out.println(i+1 + " - " + seasonList.get(i));
                }
                //Szezon USER általi kiválasztása
                this.seasonID = select_menuItem();
                if (this.seasonID > 0 && this.seasonID <= seasonList.size()) {
                    correctSeason = true;
                    this.menuLogic.setSeasonID(this.seasonID);
                } 
            }

            display_FM1_bajnoksagAllasa();
            Integer userInput = select_menuItem();
            //A BAJNOKSÁG ÁLLÁSA
            switch (userInput) {
                case 1:{
                    //1 - SORSOLÁS
                    subMenu_FM11a_fordulok();
                    break;
                }
                case 2: {
                    //2 - AKTUÁLIS FORDULÓ
                    break;
                }
                case 3: {
                    //3 - CSAPATTABELLA
                    subMenu_FM13_csapatTabella();
                    break;
                }
                case 4: {
                    //4 - JÁTÉKOSTABELLA
                    subMenu_FM14_jatekosTabella();
                    break;
                }
                case 5: {
                    //5 - EGY CSAPAT JÁTÉKOSAI
                    subMenu_FM15_egyCsapatJatekosai();
                    break;
                }
                case 6: {
                    //6 - EGY CSAPAT ADATAI
                    subMenu_FM16_egyCsapatAdatai();
                    break;
                }
                case 0: {
                    //0 - VISSZA
                    remainInLoop = false;
                    break;
                }
            }
        }
    }

    //FM1-1a = SORSOLÁS
    private void subMenu_FM11a_fordulok(){
        Boolean remainInLoop = true;
        while (remainInLoop) {
            //Lekérdezés DB-ből, ld. Round modell
            List<Round> roundList = this.menuLogic.request_roundList(seasonID);

            //Kilistázás fordulónként
            for (int i = 0; i < roundList.size(); i++) {
                System.out.println(i+1 + ". FORDULÓ");
                for (int j = 0; j < roundList.get(i).getTeamMatches().size(); j++) {
                    TeamMatch oneTeamMatch = roundList.get(i).getTeamMatches().get(j);
                    if (oneTeamMatch.getIsOver()) {
                        System.out.printf("\t\t%-40s %-4s\t\t%-40s\n", oneTeamMatch.getHomeTeam(), oneTeamMatch.getResult(), oneTeamMatch.getGuestTeam());
                    } else{
                        System.out.printf("\t\t%-40s %-40s\n", oneTeamMatch.getHomeTeam(), oneTeamMatch.getGuestTeam());
                    }
                }
                System.out.println();
            }

            //1 db forduló kiválasztása
            System.out.println("\n0 - KILÉPÉS");
            Integer userinput_fordulo = select_menuItem();
            int roundIdx = userinput_fordulo - 1;

            //0 = visszalépés a menüben
            if (userinput_fordulo == 0) {
                remainInLoop = false;
            } else if(userinput_fordulo <= roundList.size() && userinput_fordulo > 0){
                //Sorba rendezés
                List<TeamMatch> requestedList = roundList.get(roundIdx).getTeamMatches();
                List<TeamMatch> teamMatches_inOrder = this.menuLogic.orderTeamMatches_byDayTime(requestedList);
                subMenu_FM11b_csapatMeccsek(teamMatches_inOrder);
            } else{
                System.out.println("A megadott szám nem értelmezhető.");
            }
        }
    }

    private void subMenu_FM11b_csapatMeccsek(List<TeamMatch> teamMatchList){
        Boolean remainInLoop2 = true;
        while (remainInLoop2) {
            //Forduló meccseinek részletesebb kilistázása
            System.out.println(teamMatchList.get(0).getRoundNumber() + ". FORDULÓ MÉRKŐZÉSEI");
            //Kilistázás
            for (int i = 0; i < teamMatchList.size(); i++) {
                TeamMatch oneTeamMatch = teamMatchList.get(i);
                System.out.println(i + 1 + ". " + oneTeamMatch.getHomeTeam().toUpperCase() + "    --    " + oneTeamMatch.getGuestTeam().toUpperCase());
                System.out.println("\t\t\tNap / időpont: " + oneTeamMatch.getGameDay() + " / " + oneTeamMatch.getGameTime());
                System.out.println("\t\t\tHelyszín: " + oneTeamMatch.getLocation());
                System.out.println("\t\t\tJátékvezető: " + oneTeamMatch.getUmpire());
                System.out.println("\t\t\tLabda: " + oneTeamMatch.getBall());
                if (oneTeamMatch.getIsOver()) {
                    System.out.println("Végeredmény: " + oneTeamMatch.getResult() + "\n");
                }
            }
            //1 db csapatMeccs kiválasztása
            System.out.println("\n0 - KILÉPÉS");
            Integer userinput_csapatMeccs = select_menuItem();
            if (userinput_csapatMeccs == 0) {
                remainInLoop2 = false;
            } else if (userinput_csapatMeccs <= teamMatchList.size() && userinput_csapatMeccs > 0){
                int teamMatchIdx = userinput_csapatMeccs - 1;
                TeamMatch requestedTeamMatch = teamMatchList.get(teamMatchIdx);
            
                //0 = visszalépés a menüben
                if(requestedTeamMatch.getIsOver()){
                    //Adott csapatmeccs egyéni meccseinek kiíratása
                    subMenu_FM11c_egyeniMeccsek(requestedTeamMatch);
                } else{
                    System.out.println("Ezt a csapatmérkőzést még nem játszották le.");
        
                }
            } else{
                System.out.println("A megadott szám nem értelmezhető.");
            }
        }
    }

    private void subMenu_FM11c_egyeniMeccsek(TeamMatch teamMatch){
        //Egy kiválasztott csapatmeccs egyéni meccseinek kiíratása
        Boolean remainInLoop3 = true;
        //A csapatmeccsek beolvasása DB-ből
        Integer teamMatchID = teamMatch.getTeamMatchID();
        List<IndividualMatch> individualMatches = this.menuLogic.request_individualMatchList(teamMatchID);
        while (remainInLoop3) {
            System.out.println(teamMatch.getHomeTeam().toUpperCase() + " -- " + teamMatch.getGuestTeam().toUpperCase() + "\n\n");
            System.out.printf("\t\t %-22s -\t%-22s %s\n\n", "Hazai játékos", "Vendég játékos", "Eredmény");;
            for (IndividualMatch individualMatch : individualMatches) {
                System.out.printf("\t\t %-22s -\t%-22s %s\n", individualMatch.getHomePlayer(), individualMatch.getGuestPlayer(), individualMatch.getResult());
            }
            System.out.println("\nVégeredmény: " + teamMatch.getResult().toString());

            //Várakozás a 0 megnyomására
            System.out.println("0 - VISSZA");
            System.out.println("1 - NYOMTATÁS FÁJLBA");
            Integer userSelect = select_menuItem();
            if (userSelect == 1) {
                if (printTeamMatch_toFile(teamMatch)) {
                    System.out.println("A fájl elkészült. A fájl neve: " + teamMatch.getHomeTeam().split(" ")[0] +
                    "_" + teamMatch.getGuestTeam().split(" ")[0] + "_fordulo" + teamMatch.getRoundNumber() + "\n");
                }
            } else if (userSelect == 0) {
                remainInLoop3 = false;
            }
        }
    }

    public Boolean printTeamMatch_toFile(TeamMatch teamMatch){
        String fileName = teamMatch.getHomeTeam().split(" ")[0] + "_" + teamMatch.getGuestTeam().split(" ")[0] + "_fordulo" + teamMatch.getRoundNumber();
        //Fájl létrehozása
        try {
            File file = new File(fileName);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                //A csapatmeccsek beolvasása DB-ből
                Integer teamMatchID = teamMatch.getTeamMatchID();
                List<IndividualMatch> individualMatches = this.menuLogic.request_individualMatchList(teamMatchID);

                //Fájlba írás
                PrintWriter printWriter = new PrintWriter(file);
                printWriter.println("Table Tennis Manager by Levente Hornyák, " + LocalDate.now() + "\n\n");
                printWriter.println("Csapatmeccseredmény:");
                printWriter.println(teamMatch.getHomeTeam().toUpperCase() + " -- " + teamMatch.getGuestTeam().toUpperCase() + "\n\n");
                printWriter.printf("\t\t %-22s -\t%-22s %s\n\n", "Hazai játékos", "Vendég játékos", "Eredmény");
                for (IndividualMatch individualMatch : individualMatches) {
                    printWriter.printf("\t\t %-22s -\t%-22s %s\n", individualMatch.getHomePlayer(), individualMatch.getGuestPlayer(), individualMatch.getResult());
                }
                printWriter.println("\nVégeredmény: " + teamMatch.getResult().toString());
                printWriter.close();

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //CSAPATTABELLA
    private void subMenu_FM13_csapatTabella(){
        List<Team> teamList = this.menuLogic.request_teamList_forLeagueTable(this.seasonID);

        Boolean remainInLoop = true;
        while (remainInLoop) {
            System.out.println("\t\t\tA BAJNOKSÁG ÁLLÁSA\n");
            System.out.printf("%3s. | %-40s | %-3s    | %-8s    | %-9s    | %-8s    | %-8s\n",
                                "#",     //Helyezés
                                        "Csapatnév",
                                        "Mérkőzés",
                                        "Győzelem",
                                        "Döntetlen",
                                        "Vereség",
                                        "Pont"); 
            for (int i = 0; i < teamList.size(); i++) {
                System.out.printf("%3d. | %-40s |   %-9d |   %-9d |   %-10d |   %-9d |   %-8d\n", 
                                i+1,     //Helyezés
                                        teamList.get(i).getTeamName(),
                                        teamList.get(i).getMatchesPlayed(),
                                        teamList.get(i).getMatchesWon(),
                                        teamList.get(i).getMatchesDraw(),
                                        teamList.get(i).getMatchesLost(),
                                        teamList.get(i).getPoints());
            }
            System.out.println();
            //Várakozás a 0 megnyomására
            System.out.println("0 - VISSZA");
            Integer waitFor0 = select_menuItem();
            if (waitFor0 == 0) {
                remainInLoop = false;
            }
        }
    }

    //JÁTÉKOSTABELLA
    private void subMenu_FM14_jatekosTabella(){
        List<Player> playerList = this.menuLogic.request_playerList(this.seasonID);
        Boolean remainInLoop = true;
        while (remainInLoop) {
            System.out.println("\t\t\tJÁTÉKOSRANGLISTA\n");
            printOut_players(playerList);

            //Várakozás a 0 megnyomására
            System.out.println("0 - VISSZA");
            Integer waitFor0 = select_menuItem();
            if (waitFor0 == 0) {
                remainInLoop = false;
            }
        }
    }
    
    //EGY CSAPAT (KÉT METÓDUS)
    private void subMenu_FM15_egyCsapatJatekosai(){
        List<Team> teamList = this.menuLogic.request_teamList_forLeagueTable(seasonID);
        Boolean remainInLoop = true;
        while (remainInLoop) {
            //Csapatot kiírató metódus
            Integer userInput = listTeams_thenSelect(teamList);
            Integer teamIdx = userInput - 1;
            if (userInput == 0) {
                remainInLoop = false;
                //Kiválasztott csapat játékosainak kiíratása
            } else if (userInput <= teamList.size() && userInput > 0){
                //A csapat kiválasztása a listából index alapján
                Team requestedTeam = teamList.get(teamIdx);
                //Összes játékos és a csapat játékosainak listái
                List<Player> allPlayerList = this.menuLogic.request_playerList(seasonID);
                List<Player> teamPlayerList = new ArrayList<>();
                for (Player player : allPlayerList) {
                    if (player.getTeamID().equals(requestedTeam.getTeamID())) {
                        teamPlayerList.add(player);
                    }
                }
                //A csapat játékosainak kiíratása
                System.out.printf("A(Z) %S JÁTÉKOSAI\n", requestedTeam.getTeamName());
                printOut_players(teamPlayerList);

                //Várakozás a 0 megnyomására
                System.out.println("0 - VISSZA");
                Integer waitFor0 = select_menuItem();
                if (waitFor0 == 0) {
                    remainInLoop = false;
                }
            }
        }
    }
    
    private void subMenu_FM16_egyCsapatAdatai(){
        try {
            List<Team> teamList = this.menuLogic.request_teamList_forTeamDetails(seasonID);
            Boolean remainInLoop = true;
            while (remainInLoop) {
                Integer userInput = listTeams_thenSelect(teamList);
                Integer teamIdx = userInput - 1;
                if (userInput == 0) {
                    remainInLoop = false;
                } else if (userInput <= teamList.size() && userInput > 0){
                    //A csapat kiválasztása a listából index alapján
                    Team requestedTeam = teamList.get(teamIdx);
                    
                    System.out.println(requestedTeam.getTeamName().toUpperCase());
                    System.out.printf("\t%-20s   %s\n" +
                                             "\t%-20s   %s\n" +
                                             "\t%-20s   %s\n" +
                                             "\t%-20s   %s\n" +
                                             "\t%-20s   %s\n" +
                                             "\t%-20s   %s\n" +
                                             "\t%-20s   %s\n", 
                                            "Hazai pálya:", requestedTeam.getLocation(),
                                                    "Játéknap:", requestedTeam.getGameDay(),
                                                    "Kezdési időpont:", requestedTeam.getGameTime(),
                                                    "Labda:", requestedTeam.getBall(),
                                                    "Emailcím:", requestedTeam.getEmail(),
                                                    "Telefonszám:", requestedTeam.getPhoneNumber(),
                                                    "Kapcsolattartó neve:", requestedTeam.getContactPersonName());

                    //Várakozás a 0 megnyomására
                    System.out.println("\n0 - VISSZA");
                    Integer waitFor0 = select_menuItem();
                    if (waitFor0 == 0) {
                        remainInLoop = false;
                    }
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer listTeams_thenSelect(List<Team> teamList){
        System.out.println("\tCSAPATOK\n");
        for (int i = 0; i < teamList.size(); i++) {
            System.out.println(i+1 + ".\t" + teamList.get(i).getTeamName());
        }
        System.out.println("0 - VISSZA");
        System.out.print("\nVálasztás: ");
        return select_menuItem();
        
    }

    private void printOut_players(List<Player> playerList){
        System.out.printf("%3s. | %-20s | %-8s    | %-40s    | %-8s | %-8s | %-6s\n",
                                    "#",     //Helyezés
                                            "Név",
                                            "Engedély",
                                            "Csapat",
                                            "Mérkőzés",
                                            "Győzelem",
                                            "Arány"); 
        int rank = 1;
        for (Player player : playerList) {

            System.out.printf("%3d. | %-20s | %8d    | %-40s    |   %-6d |   %-6d |%6.2f%%\n",
                                    rank,
                                    player.getPlayerName(),
                                    player.getLicenceNumber(),
                                    player.getTeamName(),
                                    player.getMatchesPlayed(),
                                    player.getMatchesWon(),
                                    player.getPercentage());
            rank++;
        }
        System.out.println();
    }
    
    //--------------------------------------------------------------------------------------------------
    //MENÜ SZÖVEGEKET KIÍRATÓ METÓDUSOK

    //FM = FŐMENÜ
    private void display_mainMenu(){
        String menuScript = "    FŐMENU\r\n" + 
                            "1 - A BAJNOKSÁG ÁLLÁSA\r\n" +
                            "2 - BEJELENTKEZÉS KAPCSOLATTARTÓKÉNT\r\n" +
                            "3 - BEJELENTKEZÉS ADMINKÉNT\r\n" +
                            "0 - KILÉPÉS";
        System.out.println(menuScript);                                
    }
    //FM-1 = A BAJNOKSÁG ÁLLÁSA
    private void display_FM1_bajnoksagAllasa(){
        String menuScript = "    A BAJNOKSÁG ÁLLÁSA\r\n" +
                            "1 - SORSOLÁS\n" +
                            "2 - AKTUÁLIS FORDULÓ\r\n" +
                            "3 - CSAPATTABELLA\r\n" +
                            "4 - JÁTÉKOSTABELLA\r\n" +
                            "5 - EGY CSAPAT JÁTÉKOSAI\r\n" +
                            "6 - EGY CSAPAT ADATAI\r\n" +
                            "0 - VISSZA";
        System.out.println(menuScript);
    }


    //--------------------------------------------------------------------------------------------------
    //INPUT KEZELŐ METÓDUSOK
    private Integer select_menuItem(){
        Boolean correctInput = false;
        Integer selectedItem = null;
        while (!correctInput) {
            System.out.print("Válasszon: ");
            selectedItem = scan_integerInput();
            if (selectedItem == null) {
                System.out.println("\nNem adott meg értéket.");
            } else{
                correctInput = true;
                System.out.println();
            }
        }
        return selectedItem;
    }

    private String scan_stringInput(){
        String userInput = this.scanner.nextLine();
        Boolean correctInput = false;
        while (!correctInput) {
            if (userInput.length() > 0) {
                correctInput = true;
            }
        }
        return userInput;
    }

    private Integer scan_integerInput(){
        Integer intUserInput = null;
        String stringUserInput = this.scanner.nextLine();
        if (isInteger(stringUserInput)) {
            intUserInput = Integer.parseInt(stringUserInput);
        }
        return intUserInput;
    }

    private Boolean isInteger(String userInput){
        try {
            Integer.parseInt(userInput);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //--------------------------------------------------------------------------------------------------
    //EGYÉB SEGÉD METÓDUSOK
    

}
