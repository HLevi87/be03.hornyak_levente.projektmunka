package be03.hornyak_levente.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import be03.hornyak_levente.Logic.AdminMenuLogic;
import be03.hornyak_levente.Logic.ContactPerson_MenuLogic;
import be03.hornyak_levente.Logic.MenuLogic;
import be03.hornyak_levente.Models.NameID;
import be03.hornyak_levente.Models.Player;
import be03.hornyak_levente.Models.Team;

public class AdminUI {
    private AdminMenuLogic adminLogic;
    private Scanner scanner;
    public AdminUI() {
        this.adminLogic = new AdminMenuLogic();
        this.scanner = new Scanner(System.in);
    }

    public void enter_adminLevel(){
        if (subMenu_FM3a_belepes()) {
            subMenu_FM2b_kapcsolattartoMenupontok();
        }
    }
    
    private Boolean subMenu_FM3a_belepes(){
        //Hármas számláló inicializálása
        int cnt = 3;
        while (cnt > 0) {
            System.out.println("INFORMÁCIÓK:\n\t- A felhasználónév és a jelszó nem tartalmazhat ékezetes betűket.\n\t- A felhasználónév és a jelszó csak betűket és számokat tartalmazhat.\n");
            //Felhasználónév bekérése a felhasználótól
            System.out.println("0 - MÉGSEM\nKérem adja meg a felhasználónevét (min. 4 kar.): ");
            String inputName = request_userInput();
            if (isInteger(inputName) && Integer.parseInt(inputName) == 0) {
                return false;
            } 
            System.out.println();
    
            //Jelszó bekérése a felhasználótól
            System.out.println("0 - MÉGSEM\nKérem adja meg a jelszót (min. 4 kar.): ");
            String inputPassword = request_userInput();
            if (isInteger(inputPassword) && Integer.parseInt(inputPassword) == 0) {
                return false;
            }

            if (this.adminLogic.check_credentials(inputName, inputPassword)) {
                System.out.println("\n\nÜdvözöljük!\nÖn rendszergazdaként lépett be!\n");
                return true;
            } else {
                cnt--;
                System.out.println("A megadott bejelentkezési adatok hibásak.\nHátralévő próbálkozások száma: " + cnt);
            }
        }
        return false;
    }
  
