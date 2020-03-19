
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.sql.*;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import org.json.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

import org.jgrapht.*;
import org.jgrapht.graph.*;
//import org.jgrapht.nio.*;
//import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.traverse.*;
public class connectorPostgres {
    Connection conn;
    DefaultDirectedGraph<String, NamedEdge> winningDirectedGraph = new DefaultDirectedGraph<>(NamedEdge.class);
    // setting up connector to database during constructor
    connectorPostgres(String username, String credential, String database){
        try {
            Class.forName("org.postgresql.Driver");
            conn =  DriverManager.getConnection(database,username, credential);
            this.SportizaPrint("Connected to sportizadevspace database!");
            this.loadWinningGraph();
            this.SportizaPrint("Winning Directed Graph Loaded!\n");
        }catch(Exception e){
           this.SportizaPrint(e.toString());
        }
    }
    //request for all players given all its parameters
    public void playerFormRequest(String FirstName, String LastName, String Team, String UniformNumber, String HomeTown) {
        //formating strings inputs for SQL command
        if(!FirstName.equals("NULL")) {
            FirstName = FirstName.replace("\'","\'\'");
            FirstName = String.format("\'%s\'", FirstName);
        }
        if(!LastName.equals("NULL")) {
            LastName = LastName.replace("\'","\'\'");
            LastName = String.format("\'%s\'", LastName);
        }
        if(!Team.equals("NULL")) {
            Team = Team.replace("\'","\'\'");
            Team = String.format("\'%s\'", Team);
        }
        if(!UniformNumber.equals("NULL")) {
            UniformNumber = UniformNumber.replace("\'","\'\'");
            UniformNumber = String.format("\'%s\'", UniformNumber);
        }
        if(!HomeTown.equals("NULL")) {
            HomeTown = HomeTown.replace("\'","\'\'");
            HomeTown = String.format("\'%s\'", HomeTown);
        }
        //base query format
//		String query = "SELECT DISTINCT ON(\"players\".\"First Name\",\"players\".\"Last Name\") \"players\".\"id\",\"players\".\"First Name\", \"players\".\"Last Name\",\"players\".\"Position\", \"players\".\"Home Town\", \"players\".\"Home State\", \"players\".\"Home Country\"\n" +
//				"    FROM \"players\" JOIN \"teams\"\n" +
//				"        ON \"teams\".\"name\"  = COALESCE(%1$s,\"teams\".\"name\")\n" +
//				"               and \"teams\".\"id\" = \"players\".\"Team Code\"\n" +
//				"               and \"players\".\"First Name\" =  COALESCE(%2$s,\"players\".\"First Name\")\n" +
//				"               and \"players\".\"Last Name\" =  COALESCE(%3$s,\"players\".\"Last Name\")\n" +
//				"               and \"players\".\"Uniform Number\" = COALESCE(%4$s,\"players\".\"Uniform Number\")\n" +
//				"               and \"players\".\"Home Town\" = COALESCE(%5$s,\"players\".\"Home Town\");";
        String query = "SELECT DISTINCT ON (playerChosen.\"id\") playerChosen.\"id\", \"First Name\",\"Last Name\",\"Home Town\",\"Home Country\",\"Home State\",\"Position\", \"name\" FROM  (SELECT DISTINCT ON (\"players\".\"id\")  \"players\".\"id\",\"players\".\"First Name\", \"players\".\"Last Name\",\"players\".\"Position\", \"players\".\"Home Town\", \"players\".\"Home State\", \"players\".\"Home Country\",\"players\".\"Team Code\"\n" +
                "    FROM players\n" +
                "        WHERE \"First Name\" =  COALESCE(%2$s,\"players\".\"First Name\")\n" +
                "               and \"Last Name\" = COALESCE(%3$s,\"players\".\"Last Name\")\n" +
                "               and (\"players\".\"Uniform Number\" = COALESCE(%4$s, \"players\".\"Uniform Number\"))\n" +
                "               and (\"players\".\"Home Town\" = COALESCE(%5$s, \"players\".\"Home Town\")))as playerChosen\n" +
                "        INNER JOIN \"teams\"\n" +
                "            ON \"teams\".\"id\" = playerChosen.\"Team Code\" WHERE \"teams\".\"name\" = COALESCE(%1$s, \"teams\".\"name\");\n";
        if(HomeTown.equals("NULL")){
            query = "SELECT DISTINCT ON (playerChosen.\"id\") playerChosen.\"id\", \"First Name\",\"Last Name\",\"Home Town\",\"Home Country\",\"Home State\",\"Position\", \"name\" FROM  (SELECT DISTINCT ON (\"players\".\"id\")  \"players\".\"id\",\"players\".\"First Name\", \"players\".\"Last Name\",\"players\".\"Position\", \"players\".\"Home Town\", \"players\".\"Home State\", \"players\".\"Home Country\",\"players\".\"Team Code\"\n" +
                    "    FROM players\n" +
                    "        WHERE \"First Name\" =  COALESCE(%2$s,\"players\".\"First Name\")\n" +
                    "               and \"Last Name\" = COALESCE(%3$s,\"players\".\"Last Name\")\n" +
                    "               and (\"players\".\"Uniform Number\" = COALESCE(%4$s, \"players\".\"Uniform Number\") )\n" +
                    "               and (\"players\".\"Home Town\" = COALESCE(%5$s, \"players\".\"Home Town\") OR \"players\".\"Home Town\" IS NULL))as playerChosen\n" +
                    "        INNER JOIN \"teams\"\n" +
                    "            ON \"teams\".\"id\" = playerChosen.\"Team Code\" WHERE \"teams\".\"name\" = COALESCE(%1$s, \"teams\".\"name\");\n";
        }
        //loading values to empty query
        query = String.format(query, Team,FirstName, LastName, UniformNumber, HomeTown);
        //response for executed Query
        //System.out.println(query);
        this.SportizaPrint("Requesting Players with parameters; First Name: " + FirstName + ", Last Name: " + LastName + ", Team: " + Team + ", Uniform Number: "  + UniformNumber + ", HomeTown: " + HomeTown);
        ResultSet response = this.executeQuery(query);
        try {
            FileWriter jsonFile = new FileWriter(config.requestPlayerFormFile);
            //JSONObject fileObject = new JSONObject();
            JSONArray fileObject = new JSONArray();
            while(response.next()) {
                String playerID = response.getString("id");
                String playerName = response.getString("First Name");
                if(response.wasNull()) {
                    //System.out.println("Player Name is null");
                    playerName = "N/A";
                }
                String playerLastName = response.getString("Last Name");
                if(response.wasNull()) {
                    //System.out.println("Player Last Name is null");
                    playerLastName = "N/A";
                }
                String playerTown = response.getString("Home Town");
                if(response.wasNull()) {
                    //System.out.println("playerTown is null");
                    playerTown = "N/A";
                }
                String position = response.getString("Position");
                if(response.wasNull()) {
                    //System.out.println("position is null");
                    position = "N/A";
                }
                String playerState = response.getString("Home State");
                if(response.wasNull()) {
                    //System.out.println("playerState is null");
                    playerState = "N/A";
                }
                String playerCountry = response.getString("Home Country");
                if(response.wasNull()) {
                    //System.out.println("playerCountry is null");
                    playerCountry = "N/A";
                }
                //System.out.println("First name: " + playerName + ", Last Name: " + playerLastName+ ", Position: " + position +  ", Home Town: " + playerTown + ", State: " + playerState + ", Country: " + playerCountry);
                JSONObject userObject = new JSONObject();
                userObject.put("id", playerID);
                userObject.put("First Name", playerName);
                userObject.put("Last Name", playerLastName);
                userObject.put("Home Town", playerTown);
                userObject.put("Home State", playerState);
                userObject.put("Home Country", playerCountry);
                userObject.put("Position", position);
                fileObject.put(userObject);
            }
            jsonFile.write("var query = ");
            jsonFile.write(fileObject.toString());
            jsonFile.write(";");
            jsonFile.close();
            this.SportizaPrint("Players Form Request with Parameters: First Name: " + FirstName + ", Last Name: " + LastName + ", " + "Team: " + Team + ", Uniform Number: " + UniformNumber + ", Home Town: " + HomeTown+ " Completed! \n");
        } catch (SQLException | JSONException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //request stats for an specific player given its id
    public void playerStatsRequest(String id,String firstName, String lastName, String homeTown) {
        String seasonsQuery = "SELECT DISTINCT /*\"games\".\"id\",*/ \"games\".\"season\" /*\"players\".\"First Name\", \"players\".\"Last Name\"*/\n" +
                "    FROM \"Player Game Stats\"\n" +
                "        JOIN \"players\"\n" +
                "            ON \"Player Game Stats\".\"Player Code\" = \"players\".\"id\" and \"players\".\"id\" = %1$s\n" +
                "        JOIN \"games\"\n" +
                "            ON \"games\".\"id\" = \"Player Game Stats\".\"Game Code\";";
        seasonsQuery = String.format(seasonsQuery, id);
//		System.out.println(seasonsQuery);
        this.SportizaPrint("Requesting Stats Data for Player; " + "id: "   + id + ", First Name: " + firstName + ", Last Name: " + lastName);
        ResultSet seasonsResponse = this.executeQuery(seasonsQuery);
        ArrayList<String> seasons = new ArrayList<String>();
        String  statsQueryrequested = "SELECT\n" +
                "    round(cast(cast(SUM(\"Pass Comp\") as float4) / (CASE cast(SUM(\"Pass Att\") as float4) WHEN 0 THEN 1 ELSE  cast(SUM(\"Pass Att\") as float4) END) as numeric)*100.00,1) as \"Pass Completion Rate\",\n" +
                "    --Touchdowns stats\n" +
                "    ROUND(avg(\"Pass TD\" + \"Rush TD\"  + \"Rec TD\" + \"Kickoff Ret TD\"  + \"Punt Ret TD\" + \"Fum Ret TD\" + \"Int Ret TD\" + \"Misc Ret TD\"),2) as \"TD Average per Game\",\n" +
                "    SUM(\"Pass TD\" + \"Rush TD\"  + \"Rec TD\" + \"Kickoff Ret TD\"  + \"Punt Ret TD\" + \"Fum Ret TD\" + \"Int Ret TD\" + \"Misc Ret TD\") as \"Total TD\",\n" +
                "    SUM(\"Rush TD\") as \"Rush TD Total\",\n" +
                "    SUM(\"Pass TD\") as \"Pass TD Total\",\n" +
                "    SUM(\"Int Ret TD\") as \"Interception Ret TD\",\n" +
                "    SUM(\"Rec TD\") as \"Pass Received TD\",\n" +
                "    SUM(\"Kickoff Ret TD\") as \"Kickoff Ret TD\",\n" +
                "    SUM(\"Punt Ret TD\") as \"Punt Ret TD\",\n" +
                "    SUM(\"Fum Ret TD\") as \"Fumble Ret TD\",\n" +
                "    SUM(\"Misc Ret TD\") as \"Other TD\",\n" +
                "    --yards stats\n" +
                "    ROUND(avg(\"Pass Yard\" + \"Rush Yard\" + \"Punt Yard\" + \"Misc Ret Yard\" + \"Int Ret Yard\" + \"Kickoff Ret Yard\" + \"Rec Yards\" + \"Punt Ret Yard\" + \"Kickoff Yard\" + \"Fum Ret Yard\"),2) as \"Yards per Game\",\n" +
                "    SUM(\"Pass Yard\" + \"Rush Yard\" + \"Punt Yard\" + \"Misc Ret Yard\" + \"Int Ret Yard\" + \"Kickoff Ret Yard\" + \"Rec Yards\" + \"Punt Ret Yard\" + \"Kickoff Yard\" + \"Fum Ret Yard\") as \"Total Yards\",\n" +
                "    SUM(\"Rush Yard\") as \"Rush Yards Total\",\n" +
                "    SUM(\"Pass Yard\") as \"Pass Yards Total\",\n" +
                "    SUM(\"Int Ret Yard\") as \"Interception Ret Yards\",\n" +
                "    SUM(\"Rec Yards\") as \"Pass Received Yards\",\n" +
                "    SUM(\"Kickoff Ret Yard\") as \"Kickoff Ret Yards\",\n" +
                "    SUM(\"Punt Ret Yard\") as \"Punt Ret Yards\",\n" +
                "    SUM(\"Fum Ret Yard\") as \"Fumble Ret Yards\",\n" +
                "    SUM(\"Misc Ret Yard\") as \"Other Yards\",\n" +
                "    --Kicker Stats\n" +
                "    SUM(\"Kickoff Yard\") as \"Total Kickoff Yards\",\n" +
                "    round(avg(\"Kickoff Yard\"),1) as \"Kickoff Yards Per Game\",\n" +
                "    round(cast(cast(SUM(\"Field Goal Made\") as float4) / (CASE cast(SUM(\"Field Goal Att\") as float4) WHEN 0 THEN 1 ELSE  cast(SUM(\"Pass Att\") as float4) END) as numeric)*100.00,1) as \"Field Goal Completion Rate\",\n" +
                "    --Defense stats\n" +
                "    round(avg(\"Sack Yard\"),1) as \"Sack Yards per Game\",\n" +
                "    SUM(\"Sack Yard\") as \"Total Sack Yards\",\n" +
                "    round(avg(\"Fumble Forced\")) as \"Fumbles forced per Game\",\n" +
                "    SUM(\"Fumble Forced\") as \"Total Fumbles forced\",\n" +
                "    round(avg(\"Pass Broken Up\"),1) as \"Passes Broken per Game\",\n" +
                "    SUM(\"Pass Broken Up\") as \"Total Passes broken\",\n" +
                "    round(avg(\"Kick/Punt Blocked\"),1) as \"Kick/Punt Blocked\",\n" +
                "    SUM(\"Kick/Punt Blocked\") as \"Total Kick/Punts Blocked\",\n" +
                "    round(avg(\"QB Hurry\"),1) as \"QB Hurries per Game\",\n" +
                "    SUM(\"QB Hurry\") AS \"Total QB Hurries\",\n" +
                "    round(avg(\"Tackle Assist\" + \"Tackle Solo\")) as \"Tackles per Game\",\n" +
                "    SUM(\"Tackle Solo\" + \"Tackle Assist\") as \"Total Tackles\"\n";
        //query with stats requested for all seasons
        String overAllQuery = statsQueryrequested + "        FROM \"Player Game Stats\" WHERE \"Player Code\" = %1$s;";
        overAllQuery = String.format(overAllQuery, id);
        //System.out.println(overAllQuery);
        try {
            FileWriter jsonFile = new FileWriter(config.playerStatsRequestFile);
            JSONObject fileObject = new JSONObject();
            // Creating vector for all seasons of player////////////////////
            while(seasonsResponse.next()) {
                String season  = seasonsResponse.getString("Season");
                seasons.add(season);
            }
            fileObject.put("Seasons",seasons);
            fileObject.put("First Name",firstName);
            fileObject.put("Last Name",lastName);
            fileObject.put("Home Town",homeTown);
            //overAllQuery = overAllQuery.substring(0, overAllQuery.length() - 2) + ") and \"players\".\"Season\" = \"games\".\"season\";";
            //System.out.println(overAllQuery);
            // Requesting overall stats over seasons for a certain players
            ResultSet statsResponse = this.executeQuery(overAllQuery);
            statsResponse.next();
            fileObject.put("Overall",parseQuery(statsResponse));
            //query needed for each season
            String seasonQueryStatsBase= statsQueryrequested + "    FROM\n" +
                    "      (SELECT \"Game Code\" FROM\n" +
                    "        (SELECT \"Game Code\" FROM \"Player Game Stats\" where \"Player Code\" = %1$s) as  GameCodes\n" +
                    "            JOIN games ON GameCodes.\"Game Code\" = \"games\".id WHERE \"season\" = %2$s) as\n" +
                    "                SeasonGames JOIN \"Player Game Stats\" ON SeasonGames.\"Game Code\" = \"Player Game Stats\".\"Game Code\" WHERE \"Player Code\" =  %1$s;";
            //requesting all stats
            String playerTeam = "SELECT \"name\", \"Height\",\"Weight\" FROM players JOIN teams t on players.\"Season\" = t.season and players.\"Team Code\" = t.id and players.\"id\" = %1$s WHERE \"Season\"=%2$s;";
            for(String season :seasons) {
                String currSeasonquery = String.format(seasonQueryStatsBase, id,season);
                //System.out.println(currSeasonquery);
                String seasonNamequery = String.format(playerTeam, id,season);
                statsResponse = this.executeQuery(seasonNamequery);
                statsResponse.next();
                String height = statsResponse.getString("Height");
                String weight  = statsResponse.getString("Weight");
                String teamName = statsResponse.getString("name");
                //System.out.println(teamName);
                statsResponse = this.executeQuery(currSeasonquery);
                statsResponse.next();
                //System.out.println(currSeasonquery);
                JSONObject queryObject =  parseQuery(statsResponse);
                queryObject.put("Team Name",teamName);
                queryObject.put("Height",height);
                queryObject.put("Weight", weight);
                fileObject.put(season,queryObject);

                //System.out.println(rushTD);
            }
            jsonFile.write("var query = ");
            jsonFile.write(fileObject.toString());
            jsonFile.write(";");
            jsonFile.close();
        } catch (SQLException | IOException | JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.SportizaPrint("Requestfor PLayer; " + "id: "   + id + ", First Name: " + firstName + ", Last Name: " + lastName + " Completed!\n");

    }

    //team form request to database,
    public void teamFormRequest(String teamName, String conferenceName, String subdivision){
        //formatting strings inputs for SQL command

        if(!teamName.equals("NULL")) {
            teamName = teamName.replace("\'","\'\'");
            teamName = String.format("\'%s\'", teamName);
        }
        if(!conferenceName.equals("NULL")) {
            conferenceName = conferenceName.replace("\'","\'\'");
            conferenceName = String.format("\'%s\'", conferenceName);
        }
        if(!subdivision.equals("NULL")) {
            subdivision = subdivision.replace("\'","\'\'");
            subdivision = String.format("\'%s\'", subdivision);
        }
        //query to get all teams formed by query
        String teamFormQuery = "SELECT DISTINCT ON (t.id) conf.\"name\" as \"Conference Name\", t.name as \"Team Name\", conf.subdivision,t.id\n" +
                "    from teams t INNER JOIN conferences conf\n" +
                "        ON conf.id =  t.conference\n" +
                "            WHERE\n" +
                "                t.name = COALESCE(%1$s,t.name)\n" +
                "                AND conf.name = COALESCE(%2$s,conf.name)\n" +
                "                AND conf.subdivision=  COALESCE(%3$s,conf.subdivision);";
        //grabbing all inputs into query
        teamFormQuery = String.format(teamFormQuery,teamName,conferenceName,subdivision);
        //System.out.println(teamFormQuery);
        this.SportizaPrint("Requesting Teams with Parameters: Team Name: " + teamName + ", Conference: " + conferenceName + ", Subdivision: " + subdivision);
        try {
            FileWriter jsonFile = new FileWriter(config.teamFormRequest);
            JSONArray fileObject = this.parseQueryasList(this.executeQuery(teamFormQuery));
            jsonFile.write("var query = ");
            jsonFile.write(fileObject.toString());
            jsonFile.write(";");
            jsonFile.close();
        } catch (IOException | JSONException | SQLException e) {
            e.printStackTrace();
        }
        this.SportizaPrint("Request for Teams with Parameters: Team Name: " + teamName + ", Conference: " + conferenceName + ", Subdivision: " + subdivision + ". Completed\n" );

    }
    //team game stats request to database
    public void teamGameStatsRequest(String teamId, String teamName){
        this.SportizaPrint("Requesting Stats for team: " + teamName);
        ArrayList<String> seasons = this.getTeamActiveSeasons(teamId);
        String StatsRequested = "SELECT\n" +
                "    round(cast(cast(SUM(\"Pass Comp\") as float4) / (CASE cast(SUM(\"Pass Att\") as float4) WHEN 0 THEN 1 ELSE  cast(SUM(\"Pass Att\") as float4) END) as numeric)*100.00,1) as \"Pass Completion Rate\",\n" +
                "    --Touchdowns stats\n" +
                "    ROUND(avg(\"Pass TD\" + \"Rush TD\"  + \"Kickoff Ret TD\"  + \"Punt Ret TD\" + \"Fum Ret TD\" + \"Int Ret TD\" + \"Misc Ret TD\"),2) as \"TD Average per Game\",\n" +
                "    SUM(\"Pass TD\" + \"Rush TD\"  + \"Kickoff Ret TD\"  + \"Punt Ret TD\" + \"Fum Ret TD\" + \"Int Ret TD\" + \"Misc Ret TD\") as \"Total TD\",\n" +
                "    SUM(\"Rush TD\") as \"Rush TD Total\",\n" +
                "    SUM(\"Pass TD\") as \"Pass TD Total\",\n" +
                "    SUM(\"Int Ret TD\") as \"Interception Ret TD\",\n" +
                "    SUM(\"Kickoff Ret TD\") as \"Kickoff Ret TD\",\n" +
                "    SUM(\"Punt Ret TD\") as \"Punt Ret TD\",\n" +
                "    SUM(\"Fum Ret TD\") as \"Fumble Ret TD\",\n" +
                "    SUM(\"Misc Ret TD\") as \"Other TD\",\n" +
                "    --yards stats\n" +
                "    ROUND(avg(\"Pass Yard\" + \"Rush Yard\" + \"Punt Yard\" + \"Misc Ret Yard\" + \"Int Ret Yard\" + \"Kickoff Ret Yard\" +  \"Punt Ret Yard\" + \"Kickoff Yard\" + \"Fum Ret Yard\"),2) as \"Yards per Game\",\n" +
                "    SUM(\"Pass Yard\" + \"Rush Yard\" + \"Punt Yard\" + \"Misc Ret Yard\" + \"Int Ret Yard\" + \"Kickoff Ret Yard\" +  \"Punt Ret Yard\" + \"Kickoff Yard\" + \"Fum Ret Yard\") as \"Total Yards\",\n" +
                "    SUM(\"Rush Yard\") as \"Rush Yards Total\",\n" +
                "    SUM(\"Pass Yard\") as \"Pass Yards Total\",\n" +
                "    SUM(\"Int Ret Yard\") as \"Interception Ret Yards\",\n" +
                "    SUM(\"Kickoff Ret Yard\") as \"Kickoff Ret Yards\",\n" +
                "    SUM(\"Punt Ret Yard\") as \"Punt Ret Yards\",\n" +
                "    SUM(\"Fum Ret Yard\") as \"Fumble Ret Yards\",\n" +
                "    SUM(\"Misc Ret Yard\") as \"Other Yards\",\n" +
                "    --KickingS Stats and Extra Point Conversions\n" +
                "    SUM(\"Kickoff Yard\") as \"Total Kickoff Yards\",\n" +
                "    SUM(\"Field Goal Made\") as \"Total Field Goals Made\",\n" +
                "    SUM(\"Off XP Kick Made\") as \"Total Extra Point Kicks Made\",\n" +
                "    SUM(\"Off 2XP Made\" + \"Def 2XP Made\") as \"Total 2-Point Conversions Made\",\n" +
                "    round(avg(\"Kickoff Yard\"),1) as \"Kickoff Yards Per Game\",\n" +
                "    round(cast(cast(SUM(\"Field Goal Made\") as float4) / (CASE cast(SUM(\"Field Goal Att\") as float4) WHEN 0 THEN 1 ELSE  cast(SUM(\"Field Goal Att\") as float4) END) as numeric)*100.00,1) as \"Field Goal Completion Rate\",\n" +
                "    round(cast(cast(SUM(\"Off 2XP Made\" + \"Def 2XP Made\") as float4) / (CASE cast(SUM(\"Off 2XP Att\" + \"Def 2XP Att\") as float4) WHEN 0 THEN 1 ELSE  cast(SUM(\"Off 2XP Att\" + \"Def 2XP Att\") as float4) END) as numeric)*100.00,1) as \"2-Point Conversion Completion Rate\",\n" +
                "    round(cast(cast(SUM(\"Off XP Kick Made\") as float4) / (CASE cast(SUM(\"Off XP Kick Att\") as float4) WHEN 0 THEN 1 ELSE  cast(SUM(\"Off XP Kick Att\") as float4) END) as numeric)*100.00,1) as \"Extra-Point Kick Completion Rate\",\n" +
                "    --Defense stats\n" +
                "    round(avg(\"Sack Yard\"),1) as \"Sack Yards per Game\",\n" +
                "    SUM(\"Sack Yard\") as \"Total Sack Yards\",\n" +
                "    round(avg(\"Fumble Forced\")) as \"Fumbles forced per Game\",\n" +
                "    SUM(\"Fumble Forced\") as \"Total Fumbles forced\",\n" +
                "    round(avg(\"Pass Broken Up\"),1) as \"Passes Broken per Game\",\n" +
                "    SUM(\"Pass Broken Up\") as \"Total Passes broken\",\n" +
                "    round(avg(\"Kick/Punt Blocked\"),1) as \"Kick/Punt Blocked\",\n" +
                "    SUM(\"Kick/Punt Blocked\") as \"Total Kick/Punts Blocked\",\n" +
                "    round(avg(\"QB Hurry\"),1) as \"QB Hurries per Game\",\n" +
                "    SUM(\"QB Hurry\") AS \"Total QB Hurries\",\n" +
                "    round(avg(\"Tackle Assist\" + \"Tackle Solo\"),1) as \"Tackles per Game\",\n" +
                "    SUM(\"Tackle Solo\" + \"Tackle Assist\") as \"Total Tackles\",\n" +
                "    --Penalties and plays Stats\n" +
                "    SUM(\"Penalty\") as \"Total Penalties\",\n" +
                "    SUM(\"Penalty Yard\") as \"Total Penalty Yards\",\n" +
                "    round(cast(cast(SUM(\"Fourth Down Conv\") as float4) / (CASE cast(SUM(\"Fourth Down Att\") as float4) WHEN 0 THEN 1 ELSE  cast(SUM(\"Fourth Down Att\") as float4) END) as numeric)*100.00,1) as \"Fourth Down Conversion Completion Rate\",\n" +
                "    round(cast(cast(SUM(\"Third Down Conv\") as float4) / (CASE cast(SUM(\"Third Down Att\") as float4) WHEN 0 THEN 1 ELSE  cast(SUM(\"Third Down Att\") as float4) END) as numeric)*100.00,1) as \"Third Down Conversion Completion Rate\",\n" +
                "    round(avg(\"Penalty\"),1) as \"Penalties Average per Game\",\n" +
                "    round(avg(\"Penalty Yard\"),1) as \"Penalty Yards Average per Game\",\n" +
                "    round(avg(\"Time Of Possession\"),1) as \"Possession Time Average per Game\"";
        String overAllStatsQuery = StatsRequested + "\n" + "    FROM \"Game Team Stats\" WHERE \"Team Code\" =  %1$s;";
        overAllStatsQuery = String.format(overAllStatsQuery, teamId);
        String seasonStatsBaseQuery = StatsRequested + "\n" + "    FROM\n" +
                "      (SELECT \"Game Code\" FROM\n" +
                "        (SELECT \"Game Code\" FROM \"Game Team Stats\" where \"Team Code\" = 697) as  GameCodes\n" +
                "            JOIN games ON GameCodes.\"Game Code\" = \"games\".id WHERE \"season\" = %1$s) as\n" +
                "                SeasonGames JOIN \"Game Team Stats\" ON SeasonGames.\"Game Code\" = \"Game Team Stats\".\"Game Code\" WHERE \"Team Code\" =  %2$s;";
        //System.out.println(seasonStatsBaseQuery);
        //System.out.println(overAllStats);


        try {
            FileWriter jsonFile = new FileWriter(config.teamStatsRequestFile);
            JSONObject fileObject = new JSONObject();
            //Meta Data
            fileObject.put("Team Name", teamName);
            fileObject.put("Seasons", seasons);
            //Adding OverALl stats for team
            ResultSet overAllStats = this.executeQuery(overAllStatsQuery);
            overAllStats.next();
            fileObject.put("Overall",this.parseQuery(overAllStats));

            for(String season: seasons){
                String currSeasonQuery = String.format(seasonStatsBaseQuery,season, teamId);
                //System.out.println(currSeasonQuery);
                String conferenceName = this.getConferenceName(teamId,season);
                ResultSet seasonStatsResponse = this.executeQuery(currSeasonQuery);
                seasonStatsResponse.next();
                JSONObject seasonObject = this.parseQuery(seasonStatsResponse);
                seasonObject.put("Conference",conferenceName);
                fileObject.put(season,seasonObject);
                //System.out.println(conferenceName);
            }
            ArrayList<String> mostStats = new ArrayList<>(Arrays.asList("Rush Yard", "Pass Yard", "Points", "Sack","Rush TD","Pass TD"));
            for(String stat:mostStats){
                fileObject.put("Most " + stat,this.mostStatsAgainstTeam(teamId,stat));
            }
            jsonFile.write("var teamStatsData = ");
            jsonFile.write(fileObject.toString());
            jsonFile.write(";");
            jsonFile.close();

        } catch (IOException | JSONException | SQLException e) {
            e.printStackTrace();
        }
        this.SportizaPrint("Request of Stats for Team: " + teamName + " Completed!\n");
    }
    

    //parse all lines from query and create a json object from each line in the response based on its keys
    private JSONArray parseQueryasList(ResultSet response) throws SQLException, JSONException {
        JSONArray fileObject = new JSONArray();
        //getting keys from response
        ArrayList<String>  keys = new ArrayList<String>();
        ResultSetMetaData rsmd = response.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++ ) {
            keys.add(rsmd.getColumnName(i));
        }
        //adding objects to jsonArray
        while(response.next()){
            JSONObject jsonObj = new JSONObject();
            for(String key: keys){
                String value = response.getString(key);
                if(response.wasNull()){
                    jsonObj.put(key,"NULL");
                }else{
                    jsonObj.put(key,value);
                }
            }
            fileObject.put(jsonObj);
        }
        return fileObject;
    }
    //parses a Resultset from names given in the response as columns
    private JSONObject parseQuery(ResultSet statsQueryResponse) throws SQLException, JSONException {
        JSONObject seasonStats = new JSONObject();
        //getting keys from response
        ArrayList<String>  keys = new ArrayList<String>();
        ResultSetMetaData rsmd = statsQueryResponse.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++ ) {
            keys.add(rsmd.getColumnName(i));
        }
        for(String key: keys){
            String value = statsQueryResponse.getString(key);
            if(statsQueryResponse.wasNull()){
                seasonStats.put(key,"NULL");
            }else{
                seasonStats.put(key,value);
            }
        }
        //System.out.println(keys);
        return seasonStats;
    }
    //execute a query
    private ResultSet executeQuery(String query) {
        ResultSet response = null;
        try {
            Statement st = conn.createStatement();
            response =  st.executeQuery(query);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }

    //determines the winning and losing team given a game code
    private String[]getWinningTeam(String GameCode){
        String winnerQuery = "SELECT \"Team Code\", \"Points\" FROM \"Game Team Stats\" WHERE \"Game Code\" = %1$s;";
        winnerQuery = String.format(winnerQuery,GameCode);
        String team1 ="";
        String team2 ="";
        int team1Score;
        int team2Score;

        String winningTeam ="";
        String defeatedTeam = "";
        ResultSet response = this.executeQuery(winnerQuery);
        try {
            response.next();
            team1 = response.getString("Team Code");
            team1Score = response.getInt("Points");
            response.next();
            team2 = response.getString("Team Code");
            team2Score = response.getInt("Points");
            if(team1Score > team2Score){
                winningTeam = team1;
                defeatedTeam = team2;
            }else{
                winningTeam = team2;
                defeatedTeam = team1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Winning Team: " + winningTeam);
        String[] teams = new String[2];
        teams[0] = winningTeam;
        teams[1] = defeatedTeam;
        System.out.println("Winner: " + winningTeam + ", Loser: " + defeatedTeam);
        return teams;
    }
    //this updates te game for a specific game code updating the winning team column and defeated team column
    private void updateGameWinningTeam(String GameCode, String winningTeam, String defeatedTeam) {
        String updateQuery = "UPDATE \"games Copy\"\n" +
                "    set \"Winning Team\" = '%1$s',\n" +
                "        \"Defeated Team\" = '%2$s'\n" +
                "        WHERE id = '%3$s';";
        updateQuery = String.format(updateQuery,winningTeam,defeatedTeam,GameCode);
        try {
            PreparedStatement st = conn.prepareStatement(updateQuery);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //get game codes from database from games table
    private ArrayList<String> getAllGameCodes() throws SQLException {
        ArrayList<String> gameCodes = new ArrayList<String>();
        ResultSet response = this.executeQuery("SELECT id from \"games Copy\";");
        while (response.next()){
            gameCodes.add(response.getString("id"));
        }
        System.out.println(gameCodes.size());
        return gameCodes;
    }
    //this updates weather a team won or lose on a game on the games table
    public void updateAllWinningTeams(){
        ArrayList<String> gameCodes = null;
        try {
            gameCodes = this.getAllGameCodes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(String gameCode: gameCodes){
            String[] teams = getWinningTeam(gameCode);
            String winner = teams[0];
            String loser = teams[1];
            this.updateGameWinningTeam(gameCode,winner,loser);
        }
    }
    //adding teams Ids to winning directed graph
    private void addTeamIdToGraph(){
        String teamQuery = "SELECT DISTINCT ON(\"id\") \"id\" FROM teams;";
        ResultSet teamsResponse = this.executeQuery(teamQuery);
        try {
            while(teamsResponse.next()){
                String teamId = teamsResponse.getString("id");
                this.winningDirectedGraph.addVertex(teamId);
                //System.out.println(teamId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println("Number of vertices: " + this.winningDirectedGraph.vertexSet().size());
    }
    //adds all the edges to winning team graph
    private void addWinEdgesToGraph(){
        String gamesQuery = "SELECT \"Winning Team\", \"Defeated Team\", \"season\" FROM \"games Copy\";\n";
        ResultSet gamesResponse = this.executeQuery(gamesQuery);
        int count= 0;
        try {

            while(gamesResponse.next()){
                count++;
                String winningTeam = gamesResponse.getString("Winning Team");
                String defeatedTeam = gamesResponse.getString("Defeated Team");
                int season = gamesResponse.getInt("season");
                winningDirectedGraph.addEdge(winningTeam,defeatedTeam,new NamedEdge(season,winningTeam,defeatedTeam));
                //System.out.println(winningTeam + ", " +  defeatedTeam + ", " +  season);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println("Total Number of Edges(Games): " + winningDirectedGraph.edgeSet().size());
    }

    //loading and populating graph
    private void loadWinningGraph(){
        this.addTeamIdToGraph();
        this.addWinEdgesToGraph();
    }
    //shortest victory chain given a source and target id of the team
    public void shortestVictoryChainRequest(String sourceID, String targetID){
        int edgesForTeam = this.winningDirectedGraph.outDegreeOf(sourceID);
        //System.out.println("Edges for Texas A&M University: " + edgesForTeam);
        this.SportizaPrint("Determining Shortest Victory Chain from Team: " + this.getTeamName(sourceID) + " to " + this.getTeamName(targetID));
        DijkstraShortestPath<String, DefaultEdge> path = new DijkstraShortestPath(winningDirectedGraph);
        GraphPath pathShort = path.getPath(sourceID,targetID);
        List<NamedEdge> edges = pathShort.getEdgeList();
        List<String> vertices = pathShort.getVertexList();


        JSONObject GraphObject = new JSONObject();
        JSONArray graphVertices = new JSONArray();
        JSONArray graphEdges = new JSONArray();
        //populating vertices JSON array
        for(String vertice: vertices){
            JSONObject vertexObject = new JSONObject();
            String teamID = vertice;
            String teamName = this.getTeamName(teamID);
            try {
                vertexObject.put("id",teamID);
                vertexObject.put("label",teamName);
                vertexObject.put("size",3);
                graphVertices.put(vertexObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //System.out.println(vertice);
        }
        //populating edges array
        for(NamedEdge e: edges){
           JSONObject edgeObject = new JSONObject();
           String sourceNode = e.getWinningTeam();
           String targetNode = e.getDefeatedTeam();
           String edgeID = e.getWinningTeam() + "-" + Integer.toString(e.season()) + "-" + e.getDefeatedTeam();
           String label = Integer.toString(e.season());
            try {
                edgeObject.put("id", edgeID);
                edgeObject.put("source",sourceNode);
                edgeObject.put("target",targetNode);
                edgeObject.put("type","arrow");
                graphEdges.put(edgeObject);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        FileWriter jsonFile = null;
        try {
            jsonFile = new FileWriter(config.shortestVictoryChain);
            GraphObject.put("nodes",graphVertices);
            GraphObject.put("edges",graphEdges);
            jsonFile.write("var graphData = ");
            jsonFile.write(GraphObject.toString());
            jsonFile.write(";");
            jsonFile.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        this.SportizaPrint("Shortest Victory Chain Determined from Team: " + this.getTeamName(sourceID) + " to " + this.getTeamName(targetID) + "\n");
    }

    //get a team name based on its mapping id
    private String getTeamName(String teamID){
        String TeamQuery = "SELECT DISTINCT ON(\"id\") \"name\" FROM teams WHERE id = %1$s;";
        TeamQuery = String.format(TeamQuery,teamID);
        ResultSet teamNameResponse = this.executeQuery(TeamQuery);
        String teamName = "";
        try {
            teamNameResponse.next();
            teamName = teamNameResponse.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teamName;

    }
    //returns name of conference for a team given its team id and season
    private String getConferenceName(String teamID, String season){
        String query = "SELECT t.\"id\", c.\"name\" \n" +
                "    FROM teams t \n" +
                "        JOIN conferences c \n" +
                "            ON t.conference = c.id \n" +
                "        WHERE t.id = %1$s  AND t.season = %2$s;";
        query = String.format(query,teamID,season);
        ResultSet response = this.executeQuery(query);
        String conferenceName = "";
        try {
            response.next();
            conferenceName = response.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conferenceName;
    }
    private ArrayList<String> getTeamActiveSeasons(String teamID){
        ArrayList<String> seasons = new ArrayList<String>();
        String query = "SELECT DISTINCT /*\"games\".\"id\",*/ \"games\".\"season\" /*\"players\".\"First Name\", \"players\".\"Last Name\"*/\n" +
                "    FROM \"Game Team Stats\"\n" +
                "        JOIN \"teams\"\n" +
                "            ON \"Game Team Stats\".\"Team Code\" = \"teams\".\"id\" and \"teams\".\"id\" = %1$s\n" +
                "        JOIN \"games\"\n" +
                "            ON \"games\".\"id\" = \"Game Team Stats\".\"Game Code\" ORDER BY \"season\" DESC;";
        query = String.format(query,teamID);
        ResultSet response = this.executeQuery(query);
        try {
            while(response.next()){
                String s = response.getString("season");
                seasons.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seasons;
    }
    //format printing
    private void SportizaPrint(String message){
        System.out.println("Sportiza/---->" + message);
    }
    //given a named stat, and a team ID, it determines the top 3 teams with most of those stats against the give team id
    private JSONArray mostStatsAgainstTeam(String targetTeamID, String statName){
        String statQuery = "SELECT\"Team Code\", \"%2$s\",\"season\"\n" +
                "    FROM\n" +
                "         (SELECT \"id\",\"season\" FROM \"games Copy\" WHERE \"Winning Team\" = %1$s or \"Defeated Team\" = %1$s) as allGames\n" +
                "             INNER\n" +
                "                     JOIN \"Game Team Stats\" GTS ON allGames.id = GTS.\"Game Code\" WHERE \"Team Code\" != %1$s ORDER BY \"Rush Yard\" DESC LIMIT 3;";
        statQuery = String.format(statQuery,targetTeamID,statName);
        //SportizaPrint(statQuery);
        ResultSet mostStatsResp = this.executeQuery(statQuery);
        JSONArray mostStatsObject = new JSONArray();
        try {
            int rank  = 1;
            while(mostStatsResp.next()){
                JSONObject mostTeamStats = new JSONObject();
                String season = mostStatsResp.getString("season");
                String teamName = this.getTeamName(mostStatsResp.getString("Team Code"));
                String statsNumber = mostStatsResp.getString(statName);
                mostTeamStats.put("Team Name", teamName);
                mostTeamStats.put(statName, statsNumber);
                mostTeamStats.put("rank", rank);
                mostTeamStats.put("season",season);
                rank++;
                mostStatsObject.put(mostTeamStats);

            }
        } catch (SQLException | JSONException e) {
            e.printStackTrace();
        }
        return mostStatsObject;
    }
    private JSONArray hometownsWithMostPositions(String position){
        String query = "SELECT COUNT(*) as \"Player Count\", \"Home Town\"\n" +
                "    FROM\n" +
                "         (SELECT DISTINCT ON(\"id\") * FROM players WHERE \"Home Town\" IS NOT NULL ) AS Players\n" +
                "            WHERE \"Position\" = '%1$s' GROUP BY \"Home Town\" ORDER BY \"Player Count\" DESC LIMIT 3;";
        query = String.format(query,position);
        JSONArray mostPosition = new JSONArray();
        ResultSet response = this.executeQuery(query);
        try {
            int rank = 1;
            while(response.next()){
                JSONObject homeTownObject = new JSONObject();
                String HomeTown = response.getString("Home Town");
                String playerCount = response.getString("Player Count");
                //SportizaPrint("Home Town: " + HomeTown + ", Player Count: " + playerCount + ", Rank: " + rank);
                homeTownObject.put("Home Town",HomeTown);
                homeTownObject.put("Player Count", playerCount);
                homeTownObject.put("Rank", rank);
                mostPosition.put(homeTownObject);
                rank++;
            }
        } catch (SQLException | JSONException e) {
            e.printStackTrace();
        }
        return mostPosition;
    }
    private JSONArray homeTownWithMostStats(String statName){
        String query = "SELECT SUM(\"%1$s\") AS StatSum,\"Home Town\" FROM\n" +
                "        (SELECT DISTINCT ON(\"id\") id, \"Home Town\" FROM players WHERE \"Home Town\" IS NOT NULL )\n" +
                "            AS Players JOIN \"Player Game Stats\" ON Players.\"id\"  = \"Player Code\" GROUP BY \"Home Town\" ORDER BY StatSum DESC LIMIT 3;";
        query = String.format(query,statName);
        //SportizaPrint(query);
        JSONArray teamsWithMostStat = new JSONArray();
        ResultSet response = this.executeQuery(query);
        try {
            int rank = 1;
            while (response.next()){
                JSONObject homeTownObject = new JSONObject();
                String HomeTown = response.getString("Home Town");
                String StatSum = response.getString("StatSum");
                //SportizaPrint("Home Town: " + HomeTown + ", Player Count: " +statName + ": " + StatSum + ", Rank: " + rank);
                homeTownObject.put("Home Town",HomeTown);
                homeTownObject.put(statName, StatSum);
                homeTownObject.put("Rank", rank);
                teamsWithMostStat.put(homeTownObject);
                rank++;

            }
        } catch (SQLException | JSONException e) {
            e.printStackTrace();
        }

        return teamsWithMostStat;
    }
    private JSONArray hometownsWithMostPlayers(){
        String query = "SELECT COUNT(*) as \"Player Count\",\"Home Town\" FROM (SELECT DISTINCT ON(\"id\") * FROM players WHERE \"Home Town\" IS NOT NULL ) AS Players GROUP BY  \"Home Town\" ORDER BY \"Player Count\" DESC  LIMIT 3;";
        JSONArray MostPlayersTowns = new JSONArray();
        ResultSet response = this.executeQuery(query);
        try {
            int rank = 1;
            while(response.next()){
                JSONObject homeTownMost = new JSONObject();
                String HomeTownName = response.getString("Home Town");
                String playerNum = response.getString("Player Count");
                homeTownMost.put("Home Town", HomeTownName);
                homeTownMost.put("Player Count", playerNum);
                homeTownMost.put("Rank", rank);
                MostPlayersTowns.put(homeTownMost);
                //SportizaPrint("Home Town: " + HomeTownName + ", Player Count: " + playerNum + ", Rank: " + Integer.toString(rank ));
                rank++;

            }
        } catch (SQLException | JSONException e) {
            e.printStackTrace();
        }
        return MostPlayersTowns;

    }
    //determines all top 3  home twons with most ststs such as player positions, player stats, and Number of Players coming from Home Towns
    void homeTownsWithMostStats(){
        ArrayList<String> playerPositions = new ArrayList<>(Arrays.asList("ATH","C","CB","DB","DE","DL","DS","DT","FB","FL","FS","HB","HOLD","ILB","K","LB","LS","MLB","NG","NT","OG","OL","OLB","OT","P","PK","QB","RB","ROV","RV","S","SB","SE","SLB","SN","SS","TB","TE","WLB","WR"));
        ArrayList<String> statNames= new ArrayList<String>(Arrays.asList("Pass Yard","Rush Yard", "Pass TD", "Rush TD", "Points","Sack","Safety", "Field Goal Made"));
        try {

            FileWriter jsonFile = new FileWriter(config.homeTownStatsRanks);
            JSONObject fileObject = new JSONObject();
            //Adding all objects of most players for a Home
            fileObject.put("Most Players",this.hometownsWithMostPlayers());
            //Adding all different top home town with most players
            for(String position : playerPositions){
                fileObject.put("HomeTowns Most Position " + position, this.hometownsWithMostPositions(position));
            }
            //Adding all most STats Home towns
            for(String statName: statNames){
                fileObject.put("HomeTowns Most Stats " + statName ,this.homeTownWithMostStats(statName));
            }
            jsonFile.write("var query = ");
            jsonFile.write(fileObject.toString());
            jsonFile.write(";");
            jsonFile.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        //Adding all objects of most players for a Home

    }


}
