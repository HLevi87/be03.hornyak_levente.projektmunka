package be03.hornyak_levente.DataBaseFiller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import be03.hornyak_levente.Logic.Umpires;
import be03.hornyak_levente.Models.Umpire;

public class UmpireTable {

    public static void deleteThen_manage_UmpiresTable(Umpires umpires){
        if (doesUmpireTableExist()) {
            try{
                Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;");
                Statement statement = connection.createStatement();
                String query = "DROP TABLE Umpires;";
                statement.executeUpdate(query);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        manage_umpireTable(umpires);
    }

    public static void manage_umpireTable(Umpires umpires){
        if (!doesUmpireTableExist()) {
            System.out.println("Umpires tábla még nem létezik.");
            if (create_umpireTable()) {
                System.out.println("Az Umpire tábla létrehozása sikeres volt.");
            } else{
                System.out.println("Nem sikerült létrehozni az Umpire táblát.");
            }
        }
        if (initializeData_umpireTable(umpires)) {
            System.out.println("Az Umpires tábla adatfeltöltése sikeres volt.");
        } else{
            System.out.println("Hiba történt az Umpires tábla adatfeltöltése során.");
        }
    }

    private static Boolean doesUmpireTableExist(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;");
            Statement statement = connection.createStatement();
            String query = "SELECT count(*) FROM information_schema.tables WHERE table_name = 'Umpires'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (!rs.getObject(1).equals(0)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Boolean create_umpireTable(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;");
            Statement statement = connection.createStatement();
            String query =  "CREATE TABLE Umpires (" +
                            "TechnicalID int IDENTITY(1,1) NOT NULL PRIMARY KEY, " +
                            "UmpireID int, " +
                            "ValidFrom date, " +
                            "ValidTo date, " +
                            "IsActive bit, " +
                            "ContactID int, " +
                            "UmpireName varchar(50))";
            statement.executeUpdate(query);
            if (doesUmpireTableExist()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static Boolean initializeData_umpireTable(Umpires umpires){
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;");
            for (int i = 0; i < umpires.getUmpireList().size(); i++) {
                //Kapcsolattartási adatok beírása a ContactDetails táblába
                try(PreparedStatement ps = connection.prepareStatement("INSERT INTO ContactDetails VALUES(?, ?)")){
                    ps.setString(1, umpires.getUmpireList().get(i).getEmailAddress());
                    ps.setString(2, umpires.getUmpireList().get(i).getPhoneNumber());

                    ps.executeUpdate();
                }

                //ContactDetailsID lekérése
                int contacDetailsID = 0;
                Statement statement = connection.createStatement();
                String query = "SELECT ContactDetailsID FROM ContactDetails WHERE email = '" + umpires.getUmpireList().get(i).getEmailAddress() + "'";
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()) {
                    contacDetailsID = rs.getInt(1);
                }

                //Az Umpires tábla egy új sora
                /*
                 *  "UmpireID int, " +
                    "ValidFrom date, " +
                    "ValidTo date, " +
                    "IsActive bit, " +
                    "ContactID int, " +
                    "UmpireName varchar(50))"
                 */
                try(PreparedStatement ps2 = connection.prepareStatement("INSERT INTO Umpires VALUES(?, ?, NULL, ?, ?, ?)")){
                    ps2.setInt(1, i+1);
                    ps2.setString(2, LocalDate.now().toString());
                    ps2.setInt(3, 1);
                    ps2.setInt(4, contacDetailsID);
                    ps2.setString(5, umpires.getUmpireList().get(i).getUmpireName());

                    ps2.executeUpdate();
                }
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
