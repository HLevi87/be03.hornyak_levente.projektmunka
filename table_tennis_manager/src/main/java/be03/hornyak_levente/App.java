package be03.hornyak_levente;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import be03.hornyak_levente.Repository.Repository;
import be03.hornyak_levente.UI.GetCredentials;
import be03.hornyak_levente.UI.MainMenuUI;

public class App 
{
    public static void main( String[] args )
    {
        //EZ MAJD KELLENI FOG A DB FELHASZNÁLÓNÉV ÉS JELSZÓ BEKÉRÉSÉHEZ + a MainRepo KONSTRUKTURÁT ÁT KELL ÍRNI!!!
        // GetCredentials getCredentials = new GetCredentials();

        MainMenuUI mainMenu = new MainMenuUI();
        mainMenu.start_menu();





        
    }
}