    private void subMenu_FM2b_kapcsolattartoMenupontok(){
        Boolean remainInLoop = true;
        while (remainInLoop) {
            display_FM3menu();
            Integer userInput = select_menuItem();
            //KAPCSOLATTARTÓI MENÜPONTOK
            switch (userInput) {
                case 1:{
                    //1 - CSAPAT KIVONÁSA
                    //Csapat kiválasztása listából
                    //Kapcsolattartási adatok törlése (email + telefon)
                    //Kapcsolattartó személy --> credentials sor törlése
                    //Csapat játékosainak törlése
                    //Csapat törlése
                    subMenu_FM31_csapatKivonas();
                    break;
                }
                case 2:{
                    //2 - CSAPAT HOZZÁADÁSA
                    //1) Csapat osztályszintű létrehozása a szükséges adatok alapján
                    //  b) ContactDetails, Ball, Location
                    //2) Legalább 4 játékos hozzáadása --> ugyanaz lesz, mint az FM34-es menü!!!
                    //  b) ContacPerson kijelölése, Credentials tábla kezelése
                    //Ha játékosokat nem adunk hozzá, a frissen hozzáadott csapatot is törölni kell!!! + a ContactDetails recordot !!!
                    subMenu_FM32_csapatHozzaadas();
                    break;
                }
                case 3:{
                    //3 - JÁTÉKOS KIVONÁSA
                    //Játékos kiválasztása listából
                    //HA contactPerson --> ÚJ ID megadása a csapatból (a csapatban játszó játékosok listájából választva)
                    //Játékos törlése
                    subMenu_FM33_jatekosKivonas();
                    break;
                }
                case 4:{
                    //4 - JÁTÉKOS HOZZÁADÁSA
                    //Ugyanaz, mint az FM3-2 második fele
                    subMenu_FM34_jatekosHozzaadas();
                    break;
                }
                case 5:{
                    //5 - JÁTÉKVEZETŐ KIVONÁSA
                    subMenu_FM35_jatekvezetoKivonas();
                    break;
                }
                case 6:{
                    subMenu_FM36_jatekvezetoHozzaadas();
                    //6 - JÁTÉKVEZETŐ HOZZÁADÁSA
                    break;
                }
                case 7:{
                    //7 - EGY MECCSEREDMÉNY FELÜLÍRÁSA
                    //Kiválasztás az alap "bajnokság állása / sorsolás" felépítés szerint
                    //Egyéni mérkőzés eredmények megadása --> ugyanaz, mint az FM21 (ContactPerson eredmények bevitele)
                    subMenu_FM37_eredmenyFeluliras();
                    break;
                }
                case 8:{
                    //8 - PONTLEVONÁS
                    //UPDATE Teams_xxxx_xxxx SET Points = (SELECT Points FROM Teams WHERE TeamID = ? AND IsActive = 1 AND SeasonID = ?) - 1
                    subMenu_FM38_pontlevonas();
                    break;
                }
                case 9:{
                    //9 - ÚJ SZEZON INDÍTÁSA
                    //CSAK AKKOR, ha minden mérközést lejátszottak
                    //Szezon hozzáadása a Seasons táblához
                    //Teams és Players táblák lemásolása és nevének átírása
                    //A SeasonID módosítása a Teams és Players táblákban
                    //A statiksztikák nullázása a Teams és Players táblákban
                    subMenu_FM39_ujSzezon();
                    break;
                }
                case 10:{
                    //10 - JELSZÓ MÓDOSÍTÁSA
                    //Ugyanaz, mint a ContactPersonUI-nál
                    subMenu_FM310_jelszoModositas();
                    break;
                }
                case 0:{
                    remainInLoop = false;
                    break;
                }
                default:
                    break;
            }
        }
    }

 
    private void subMenu_FM31_csapatKivonas(){
        String tableName = "Teams";
        String columnName_stringName = "TeamName";
        String columnName_intID = "TeamID";

        List<NameID> displayableList = this.adminLogic.get_simpleList(tableName, columnName_stringName, columnName_intID);
        Boolean remainInLoop = true;
        while (remainInLoop) {
            System.out.println("A csapat kiválasztása");
            Integer teamID_toDelete = displayANDchoose_fromList(displayableList);
            int listIdx = teamID_toDelete - 1;
            if (teamID_toDelete == 0) {
                remainInLoop = false;
            } else if (teamID_toDelete > 0 && teamID_toDelete <= displayableList.size()) {
                if (this.adminLogic.remove_team(teamID_toDelete)) {
                    System.out.println("A(z) "+ displayableList.get(listIdx).getName().toUpperCase() + " csapat és játékosai inaktiválva lettek.\nA kapcsolattartási adatok és a belépési adatok törlésre kerültek.");
                } else {
                    System.out.println("A csapat törlése során probléma merült fel.\n\nEGYES ADATOK ELVESZHETTEK AZ ADATBÁZISBÓL!!!\n");
                }
            } 
        }
    }

