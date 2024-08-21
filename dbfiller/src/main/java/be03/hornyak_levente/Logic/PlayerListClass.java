package be03.hornyak_levente.Logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import be03.hornyak_levente.DataBaseFiller.PlayerTable;
import be03.hornyak_levente.Factory.LogicQueries;
import be03.hornyak_levente.Factory.NameFactory;
import be03.hornyak_levente.Models.Player;

public class PlayerListClass {
    public List<Player> playerList;

    public PlayerListClass() {
        this.playerList = generate_playerList();
        set_teamIDs();
        set_licenceNumbers();
        set_strengths();
        initialize_statistics();
        cheatCode();
    }

    private void cheatCode(){
        for (int i = 0; i < this.playerList.size(); i++) {
            if (playerList.get(i).getTeamID().equals(11)) {
                playerList.get(i).setName("Hornyák Levente");
                playerList.get(i).setPlayerStrength(6);
                playerList.get(i).setLicenceNumber(69420);
                return;
            }
        }
    }

    private List<Player> generate_playerList(){
        List<Player> playerList = new ArrayList<>();
        NameFactory nameFactory = new NameFactory("Vezetéknevek.txt", "Férfi keresztnevek.txt", "Női keresztnevek.txt");
        Random rnd = new Random();
        for (int i = 0; i < rnd.nextInt(80, 90); i++) {
            String oneName = nameFactory.getSurnames().get(rnd.nextInt(nameFactory.getSurnames().size())) + " " + nameFactory.getFirstNamesMixed().get(rnd.nextInt(nameFactory.getFirstNamesMixed().size()));
            Player onePlayer = new Player(oneName);
            playerList.add(onePlayer);
        }
        return playerList;
    }

    private void set_teamIDs(){
        Integer numberOfTeams = LogicQueries.count_numberOfTeams();
        Integer[] teamLimits = new Integer[numberOfTeams];
        for (int i = 0; i < teamLimits.length; i++) {
            teamLimits[i] = 0;
        }
        Random rnd = new Random();
        int minValue = 0;
        for (int i = 0; i < this.playerList.size(); i++) {
            Boolean isAllocated = false;
            while (!isAllocated) {
                Integer teamNumber = rnd.nextInt(numberOfTeams);
                if (teamLimits[teamNumber] <= 4) {
                    teamLimits[teamNumber]++;
                    isAllocated = true;
                    this.playerList.get(i).setTeamID(teamNumber + 1);
                } else if(i > this.playerList.size()/2 && minValue <= 4){
                    minValue = Integer.MAX_VALUE;
                    for (int j = 0; j < teamLimits.length; j++) {
                        if (teamLimits[j] < minValue) {
                            teamNumber = j;
                            minValue = teamLimits[j];
                        }
                    }
                    teamLimits[teamNumber]++;
                    isAllocated = true;
                    this.playerList.get(i).setTeamID(teamNumber + 1);
                } else if(i > this.playerList.size()/2 && minValue > 4){
                    teamLimits[teamNumber]++;
                    isAllocated = true;
                    this.playerList.get(i).setTeamID(teamNumber + 1);
                }
            }
        }
    }

    private void set_licenceNumbers(){
        Random rnd = new Random();
        for (int i = 0; i < this.playerList.size(); i++) {
            int licenceLength = rnd.nextInt(4, 6);
            if (licenceLength == 4) {
                this.playerList.get(i).setLicenceNumber(rnd.nextInt(1000, 10000));
            } else {
                this.playerList.get(i).setLicenceNumber(rnd.nextInt(10000, 100000));
            }
        }
    }
    
    private void set_strengths(){
        Random rnd = new Random();
        for (int i = 0; i < this.playerList.size(); i++) {
            int strength = rnd.nextInt(1, 7);
            if (strength == 6) {
                strength = rnd.nextInt(5, 7);
            }
            if (strength == 5) {
                strength = rnd.nextInt(4, 6);
            }
            if (strength == 1) {
                strength = rnd.nextInt(1, 4);
            }
            if (strength == 2) {
                strength = rnd.nextInt(2, 4);
            }
            this.playerList.get(i).setPlayerStrength(strength);
        }
    }

    private void initialize_statistics(){
        for (int i = 0; i < this.playerList.size(); i++) {
            this.playerList.get(i).setMatchesPlayed(0);
            this.playerList.get(i).setMatchesWon(0);
            this.playerList.get(i).setWinPercentage(0.0);
        }
    }

    public void printOut(){
        System.out.println("Név / csapatszám / engedélyszám / erősség / stat (P/W/%)");
        for (int i = 0; i < this.playerList.size(); i++) {
            System.out.println(
                    this.playerList.get(i).getName() + " / " +
                    this.playerList.get(i).getTeamID() + " / " + 
                    this.playerList.get(i).getLicenceNumber() + " / " +
                    this.playerList.get(i).getPlayerStrength() + " / (" +
                    this.playerList.get(i).getMatchesPlayed() + "/" +
                    this.playerList.get(i).getMatchesWon() + "/" +
                    this.playerList.get(i).getWinPercentage() + ")"
            );
        }
        System.out.println("\nÖsszesen " + this.playerList.size() + " játékos\n");


        System.out.println("Csapatszámok és játékosok száma");
        Integer numberOfTeams = LogicQueries.count_numberOfTeams();
        Integer[] teams = new Integer[numberOfTeams];
        for (int i = 0; i < teams.length; i++) {
            teams[i] = 0;
        }
        for (int i = 0; i < this.playerList.size(); i++) {
            teams[playerList.get(i).getTeamID()-1]++;
        }
        for (int i = 0; i < teams.length; i++) {
            System.out.println(i+1 + "\t" + teams[i]);
        }


        System.out.println("\nErősség és játékosok száma");
        Integer[] strengthArray = new Integer[6];
        for (int i = 0; i < strengthArray.length; i++) {
            strengthArray[i] = 0;
        }
        for (int i = 0; i < this.playerList.size(); i++) {
            strengthArray[this.playerList.get(i).getPlayerStrength()-1]++;
        }
        for (int i = 0; i < strengthArray.length; i++) {
            System.out.println((i+1) + "\t" + strengthArray[i]);
        }
    }

    
}
