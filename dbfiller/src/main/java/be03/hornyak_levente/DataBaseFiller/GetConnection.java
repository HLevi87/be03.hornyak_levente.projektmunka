package be03.hornyak_levente.DataBaseFiller;

import java.sql.Connection;
import java.sql.DriverManager;

public class GetConnection {
    private static final String DB_PATH = "jdbc:sqlserver://localhost;databaseName=TableTennisV2;encrypt=false;user=java;password=java;";
    
    Connection connection;

    public GetConnection(Connection connection) {
        try {
            this.connection = DriverManager.getConnection(DB_PATH);
        } catch (Exception e) {
            System.out.println("A kapcsolat létrehozása sikertelen: ");
            e.printStackTrace();
        }
    }

}
