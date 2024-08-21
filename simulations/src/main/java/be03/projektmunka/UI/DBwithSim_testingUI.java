package be03.projektmunka.UI;

import java.util.List;

import be03.projektmunka.Logic.DBwithSim_testingLogic;

public class DBwithSim_testingUI {
    private DBwithSim_testingLogic testingLogic;

    public DBwithSim_testingUI() {
        this.testingLogic = new DBwithSim_testingLogic();
        matchesPlayedCheck();
        pointsSumCheck();
        winLossSum();
        unequalWDL();
        matchesPlayed_teamVSplayers();
    }

    private void matchesPlayedCheck(){
        List <Integer> teamIDs = this.testingLogic.matchesPlayed();
        if (teamIDs.size() == 0) {
            System.out.println("Minden csapat megfelelő számú mérkőzést játszott le.\n");
        } else{
            System.out.println("Az alábbi sorszámú csapatok lejátszott meccseinek száma hibára utal:\n");
            for (int i = 0; i < teamIDs.size(); i++) {
                System.out.println(teamIDs.get(i));
            }
            System.out.println();
        }
    }

    private void pointsSumCheck(){
        List <Integer> teamIDs = this.testingLogic.points();
        if (teamIDs.size() == 0) {
            System.out.println("Minden csapat pontszáma megfelelő.\n");
        } else{
            System.out.println("Az alábbi sorszámú csapatok pontszáma hibára utal:\n");
            for (Integer integer : teamIDs) {
                System.out.println(integer);
            }
            System.out.println();
        }
    }

    private void winLossSum(){
        if (this.testingLogic.isWinLossEqual()) {
            System.out.println("A nyert meccsek összes száma megegyezik az elvesztett meccsek összes számával.\n");
        } else{
            System.out.println("A nyert és a vesztett meccsek száma között eltérés van.\n");
        }
    }
    
    private void unequalWDL(){
        List <Integer> teamIDs = this.testingLogic.unequalWDL();
        if (teamIDs.size() == 0) {
            System.out.println("A lejátszott meccsek száma minden csapatnál egyenlő a megnyert + döntetlen + elveszített meccsek számával.\n");
        } else{
            System.out.println("Az alábbi sorszámú csapatoknál nem egyezik a megnyert + döntetlen + elveszített meccsek száma a lejátszott meccsek számával:\n");
            for (Integer integer : teamIDs) {
                System.out.println(integer);
            }
            System.out.println();
        }
    }

    private void matchesPlayed_teamVSplayers(){
        List <Integer> teamIDs = this.testingLogic.matchesPlayed_teamVSplayers();
        if (teamIDs.size() == 0) {
            System.out.println("A lejátszott egyéni meccsek számában nincs eltérés a csapat és a csapat játékosainak statiksztikájában.\n");
        } else{
            System.out.println("Az alábbi sorszámú csapatoknál nem egyezik a lejátszott egyéni meccsek száma a csapat és a csapat játékosainak statiksztikájában:\n");
            for (Integer integer : teamIDs) {
                System.out.println(integer);
            }
            System.out.println();
        }
    }

}
