package be03.hornyak_levente.UI;

import java.util.List;
import java.util.Scanner;

import be03.hornyak_levente.Logic.ContactPerson_MenuLogic;

public class ContactPersonUIs {
    private ContactPerson_MenuLogic higherMenuLogic;
    private Scanner scanner;

    public ContactPersonUIs() {
        this.higherMenuLogic = new ContactPerson_MenuLogic(1);
        this.scanner = new Scanner(System.in);
    }

    public void enter_contactPersonLevel(){
            if (subMenu_FM2a_belepes()) {
                subMenu_FM2b_kapcsolattartoMenupontok();
            }
    }

    private Boolean subMenu_FM2a_belepes(){
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

            if (this.higherMenuLogic.check_credentials(inputName, inputPassword)) {
                System.out.println("\n\nÜdvözöljük " + this.higherMenuLogic.fetch_name() + "!\n");
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
            display_FM2menu();
            Integer userInput = select_menuItem();
            //KAPCSOLATTARTÓI MENÜPONTOK
            switch (userInput) {
                case 1:{
                    //1 - EREDMÉNYEK BEVITELE
                    //Aktuális, még le nem játszott csapatmeccs megkeresése -- csak ezt lehet megadni
                    //ESETLEGES KÉSŐBBI FUNKCIÓ: "halasztás kérés" = átugrás és a következő meccs kitöltése, amit a program magától megkeres
                    //Egyéni meccsek eredményei
                    //Csapatmeccs eredménye
                    //UPDATE Fixture SET IsOver = 1
                    break;
                }
                case 2:{
                    //2 - HAZAI PÁLYA MÓDOSÍTÁSA
                    subMenu_FM22_palyaModositas();
                    break;
                }
                case 3:{
                    //3 - LABDA MÓDOSÍTÁSA
                    subMenu_FM23_labdaModositas();
                    break;
                }
                case 4:{
                    //4 - TELEFONSZÁM MÓDOSÍTÁSA
                    subMenu_FM24_telefonModositas();
                    break;
                }
                case 5:{
                    //5 - EMAILCÍM MÓDOSÍTÁSA
                    subMenu_FM25_emailModositas();
                    break;
                }
                case 6:{
                    //6 - JELSZÓ MÓDOSÍTÁSA
                    subMenu_FM26_jelszoModositas();
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

    private void subMenu_FM22_palyaModositas(){
        String tableName_forQuery = "Locations";
        String columnName_ofStringDetail = "Location";
        String columnName_detailID = "LocationID";

        System.out.println("A hazai pálya adatbázisban szereplő jelenlegi címe:\n" + this.higherMenuLogic.
                            get_currentDetail(tableName_forQuery, columnName_ofStringDetail, columnName_detailID) + "\n");
        List<String> locationList = this.higherMenuLogic.request_teamDetailList(tableName_forQuery, columnName_ofStringDetail, columnName_detailID);
        Integer addNewIdx = locationList.size() + 1;
        Boolean remainInLoop = true;
        System.out.println("A módosításhoz választhat az adatbázisban szereplő címek közül, vagy megadhat egy új címet.\n");
        while (remainInLoop) {
            System.out.println("Az adatbázisban szereplő címek:\n");
            for (int i = 0; i < locationList.size(); i++) {
                System.out.printf("\t%3d. - %s\n", i+1, locationList.get(i));
            }
            System.out.printf("\t%3d. - ÚJ MEGADÁSA\n", addNewIdx);
            System.out.printf("\t%3d. - VISSZA\n\n", 0);

            //Felhasználó választ
            Integer userInput_newLocID = select_menuItem();
            int locationIdx = userInput_newLocID - 1;

            //Ha a userInput = 0 -->kilépés
            if (userInput_newLocID == 0) {
                remainInLoop = false;
            } else if (userInput_newLocID == addNewIdx) {
                //ÚJ MEGADÁSA
                boolean remainInLoop_newLoc = true;
                while (remainInLoop_newLoc) {
                    System.out.println("Új helyszín megadása a csapat hazai pályájaként\n");
                    System.out.print("Kérem, írja be helyesen az új címet: ");
                    String newLocation_string = request_userInput();
                    System.out.println();
    
                    //Visszaírás és megerősítés kérése
                    System.out.println("Az ön által beírt cím: " + newLocation_string + "\nMegerősíti?");
                    System.out.println("1 - IGEN\n0 - NEM");
                    Integer userInput_confirm = select_menuItem();
                    if (userInput_confirm == 1) {
                        remainInLoop_newLoc = false;
                        remainInLoop = false;
                        if (this.higherMenuLogic.manage_newDetailUpdate(tableName_forQuery, columnName_detailID, newLocation_string)) {
                            System.out.println("Adatfrissítés megtörtént.");
                        }
                    }
                }
            } else if (userInput_newLocID > 0 && userInput_newLocID < addNewIdx) {
                //KIVÁLASZTÁS DB-ből
                System.out.println("A kiválasztott új helyszín: " + locationList.get(locationIdx) + "\nMegerősíti?");
                System.out.println("1 - IGEN\n0 - NEM");
                Integer userInput_confirm = select_menuItem();
                if (userInput_confirm == 1) {
                    remainInLoop = false;
                    try {
                        if (this.higherMenuLogic.update_newID(tableName_forQuery, columnName_detailID, userInput_newLocID)) {
                            System.out.println("Adatfrissítés megtörtént.");
                        } else {
                            System.out.println("Az adatfrissítés sikertelen.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else{
                System.out.println("Nem értelmezhető adatbevitel. Próbálja újra!\n");
            }
        }
    }

    private void subMenu_FM23_labdaModositas(){
        String tableName_forQuery = "Balls";
        String columnName_ofStringDetail = "Ball";
        String columnName_detailID = "BallID";

        System.out.println("Az adatbázisban szereplő jelenlegi labda:\n" + this.higherMenuLogic.
                            get_currentDetail(tableName_forQuery, columnName_ofStringDetail, columnName_detailID) + "\n");
        List<String> ballList = this.higherMenuLogic.request_teamDetailList(tableName_forQuery, columnName_ofStringDetail, columnName_detailID);
        Integer addNewIdx = ballList.size() + 1;
        Boolean remainInLoop = true;
        System.out.println("A módosításhoz választhat az adatbázisban szereplő labdák közül, vagy megadhat egy új labdát.\n");
        while (remainInLoop) {
            System.out.println("Az adatbázisban szereplő labdák:\n");
            for (int i = 0; i < ballList.size(); i++) {
                System.out.printf("\t%3d. - %s\n", i+1, ballList.get(i));
            }
            System.out.printf("\t%3d. - ÚJ MEGADÁSA\n", addNewIdx);
            System.out.printf("\t%3d. - VISSZA\n\n", 0);

            //Felhasználó választ
            Integer userInput_newBallID = select_menuItem();
            int ballIdx = userInput_newBallID - 1;

            //Ha a userInput = 0 -->kilépés
            if (userInput_newBallID == 0) {
                remainInLoop = false;
            } else if (userInput_newBallID == addNewIdx) {
                //ÚJ MEGADÁSA
                boolean remainInLoop_newBall = true;
                while (remainInLoop_newBall) {
                    System.out.println("Új labda megadása\n");
                    System.out.print("Kérem, írja be helyesen az új labda típusát: ");
                    String newBall_string = request_userInput();
                    System.out.println();
    
                    //Visszaírás és megerősítés kérése
                    System.out.println("Az ön által beírt labdatípus: " + newBall_string + "\nMegerősíti?");
                    System.out.println("1 - IGEN\n0 - NEM");
                    Integer userInput_confirm = select_menuItem();
                    //Új labda felvitele az adatbázisba
                    if (userInput_confirm == 1) {
                        remainInLoop_newBall = false;
                        remainInLoop = false;
                        if (this.higherMenuLogic.manage_newDetailUpdate(tableName_forQuery, columnName_detailID, newBall_string)) {
                            System.out.println("Adatfrissítés megtörtént.");
                        }
                    }
                }
            } else if (userInput_newBallID > 0 && userInput_newBallID < addNewIdx) {
                //KIVÁLASZTÁS DB-ből
                System.out.println("A kiválasztott új labda: " + ballList.get(ballIdx) + "\nMegerősíti?");
                System.out.println("1 - IGEN\n0 - NEM");
                Integer userInput_confirm = select_menuItem();
                if (userInput_confirm == 1) {
                    remainInLoop = false;
                    //Új labda felvitele az adatbázisba
                    try {
                        if (this.higherMenuLogic.update_newID(tableName_forQuery, columnName_detailID, userInput_newBallID)) {
                            System.out.println("Adatfrissítés megtörtént.");
                        } else {
                            System.out.println("Az adatfrissítés sikertelen.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else{
                System.out.println("Nem értelmezhető adatbevitel. Próbálja újra!\n");
            }
        }
    }

    private void subMenu_FM24_telefonModositas(){
        String tableName_forQuery = "ContactDetails";
        String columnName_ofStringDetail = "PhoneNumber";
        String columnName_detailID = "ContactDetailsID";

        System.out.println("Az adatbázisban szereplő jelenlegi kapcsolattartási telefonszám:\n" + this.higherMenuLogic.
                            get_currentDetail(tableName_forQuery, columnName_ofStringDetail, columnName_detailID) + "\n");
        
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
                //ÚJ telefonszám MEGADÁSA
                boolean remainInLoop_newPhoneNumber = true;
                while (remainInLoop_newPhoneNumber) {
                    System.out.println("Új telefonszám megadása\n");
                    System.out.print("Kérem, írja be helyesen az új telefonszámot: ");
                    String newPhoneNumber_string = request_userInput();
                    System.out.println();
    
                    //Visszaírás és megerősítés kérése
                    System.out.println("Az ön által beírt telefonszám: " + newPhoneNumber_string + "\nMegerősíti?");
                    System.out.println("1 - IGEN\n0 - NEM");
                    Integer userInput_confirm = select_menuItem();

                    //Új telefonszám felvitele az adatbázisba
                    if (userInput_confirm == 1) {
                        remainInLoop_newPhoneNumber = false;
                        remainInLoop = false;
                        if (this.higherMenuLogic.update_detail(tableName_forQuery, columnName_ofStringDetail, columnName_detailID, newPhoneNumber_string)) {
                            System.out.println("Adatfrissítés megtörtént.");
                        }
                    }
                }
            } else{
                System.out.println("Nem értelmezhető adatbevitel. Próbálja újra!\n");
            }
        }
    }

    private void subMenu_FM25_emailModositas(){
        String tableName_forQuery = "ContactDetails";
        String columnName_ofStringDetail = "Email";
        String columnName_detailID = "ContactDetailsID";

        System.out.println("Az adatbázisban szereplő jelenlegi emailcím:\n" + this.higherMenuLogic.
                            get_currentDetail(tableName_forQuery, columnName_ofStringDetail, columnName_detailID) + "\n");
        
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
                //ÚJ emailcím MEGADÁSA
                boolean remainInLoop_newEmail = true;
                while (remainInLoop_newEmail) {
                    System.out.println("Új emailcím megadása\n");
                    System.out.print("Kérem, írja be helyesen az új emailcímet: ");
                    String newEmail_string = request_userInput();
                    System.out.println();
    
                    //Visszaírás és megerősítés kérése
                    System.out.println("Az ön által beírt emailcím: " + newEmail_string + "\nMegerősíti?");
                    System.out.println("1 - IGEN\n0 - NEM");
                    Integer userInput_confirm = select_menuItem();

                    //Új telefonszám felvitele az adatbázisba
                    if (userInput_confirm == 1) {
                        remainInLoop_newEmail = false;
                        remainInLoop = false;
                        if (this.higherMenuLogic.update_detail(tableName_forQuery, columnName_ofStringDetail, columnName_detailID, newEmail_string)) {
                            System.out.println("Adatfrissítés megtörtént.");
                        }
                    }
                }
            } else{
                System.out.println("Nem értelmezhető adatbevitel. Próbálja újra!\n");
            }
        }
    }

    private void subMenu_FM26_jelszoModositas(){
        String tableName_forQuery = "Credentials";
        String columnName_ofStringDetail = "Password";
        String columnName_detailID = "ContactPersonID";

        System.out.println("A jelszó módosításához, kérem, adja meg a belépési adatait!");
        if (subMenu_FM2a_belepes()) {
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
                                if (this.higherMenuLogic.update_password(tableName_forQuery, columnName_ofStringDetail, twoPWs[1])) {
                                    System.out.println("Adatfrissítés megtörtént.");
                                }
                            }
                        }           //IF - egyezőség ellenőrzése 
                        else{
                            System.out.println("A két jelszó nem egyezik. Kérem, próbálja újra!");
                        }
                    }           //Új pw megadása - while
                } else{
                    System.out.println("Nem értelmezhető adatbevitel. Próbálja újra!\n");
                }
            }           //Main while
        }           //Beléptető if
    }           //Metódus vége

    //-------------------------------------------------------------------------------------------------------------
    //MENÜK KIÍRATÁSA
    private void display_FM2menu(){
        String menuScript = "1 - EREDMÉNYEK BEVITELE\r\n" +
                            "2 - HAZAI PÁLYA MÓDOSÍTÁSA\r\n" +
                            "3 - LABDA MÓDOSÍTÁSA\r\n" +
                            "4 - TELEFONSZÁM MÓDOSÍTÁSA\r\n" +
                            "5 - EMAILCÍM MÓDOSÍTÁSA\r\n" +
                            "6 - JELSZÓ MÓDOSÍTÁSA\r\n" +
                            "0 - KILÉPÉS";
        System.out.println(menuScript);                                
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


}
