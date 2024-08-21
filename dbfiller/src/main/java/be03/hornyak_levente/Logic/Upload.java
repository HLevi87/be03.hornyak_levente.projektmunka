package be03.hornyak_levente.Logic;

import be03.hornyak_levente.DataBaseFiller.CredentialsTable;
import be03.hornyak_levente.DataBaseFiller.FixtureTable;
import be03.hornyak_levente.DataBaseFiller.MatchResultsTable;
import be03.hornyak_levente.DataBaseFiller.PlayerTable;
import be03.hornyak_levente.DataBaseFiller.SeasonsTable;
import be03.hornyak_levente.DataBaseFiller.UmpireTable;

public class Upload {
            //PLAYERS

    public static void PlayerUpload(Integer seasonID){
        PlayerTable playerTable = new PlayerTable();
        playerTable.manage_playersTable(seasonID);
    }

    public static void deleteThenUpload_PlayersTable(Integer seasonID){
        PlayerTable playerTable = new PlayerTable();
        playerTable.deleteThen_manage_PlayersTable(seasonID);
    }


        //UMPIRES
    public static void UmpireUpload(){
        Umpires umpires = new Umpires();
        UmpireTable.manage_umpireTable(umpires);
    }

    public static void deleteThenUpload_UmpiresTable(){
        Umpires umpires = new Umpires();
        UmpireTable.deleteThen_manage_UmpiresTable(umpires);
    }


        //FIXTURE
    public static void FixtureUpload(){
        FixtureTable.manage_fixtureTable();
    }

    public static void deleteThenUpload_FixtureTable(){
        FixtureTable.deleteThenManage_FixtureTable();
    }


        //MATCHRESULTSONEVONE
    public static void MatchResultsUpload() {
        MatchResultsTable.manage_matchResultsTable();
    }

    public static void deleteThenUpload_MatchResults(){
        MatchResultsTable.deleteThen_manage_matchResultsTable();
    }


        //CREDENTIALS
    public static void CredentialsUpload(){
        CredentialsTable.manage_credentialsTable();
    }

    public static void deleteThenCreate_credentialsTable(){
        CredentialsTable.deleteThen_manage_CredentialsTable();
    }



        //SEASONS
    public static void SeasonsUpload(){
        SeasonsTable.manage_seasonsTable();
    }

    public static void deleleteThenCreate_SeasonsTable(){
        SeasonsTable.deleteThen_manage_SeasonsTable();
    }

}
