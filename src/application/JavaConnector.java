package application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class JavaConnector {
  Connection conn;

  // setting up connector to database during constructor
  JavaConnector(String username, String credential, String database) {
    try {
      Class.forName("org.postgresql.Driver");
      conn = DriverManager.getConnection(database, username, credential);
      System.out.println("Connected to sportizadevspace database!");
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  //request
  public void playerFormRequest(String FirstName, String LastName, String Team, String UniformNumber, String HomeTown) {
    //formating strings inputs for SQL command
    if (!FirstName.equals("NULL")) {
      FirstName = FirstName.replace("\'", "\'\'");
      FirstName = String.format("\'%s\'", FirstName);
    }
    if (!LastName.equals("NULL")) {
      LastName = LastName.replace("\'", "\'\'");
      LastName = String.format("\'%s\'", LastName);
    }
    if (!Team.equals("NULL")) {
      Team = Team.replace("\'", "\'\'");
      Team = String.format("\'%s\'", Team);
    }
    if (!UniformNumber.equals("NULL")) {
      UniformNumber = UniformNumber.replace("\'", "\'\'");
      UniformNumber = String.format("\'%s\'", UniformNumber);
    }
    if (!HomeTown.equals("NULL")) {
      HomeTown = HomeTown.replace("\'", "\'\'");
      HomeTown = String.format("\'%s\'", HomeTown);
    }
    //base query format
    String query = "SELECT DISTINCT ON (playerChosen.\"id\") playerChosen.\"id\", \"First Name\",\"Last Name\",\"Home Town\",\"Home Country\",\"Home State\",\"Position\", \"name\" FROM  (SELECT DISTINCT ON (\"players\".\"id\")  \"players\".\"id\",\"players\".\"First Name\", \"players\".\"Last Name\",\"players\".\"Position\", \"players\".\"Home Town\", \"players\".\"Home State\", \"players\".\"Home Country\",\"players\".\"Team Code\"\n" +
            "    FROM players\n" +
            "        WHERE \"First Name\" =  COALESCE(%2$s,\"players\".\"First Name\")\n" +
            "               and \"Last Name\" = COALESCE(%3$s,\"players\".\"Last Name\")\n" +
            "               and (\"players\".\"Uniform Number\" = COALESCE(%4$s, \"players\".\"Uniform Number\"))\n" +
            "               and (\"players\".\"Home Town\" = COALESCE(%5$s, \"players\".\"Home Town\")))as playerChosen\n" +
            "        INNER JOIN \"teams\"\n" +
            "            ON \"teams\".\"id\" = playerChosen.\"Team Code\" WHERE \"teams\".\"name\" = COALESCE(%1$s, \"teams\".\"name\");\n";
    if (HomeTown.equals("NULL")) {
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
    query = String.format(query, Team, FirstName, LastName, UniformNumber, HomeTown);
    //response for executed Query
    System.out.println(query);
    ResultSet response = this.executeQuery(query);
    try {
      FileWriter outputFile = new FileWriter(config.requestPlayerFormFile);
      //JSONObject fileObject = new JSONObject();
      JSONArray fileObject = new JSONArray();
      while (response.next()) {
        String playerID = response.getString("id");
        String playerName = response.getString("First Name");
        if (response.wasNull()) {
          //System.out.println("Player Name is null");
          playerName = "N/A";
        }
        String playerLastName = response.getString("Last Name");
        if (response.wasNull()) {
          //System.out.println("Player Last Name is null");
          playerLastName = "N/A";
        }
        String playerTown = response.getString("Home Town");
        if (response.wasNull()) {
          //System.out.println("playerTown is null");
          playerTown = "N/A";
        }
        String position = response.getString("Position");
        if (response.wasNull()) {
          //System.out.println("position is null");
          position = "N/A";
        }
        String playerState = response.getString("Home State");
        if (response.wasNull()) {
          //System.out.println("playerState is null");
          playerState = "N/A";
        }
        String playerCountry = response.getString("Home Country");
        if (response.wasNull()) {
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
      outputFile.write("var query = ");
      outputFile.write(fileObject.toString());
      outputFile.write(";");
      outputFile.close();
      System.out.println("Request with Parameters: First Name: " + FirstName + ", Last Name: " + LastName + ", " + "Team: " + Team + ", Uniform Number: " + UniformNumber + ", Home Town: " + HomeTown + "------completed");
    } catch (SQLException | JSONException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void playerStatsRequest(String id, String firstName, String lastName, String homeTown, String homeState, String homeCountry) {
    String seasonsQuery = "SELECT DISTINCT /*\"games\".\"id\",*/ \"games\".\"season\" /*\"players\".\"First Name\", \"players\".\"Last Name\"*/\n" +
            "    FROM \"Player Game Stats\"\n" +
            "        JOIN \"players\"\n" +
            "            ON \"Player Game Stats\".\"Player Code\" = \"players\".\"id\" and \"players\".\"id\" = %1$s\n" +
            "        JOIN \"games\"\n" +
            "            ON \"games\".\"id\" = \"Player Game Stats\".\"Game Code\";";
    seasonsQuery = String.format(seasonsQuery, id);
//		System.out.println(seasonsQuery);

    ResultSet seasonsResponse = this.executeQuery(seasonsQuery);
    ArrayList<String> seasons = new ArrayList<String>();
    String statsQueryrequested = "SELECT\n" +
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
    System.out.println(overAllQuery);
    try {
      FileWriter outputFile = new FileWriter(config.playerStatsRequestFile);
      JSONObject fileObject = new JSONObject();
      // Creating vector for all seasons of player////////////////////
      while (seasonsResponse.next()) {
        String season = seasonsResponse.getString("Season");
        seasons.add(season);
      }
      fileObject.put("Seasons", seasons);
      fileObject.put("First Name", firstName);
      fileObject.put("Last Name", lastName);
      fileObject.put("Home Town", homeTown);
      fileObject.put("Home State", homeState);
      fileObject.put("Home Country", homeCountry);
      // Requesting overall stats over seasons for a certain players
      ResultSet statsResponse = this.executeQuery(overAllQuery);
      statsResponse.next();
      fileObject.put("Overall", parseQuery(statsResponse));
      //query needed for each season
      String seasonQueryStatsBase = statsQueryrequested + "    FROM\n" +
              "      (SELECT \"Game Code\" FROM\n" +
              "        (SELECT \"Game Code\" FROM \"Player Game Stats\" where \"Player Code\" = %1$s) as  GameCodes\n" +
              "            JOIN games ON GameCodes.\"Game Code\" = \"games\".id WHERE \"season\" = %2$s) as\n" +
              "                SeasonGames JOIN \"Player Game Stats\" ON SeasonGames.\"Game Code\" = \"Player Game Stats\".\"Game Code\" WHERE \"Player Code\" =  %1$s;";
      //requesting all stats
      String playerTeam = "SELECT \"name\", \"Height\",\"Weight\" FROM players JOIN teams t on players.\"Season\" = t.season and players.\"Team Code\" = t.id and players.\"id\" = %1$s WHERE \"Season\"=%2$s;";
      for (String season : seasons) {
        String currSeasonquery = String.format(seasonQueryStatsBase, id, season);
        String seasonNamequery = String.format(playerTeam, id, season);
        statsResponse = this.executeQuery(seasonNamequery);
        statsResponse.next();
        String height = statsResponse.getString("Height");
        String weight = statsResponse.getString("Weight");
        String teamName = statsResponse.getString("name");
        statsResponse = this.executeQuery(currSeasonquery);
        statsResponse.next();
        JSONObject queryObject = parseQuery(statsResponse);
        queryObject.put("Team Name", teamName);
        queryObject.put("Height", height);
        queryObject.put("Weight", weight);
        fileObject.put(season, queryObject);
      }
      outputFile.write("var query = ");
      outputFile.write(fileObject.toString());
      outputFile.write(";");
      outputFile.close();
    } catch (SQLException | IOException | JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public JSONObject parseQuery(ResultSet statsQueryResponse) throws SQLException, JSONException {
    JSONObject seasonStats = new JSONObject();
    //getting keys from response
    ArrayList<String> keys = new ArrayList<String>();
    ResultSetMetaData rsmd = statsQueryResponse.getMetaData();
    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
      keys.add(rsmd.getColumnName(i));
    }
    for (String key : keys) {
      String value = statsQueryResponse.getString(key);
      if (statsQueryResponse.wasNull()) {
        seasonStats.put(key, "NULL");
      } else {
        seasonStats.put(key, value);
      }
    }
    System.out.println(keys);
    return seasonStats;
  }

  //execute a query
  public ResultSet executeQuery(String query) {
    ResultSet response = null;
    try {
      Statement st = conn.createStatement();
      response = st.executeQuery(query);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return response;

  }

}