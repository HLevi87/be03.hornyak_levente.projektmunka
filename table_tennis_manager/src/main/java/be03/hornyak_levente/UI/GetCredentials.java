package be03.hornyak_levente.UI;

import java.util.Scanner;

import be03.hornyak_levente.Logic.BuildConnectionLogic;

public class GetCredentials {
    private BuildConnectionLogic buildConnection;
    private Scanner scanner;

    public GetCredentials() {
        this.scanner = new Scanner(System.in);
        this.buildConnection = new BuildConnectionLogic(get_user(), get_pw());
        if (buildConnection.build_connection()) {
            System.out.println("\nA kapcsolat létrehozása sikeres volt.\n");
        } else{
            System.out.println("\nA kapcsolat létrehozása nem sikerült.\n");
        }
    }

    private String get_user(){
        Boolean acceptableInput = false;
        String userName = null;
        while (!acceptableInput) {
            System.out.print("Kérem a felhasználónevet: ");
            userName = this.scanner.nextLine();
            if (userName.length() > 0) {
                acceptableInput = true;
            }
        }
        return userName;
    }
    
    private String get_pw(){
        Boolean acceptableInput = false;
        String pw = null;
        while (!acceptableInput) {
            System.out.print("Kérem a jelszót: ");
            pw = this.scanner.nextLine();
            if (pw.length() > 0) {
                acceptableInput = true;
            }
        }
        return pw;
    }

}