    private void subMenu_FM32_csapatHozzaadas(){
        //2 - CSAPAT HOZZÁADÁSA
            //PROGRAM LOGIKAI FELÉPÍTÉSE
                //ALAP ADATOK
                //4 JÁTÉKOS
                //1 KAPCSOLATTARTÓ
                //CREDENTIALS
                //BEÍRÁS DB-be

        //CSAPAT ADATAINAK BEKÉRÉSE
        Team teamSkeleton = request_teamData();

        //JÁTÉKOSOK ADATAINAK BEKÉRÉSE
        List<Player> playerList = new ArrayList<>();

        Boolean remainInLoop = true;
        while (remainInLoop) {
            Boolean remainInLoop_addPlayers = true;
            while (remainInLoop_addPlayers) {
                Player onePlayer = request_playerData();
                //Ellenőrzés, hogy meg lett-e már adva
                for (int i = 0; i < playerList.size(); i++) {
                    if (playerList.get(i).getPlayerName().equals(onePlayer.getPlayerName()) &&
                        playerList.get(i).getLicenceNumber().equals(onePlayer.getLicenceNumber())) {
                        System.out.println("Ezt a játékost már hozzáadta!");
                    } else{
                        playerList.add(onePlayer);
                    }
                }

                Boolean remainInLoop_confirm = true;
                while (remainInLoop_confirm) {
                    System.out.println("\n1 - ÚJ JÁTÉKOS HOZZÁADÁSA\n0 - NINCS TÖBB JÁTÉKOS");
                    Integer userInput_addPlayer = select_menuItem();
                    if (userInput_addPlayer == 0) {
                        remainInLoop_addPlayers = false;
                    } else if (userInput_addPlayer != 0 || userInput_addPlayer != 1) {
                        System.out.println("Nem értelmezhető adatbevitel. Próbálja újra!\n");
                    }
                }
            }

            if (playerList.size() < 4) {
                Boolean remainInLoop_confirm = true;
                while (remainInLoop_confirm) {
                    System.out.println("Egy új csapat beviteléhez legalább 4 játékos kell. Ha most kilép, azzal a csapat minden megadott adata elvész.");
                    System.out.println("1 - FOLYTATÁS\n0 - KILÉPÉS");
                    Integer userInput_continue = select_menuItem();
                    if (userInput_continue == 0) {
                        return;
                    } else if (userInput_continue == 1){
                        remainInLoop_confirm = false;
                    } else if (userInput_continue != 0 || userInput_continue != 1) {
                        System.out.println("Nem értelmezhető adatbevitel. Próbálja újra!\n");
                    } 
                }
            } else{
                remainInLoop = false;
            }
        }

        //KAPCSOLATTARTÓ KIVÁLASZTÁSA
        remainInLoop = true;
        while (remainInLoop) {
            System.out.println("A kapcsolattartó személy kiválasztása a játékosok közül:");
            for (int i = 0; i < playerList.size(); i++) {
                System.out.printf("\t%3d. - %s\n", i+1, playerList.get(i));
            }
            System.out.printf("\t%3d. - KILÉPÉS\n\n", 0);
            Integer userInput = select_menuItem();
            int playerIdx = userInput - 1;

            if (userInput == 0) {
                remainInLoop = false;
            } else if (userInput > 0 && userInput < playerList.size()) {
                teamSkeleton.setContactPerson(playerList.get(playerIdx));
            }
        }

        //BELÉPÉSI ADATOK
        String userName = "";
        String pw = "";
        remainInLoop = true;
        System.out.println("Belépési adatok megadása - javasoljuk, hogy jegyezze le őket.");
        while (remainInLoop) {
            System.out.println("Adjon meg egy felhasználónevet:");
            userName = request_userInput();
            System.out.println("Adjon meg egy jelszót:");
            pw = request_userInput();
        }
        
        //BEVITEL ADATBÁZISBA
        if (this.adminLogic.add_newTeam(teamSkeleton, playerList, userName, pw)) {
            System.out.println("A csapat a játékosokkal és minden csapathoz tartozó adattal együtt sikeresen bekerült az adatbázisba.");
        } else{
            System.out.println("A csapat felvétele az adatbázisba nem sikerült.");
        }
    }

    private void subMenu_FM33_jatekosKivonas(){

    }

    private void subMenu_FM34_jatekosHozzaadas(){

    }

    private void subMenu_FM35_jatekvezetoKivonas(){

    }

    private void subMenu_FM36_jatekvezetoHozzaadas(){

    }
 
    private void subMenu_FM37_eredmenyFeluliras(){

    }

    private void subMenu_FM38_pontlevonas(){
        List<Team> teamList = this.adminLogic.request_teamList_withPoints();
        Boolean remainInLoop = true;
        while (remainInLoop) {
            System.out.println("Pontlevonás csapattól\n");
            for (int i = 0; i < teamList.size(); i++) {
                System.out.printf("%3d - %s\n", i+1, teamList.get(i).getTeamName());
            }
            System.out.printf("%3d - %s\n\n", 0, "VISSZA");
            System.out.println("Válassza ki a csapatot: ");
            Integer userInput_teamID = select_menuItem();
            Integer teamIdx = userInput_teamID - 1;
            if (userInput_teamID == 0) {
                remainInLoop = false;
            } else if (userInput_teamID > 0 && userInput_teamID <= teamList.size()){
                System.out.println("Adja meg a levonandó pontok számát: ");
                Integer userInput_penalty = select_menuItem();

                System.out.println("A kiválasztott csapat:   " + teamList.get(teamIdx) + "Levonandó pont: " + userInput_penalty + "\n");
                System.out.println("Megerősíti?\n1 - IGEN\\0 - NEM");
                Integer userInput_confirm = select_menuItem();
                if (userInput_confirm == 1) {
                    if (this.adminLogic.substract_pointsFromTeam(userInput_penalty, userInput_teamID)) {
                        System.out.println("A pontlevonás sikeresen lefutott.");
                    }
                }
            } else {
                System.out.println("Nem értelmezhető adatbevitel. Próbálja újra!\n");
            }

        }
    }

