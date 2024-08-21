package be03.hornyak_levente;

import be03.hornyak_levente.Factory.TeamLists;
import be03.hornyak_levente.Logic.InitializeContactPersons;
import be03.hornyak_levente.Logic.PlayerListClass;
import be03.hornyak_levente.Logic.Upload;
import be03.hornyak_levente.DataBaseFiller.FillUpTeams;
import be03.hornyak_levente.DataBaseFiller.FillUpTeamsV2;
import be03.hornyak_levente.Factory.LogicQueries;
import be03.hornyak_levente.Factory.NameFactory;
import be03.hornyak_levente.Factory.Processor;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        /*
         * Ez az 1. projekt, amely az alábbi feladatokat látja el:
         *      - alapadatok beolvasása fájlból (csapatnevek, helyszínek, labdák);
         *      - játékos- és játékvezető-nevek véletlenszerű generálása fájlból és hozzárendelése csapatokhoz
         *      - DUMMY kapcsolattartási adatok generálása
         *      - táblák létrehozása és a fenti adatok feltöltése a megfelelő táblákba
         * 
         */



        

        //TÁBLÁK LÉTREHOZÁSA A KÖVETKEZŐ SORRENDBEN:
            //FillUpTeamsV2.fillUp_TeamsDB:
                //Teams
                //ContactDetails
                //Locations
                //Balls
            //Upload.PlayerUpload
                //Players
            //Upload.FixtureUpload
                //Fixture
            //Upload.MatchResultsUpload
                //MatchResultsOneVOne
            //Upload.CredentialsUpload
                //Credentials
            //Upload.SeasonsUpload
                //Seasons

        // FillUpTeamsV2.createAndfillUp_TeamsAndCoTables(1);
        FillUpTeamsV2.deleteAnd_createAndfillUp_TeamsAndCoTables(1);     //táblák törlése és újbóli létrehozása

        // Upload.PlayerUpload(1);
        Upload.deleteThenUpload_PlayersTable(1);    //tábla törlése és újbóli létrehozása

        // Upload.UmpireUpload();
        Upload.deleteThenUpload_UmpiresTable();     //tábla törlése és újbóli létrehozása

        // Upload.FixtureUpload();
        Upload.deleteThenUpload_FixtureTable();     //tábla törlése és újbóli létrehozása

        // Upload.MatchResultsUpload();
        Upload.deleteThenUpload_MatchResults();     //tábla törlése és újbóli létrehozása

        // Upload.SeasonsUpload();
        Upload.deleleteThenCreate_SeasonsTable();   //tábla törlése és újbóli létrehozása


        InitializeContactPersons initializeContactPersons = new InitializeContactPersons();
        System.out.println(initializeContactPersons.linesModified + " új kapcsolattartó személy felvitele sikeres volt.");

        // Upload.CredentialsUpload();
        Upload.deleteThenCreate_credentialsTable(); //tábla törlése és újbóli létrehozása



    }

    private static void printOut_list(List<String> list){
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        
    }

    private static void print_toFile(List<String> lista){
        try {
            FileWriter fw = new FileWriter("RandomLista.txt");
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < lista.size(); i++) {
                pw.println(lista.get(i));
            }
            pw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
