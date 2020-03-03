import java.io.FileWriter;
import java.io.IOException;
import java.sql.*; 
import org.json.*;
import java.util.ArrayList;

public class connectorPostgres {
	Connection conn;
	// setting up connector to database during constructor
	connectorPostgres(String username, String credential, String database){
		try {
			Class.forName("org.postgresql.Driver");
			conn =  DriverManager.getConnection(database,username, credential);
			System.out.println("Connected to sportizadevspace database!");
		}catch(Exception e){ 
			System.out.println(e);
		} 
	}
	//request 
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
		System.out.println(query);
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
			jsonFile.write(fileObject.toString());
			jsonFile.close();
			System.out.println("Request with Parameters: First Name: " + FirstName + ", Last Name: " + LastName + ", " + "Team: " + Team + ", Uniform Number: " + UniformNumber + ", Home Town: " + HomeTown+ "------completed");
		} catch (SQLException | JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void playerStatsRequest(String id) {
		String seasonsQuery = "SELECT \"players\".\"Season\" FROM \"players\" WHERE \"players\".\"id\" = %1$s;";
		seasonsQuery = String.format(seasonsQuery, id);
//		System.out.println(seasonsQuery);
		ResultSet seasonsResponse = this.executeQuery(seasonsQuery);
		ArrayList<String> seasons = new ArrayList<String>();
		String overAllQuery = "SELECT round(avg(\"Player Game Stats\".\"Rush Yard\"),2) as \"Rush Yards\", round(avg(\"Player Game Stats\".\"Pass Yard\"),2) as \"Pass Yards\", round(avg(\"Player Game Stats\".\"Rush TD\"),2) as \"Rush TD\", round(avg(\"Player Game Stats\".\"Pass Comp\"),2) as \"Pass Comp\", round(avg(\"Player Game Stats\".\"Pass TD\"),2) as \"Pass TD\"\n" + 
				"    FROM \"Player Game Stats\"\n" + 
				"         JOIN \"players\"\n" + 
				"             ON \"Player Game Stats\".\"Player Code\" = \"players\".\"id\" and \"players\".\"id\" = %1$s\n" + 
				"         JOIN \"games\"\n" + 
				"             ON \"games\".\"id\" = \"Player Game Stats\".\"Game Code\"\n" + 
				"                    and (";
		overAllQuery = String.format(overAllQuery, id);
				
		try {
			FileWriter jsonFile = new FileWriter(config.playerStatsRequestFile);
			JSONObject fileObject = new JSONObject();
			while(seasonsResponse.next()) {
				String season  = seasonsResponse.getString("Season");
				seasons.add(season);
				overAllQuery = overAllQuery+" \"games\".\"season\" = " + season + " or"	;
			}
			overAllQuery = overAllQuery.substring(0, overAllQuery.length() - 2) + ") and \"players\".\"Season\" = \"games\".\"season\";";
			//System.out.println(overAllQuery);
			// Requesting overall stats over seasons for a certain players
			ResultSet statsResponse = this.executeQuery(overAllQuery);
			statsResponse.next();
			String rushYards= statsResponse.getString("Rush Yards");
			String passYards = statsResponse.getString("Pass Yards");
			String rushTD = statsResponse.getString("Rush TD");
			String passTD = statsResponse.getString("Pass TD");
			String passComp = statsResponse.getString("Pass Comp");
			//adding values to json object
			JSONObject overAllStats = new JSONObject();
			overAllStats.put("Rush Yards", rushYards);
			overAllStats.put("Pass Yards", passYards);
			overAllStats.put("Rush TD", rushTD);
			overAllStats.put("Pass TD", passTD);
			overAllStats.put("Pass Comp", passComp);
			fileObject.put("Overall Stats", overAllStats);
			//System.out.println(rushTD);
			String seasonQueryStatsBase= "SELECT round(avg(\"Player Game Stats\".\"Rush Yard\"),2) as \"Rush Yards\", round(avg(\"Player Game Stats\".\"Pass Yard\"),2) as \"Pass Yards\", round(avg(\"Player Game Stats\".\"Rush TD\"),2) as \"Rush TD\", round(avg(\"Player Game Stats\".\"Pass Comp\"),2) as \"Pass Comp\", round(avg(\"Player Game Stats\".\"Pass TD\"),2) as \"Pass TD\"\n" + 
					"    FROM \"Player Game Stats\"\n" + 
					"         JOIN \"players\"\n" + 
					"             ON \"Player Game Stats\".\"Player Code\" = \"players\".\"id\" and \"players\".\"id\" = %1$s\n" + 
					"         JOIN \"games\"\n" + 
					"             ON \"games\".\"id\" = \"Player Game Stats\".\"Game Code\"\n" + 
					"                    and \"games\".\"season\" = %2$s\n" + 
					"                    and \"players\".\"Season\" = \"games\".\"season\";";
			for(String season :seasons) {
				String currSeasonquery = String.format(seasonQueryStatsBase, id,season);
				//System.out.println(currSeasonquery);
				statsResponse = this.executeQuery(currSeasonquery);
				statsResponse.next();
				rushYards= statsResponse.getString("Rush Yards");
				passYards = statsResponse.getString("Pass Yards");
				rushTD = statsResponse.getString("Rush TD");
				passTD = statsResponse.getString("Pass TD");
				passComp = statsResponse.getString("Pass Comp");
				JSONObject seasonStats = new JSONObject();
				seasonStats.put("Rush Yards", rushYards);
				seasonStats.put("Pass Yards", passYards);
				seasonStats.put("Rush TD", rushTD);
				seasonStats.put("Pass TD", passTD);
				seasonStats.put("Pass Comp", passComp);
				fileObject.put(season, overAllStats);
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
		//System.out.println(seasons.toString())
		
	}
	
	//execute a query
	public ResultSet executeQuery(String query) {
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
	
}