    private void subMenu_FM39_ujSzezon(){

    }

    private void subMenu_FM310_jelszoModositas(){
        String tableName_forQuery = "Credentials";
        String columnName_ofStringDetail = "Password";
        String columnName_detailID = "ContactPersonID";

        System.out.println("A jelszó módosításához, kérem, adja meg a belépési adatait!");
        if (subMenu_FM3a_belepes()) {
            Boolean remainInLoop = true;
            while (remainInLoop) {
                System.out.printf("\t%3d. - ÚJ MEGADÁSA\n", 1);
                System.out.printf("\t%3d. - VISSZA\n\n", 0);

                //Felhasználó választ
                Integer userInput = select_menuItem();

                //Ha a userInput = 0 -->kilépés
                if (userInput == 0) {
                    remainInLoop = false;
                } else if (userInput == 1) {
                    //ÚJ jsz MEGADÁSA
                    boolean remainInLoop_newPW = true;
                    while (remainInLoop_newPW) {
                        System.out.println("JELSZÓ MÓDOSÍTÁSA\n");
                        System.out.println("INFORMÁCIÓK:\n\t- A felhasználónév és a jelszó nem tartalmazhat ékezetes betűket.\n\t- A felhasználónév és a jelszó csak betűket és számokat tartalmazhat.\n");

                        String[] twoPWs = new String[2];
                        System.out.println("A módosításhoz adja meg kétszer egymás után az ENTER-rel elválasztva.");
                        for (int i = 0; i < 2; i++) {
                            System.out.print("Az új jelszó: ");
                            twoPWs[i] = request_userInput();
                            System.out.println();
                        }

                        //Ellenőrzés, hogy a kettő egyezik-e
                        if (twoPWs[0].equals(twoPWs[1])) {
                            //Visszaírás és megerősítés kérése
                            System.out.println("A jelszó módosítva lesz.\nMegerősíti?");
                            System.out.println("1 - IGEN\n0 - NEM");
                            Integer userInput_confirm = select_menuItem();

                            //Új jelszó felvitele az adatbázisba
                            if (userInput_confirm == 1) {
                                remainInLoop_newPW = false;
                                remainInLoop = false;
                                if (this.adminLogic.update_password(tableName_forQuery, columnName_ofStringDetail, columnName_detailID, twoPWs[1])) {
                                    System.out.println("Adatfrissítés megtörtént.");
                                }
                            }
                        }           //IF - egyezőség ellenőrzése VÉGE
                        else{
                            System.out.println("A két jelszó nem egyezik. Kérem, próbálja újra!");
                        }
                    }           //Új pw megadása - while VÉGE
                } else{
                    System.out.println("Nem értelmezhető adatbevitel. Próbálja újra!\n");
                }
            }           //Main while
        }           //Beléptető if
    }           //Metódus vége

