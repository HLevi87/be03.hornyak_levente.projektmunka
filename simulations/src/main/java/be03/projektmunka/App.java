package be03.projektmunka;

import be03.projektmunka.Logic.IndividualMatchesLogic;
import be03.projektmunka.Logic.RoundRobinLogic;
import be03.projektmunka.Logic.UpdateDBlogic;
import be03.projektmunka.UI.SimulationsUI;
import be03.projektmunka.UI.DBwithSim_testingUI;
import be03.projektmunka.UI.RoundRobinTestingUI;

public class App 
{
    public static void main( String[] args )
    {
        RoundRobinTestingUI testingRoundRobin = new RoundRobinTestingUI();
        testingRoundRobin.printOut_drawing_byTeamName();
        testingRoundRobin.printOut_drawing_byTeamID();
        IndividualMatchesLogic individualMatches = new IndividualMatchesLogic();
        individualMatches.do_drawing();
        individualMatches.manage_individualMatches();

        SimulationsUI simulationsUI = new SimulationsUI();
        simulationsUI.oneTeamMatch_wExternalDetails(0, 0);
        System.out.println();

        UpdateDBlogic fullSeasonSimulation = new UpdateDBlogic();
        fullSeasonSimulation.do_drawing();
        fullSeasonSimulation.manage_individualMatches();
        // fullSeasonSimulation.update_dataBase("2023/2024");
        fullSeasonSimulation.update_dataBase_WITHdelete("2023/2024");

        DBwithSim_testingUI simTesting = new DBwithSim_testingUI();
    }

    
}
