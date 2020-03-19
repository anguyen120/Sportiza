import java.sql.SQLException;

public class main {
    ////////////////////////////////////////
    //MAKE SURE the directory "requestFiles" is created before requesting anything
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //constructor to set up the connector between database and this class
        connectorPostgres connector = new connectorPostgres(config.username,config.credential,config.databaseName);

        // requesting form with parameters in following order
        //FirstName LastName TeamName UniformNumber HomeTown
        //connector.playerFormRequest("NULL", "NULL", "UCLA", "NULL", "Plano");
        connector.playerFormRequest("NULL", "NULL", "Texas A&M", "NULL", "NULL");

        //Parameters player id, First Name, Last name, home town
        connector.playerStatsRequest("1041700","Johnny", "Manziel","kerriville");

        //Parameters team name, conference name, subdivision (they can all be "NULL"
        connector.teamFormRequest("NULL","NULL","NULL");

        //Requesting stats for a given team
        //Parameters String teamID, String team Name
        connector.teamGameStatsRequest("697","Texas A&M");


        //updating the winning team of the games table
        //connector.updateAllWinningTeams();

        //Shortest Victory Chain Algorithm
        //outpput redirected to shortestVictoryChain.js
        //Parameters : source Team ID, target Team ID
        connector.shortestVictoryChainRequest("697","5");

        //determines Home Towns with most different stats
        connector.homeTownsWithMostStats();
        //connector.hometownsWithMostPositions("QB");
        //connector.hometownsWithMostPlayers();
        //System.out.println(connector.getTeamActiveSeasons("697"));
        //System.out.println(connector.getConferenceName("697","2013"));

    }
}