    //-------------------------------------------------------------------------------------------------------------
    //CSAPATHOZZÁADÁS ALMETÓDUSOK
    private Team request_teamData(){
        Team teamSkeleton = null;
        Boolean remainInLoop = true;
        while (remainInLoop) {
            System.out.println("Új csapat hozzáadása");
            System.out.println("Egy új csapat hozzáadásához az alábbiak pontos megadására lesz szükség:\n" +
                                    "Csapatnév\r\n" + //
                                    "A játék napja\r\n" + //
                                    "Kezdés időpontja\r\n" + //
                                    "Hazai pálya címe\r\n" + //
                                    "Email + telefonszám\r\n" + //
                                    "Labda típusa\r\n" +
                                    "Legalább 4 játékos adatai\r\n" +
                                    "1 kapcsolattartó játékos\n");
                
            //CSAPATNÉV
                System.out.println("A csapat neve: ");
            String teamName = request_userInput();
            
            //JÁTÉKNAP
                System.out.println("Játéknap:");
                List<NameID> daysOfWeek = create_gameDayList();
                Integer gameDayID = displayANDchoose_fromList(daysOfWeek);
                if (gameDayID == 0) {
                    break;
                }
                int gameDayIdx = gameDayID - 1;
            String gameDay = daysOfWeek.get(gameDayIdx).getName();

            //KEZDÉSI IDŐPONT
                System.out.println("Kezdés időpontja - formátum: óó:pp:");
            String gameTime = request_userInput();

            //HAZAI PÁLYA
                System.out.println("Hazai pálya: ");
                String location = "";
                List<NameID> locationList = this.adminLogic.get_simpleList("Locations", "Location", "LocationID");

                Integer userInput_locationID = 0;
                Integer addNew = locationList.size() + 1;
                Boolean remainInLoop_location = true;

                while (remainInLoop_location) {
                    System.out.println("Lista az adatbázisból");
                    for (int i = 0; i < locationList.size(); i++) {
                        System.out.printf("\t%3d. - %s\n", i+1, locationList.get(i));
                    }
                    System.out.printf("\t%3d. - ÚJ MEGADÁSA\n\n", addNew);
                    System.out.printf("\t%3d. - VISSZA\n\n", 0);
                    userInput_locationID = select_menuItem();
                    Integer userInput_locationIdx = userInput_locationID - 1;
                    if (userInput_locationID == 0) {
                        break;
                    } else if (userInput_locationID == addNew){
                        location = request_userInput();
                        remainInLoop_location = false;
                    } else if (userInput_locationID > 0 && userInput_locationID <= locationList.size()){
                        location = locationList.get(userInput_locationIdx).getName();
                        remainInLoop_location = false;
                    } else{
                        System.out.println("Nem értelmezhető bevitel. Próbálja újra!\n");
                    }
                }

            //EMAILCÍM
                System.out.println("Emailcím:");
            String email = request_userInput();

            //Telefonszám
                System.out.println("Telefonszám:");
            String phoneNumber = request_userInput();

            //LABDA
                System.out.println("Labda:");
            String ball = "";
                List<NameID> ballList = this.adminLogic.get_simpleList("Balls", "Ball", "BallID");

                Integer userInput_ballID = 0;
                Integer addNewBall = ballList.size() + 1;
                Boolean remainInLoop_ball = true;

                while (remainInLoop_ball) {
                    System.out.println("Lista az adatbázisból");
                    for (int i = 0; i < ballList.size(); i++) {
                        System.out.printf("\t%3d. - %s\n", i+1, ballList.get(i));
                    }
                    System.out.printf("\t%3d. - ÚJ MEGADÁSA\n\n", addNew);
                    System.out.printf("\t%3d. - VISSZA\n\n", 0);
                    userInput_ballID = select_menuItem();
                    Integer userInput_ballIdx = userInput_ballID - 1;       //a listához
                    if (userInput_ballID == 0) {
                        break;
                    } else if (userInput_ballID == addNewBall){
                        ball = request_userInput();
                        remainInLoop_ball = false;
                    } else if (userInput_ballID > 0 && userInput_ballID <= ballList.size()){
                        ball = ballList.get(userInput_ballIdx).getName();
                        remainInLoop_ball = false;
                    } else{
                        System.out.println("Nem értelmezhető bevitel. Próbálja újra!\n");
                    }
                }

            Integer seasonID = this.adminLogic.getSeasonID();

            //VISSZAÍRÁS - MEGERŐSÍTÉS
            System.out.println("Az eddig megadott adatok:");
            System.out.println(  "Csapatnév: " + teamName +
                                    "\nA játék napja: " + gameDay +
                                    "\nKezdés időpontja: " + gameTime +
                                    "\nHazai pálya címe: " + location +
                                    "\nEmail: " + email +
                                    "\nTelefonszám: " + phoneNumber +
                                    "\nLabda: " + ball);
            System.out.println("1 - MEGERŐSÍTÉS\n2 - ÚJRA\n0 - KILÉPÉS");
            Integer userInput_confirm = select_menuItem();
            if (userInput_confirm == 0) {
                remainInLoop = false;
            } else if (userInput_confirm == 1){
                teamSkeleton = new Team(teamName, location, gameDay, gameTime, ball, email, phoneNumber, seasonID);
            }
        }

        return teamSkeleton;
    }

    private Player request_playerData(){
        Player onePlayer = null;
        Boolean remainInLoop = true;
        while (remainInLoop) {
            System.out.println("Új játékos hozzáadása");
            System.out.println("Egy új játékos hozzáadásához az alábbiak pontos megadására lesz szükség:\n" +
                                    "Játékos neve\r\n" + //
                                    "A játékos engedélyszáma\r\n");
                
            //JÁTÉKOS NEVE
            System.out.println("A játékos neve: ");
            String playerName = request_userInput();
            
            //JÁTÉKOS ENGEDÉLYSZÁMA
            Boolean correctInput = false;
            Integer licenceNumber = null;
            while (!correctInput) {
            System.out.print("Engedélyszáma: ");
            licenceNumber = scan_integerInput();
            if (licenceNumber == null) {
                System.out.println("\nNem adott meg értéket.");
            } else{
                correctInput = true;
                System.out.println();
            }
        }

            //VISSZAÍRÁS - MEGERŐSÍTÉS
            System.out.println("Az eddig megadott adatok:");
            System.out.println(  "Játékos neve: " + playerName +
                                    "\n játékos engedélyszáma: " + licenceNumber);
            System.out.println("1 - MEGERŐSÍTÉS\n2 - ÚJRA\n0 - KILÉPÉS");
            Integer userInput_confirm = select_menuItem();
            if (userInput_confirm == 0) {
                remainInLoop = false;
            } else if (userInput_confirm == 1){
                onePlayer = new Player(playerName, licenceNumber);
            }
        }

        return onePlayer;
    }

    //-------------------------------------------------------------------------------------------------------------
    //MENÜK KIÍRATÁSA
    private void display_FM3menu(){
        String menuScript = "1 - CSAPAT KIVONÁSA\r\n" +
                            "2 - CSAPAT HOZZÁADÁSA\r\n" +
                            "3 - JÁTÉKOS KIVONÁSA\r\n" +
                            "4 - JÁTÉKOS HOZZÁADÁSA\r\n" +
                            "5 - JÁTÉKVEZETŐ KIVONÁSA\r\n" +
                            "6 - JÁTÉKVEZETŐ HOZZÁADÁSA\r\n" +
                            "7 - EGY MECCSEREDMÉNY FELÜLÍRÁSA\r\n" +
                            "8 - PONTLEVONÁS\r\n" +
                            "9 - ÚJ SZEZON INDÍTÁSA\r\n" +
                            "10 - JELSZÓ MÓDOSÍTÁSA\r\n" +
                            "0 - KILÉPÉS";
        System.out.println(menuScript);                                
    }

    //"addNew" nélküli választás listáról
    private Integer displayANDchoose_fromList(List<NameID> displayableList){
        Integer userInput = 0;
        Boolean remainInLoop = true;
        while (remainInLoop) {
            System.out.println("Lista az adatbázisból");
            for (int i = 0; i < displayableList.size(); i++) {
                System.out.printf("\t%3d. - %s\n", i+1, displayableList.get(i));
            }
            System.out.printf("\t%3d. - VISSZA\n\n", 0);
            userInput = select_menuItem();
    
            if (userInput < 0 || userInput > displayableList.size()) {
                System.out.println("Nem értelmezhető bevitel. Próbálja újra!\n");
            } else {
                remainInLoop = false;
            }
        }

        return userInput;
    }

    
    //-------------------------------------------------------------------------------------------------------------
    //SEGÉD METÓDUSOK

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

    private String request_userInput(){
        Boolean correctInput = false;
        String userInput = "";
        while (!correctInput) {
            userInput = scanner.nextLine();
            if (userInput.length() > 0) {
                correctInput = true;
            }
        }
        return userInput;
    }

    private List<NameID> create_gameDayList(){
        List<NameID> gameDays = new ArrayList<>();
        gameDays.add(new NameID(1, "Hétfő"));
        gameDays.add(new NameID(2, "Kedd"));
        gameDays.add(new NameID(3, "Szerda"));
        gameDays.add(new NameID(4, "Csütörtök"));
        gameDays.add(new NameID(5, "Péntek"));
        
        return gameDays;
    }

}
