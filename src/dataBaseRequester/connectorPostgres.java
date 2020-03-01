import java.io.FileWriter;
import java.io.IOException;
import java.sql.*; 
import org.json.*;
public class connectorPostgres {
	Connection conn;
	
	connectorPostgres(String username, String credential, String database){
		try {
			Class.forName("org.postgresql.Driver");
			conn =  DriverManager.getConnection(database,username, credential);
			System.out.println("Connected to sportizadevspace database!");
		}catch(Exception e){ 
			System.out.println(e);
		} 
	}
	public void playerFormRequest(String FirstName, String LastName, String Team, String UniformNumber, String HomeTown) {
		//formating strings
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
		String query = "SELECT DISTINCT ON(\"players\".\"First Name\",\"players\".\"Last Name\") \"players\".\"id\",\"players\".\"First Name\", \"players\".\"Last Name\",\"players\".\"Position\", \"players\".\"Home Town\", \"players\".\"Home State\", \"players\".\"Home Country\"\n" + 
				"    FROM \"players\" JOIN \"teams\"\n" + 
				"        ON \"teams\".\"name\"  = COALESCE(%1$s,\"teams\".\"name\")\n" + 
				"               and \"teams\".\"id\" = \"players\".\"Team Code\"\n" + 
				"               and \"players\".\"First Name\" =  COALESCE(%2$s,\"players\".\"First Name\")\n" + 
				"               and \"players\".\"Last Name\" =  COALESCE(%3$s,\"players\".\"Last Name\")\n" + 
				"               and \"players\".\"Uniform Number\" = COALESCE(%4$s,\"players\".\"Uniform Number\")\n" + 
				"               and \"players\".\"Home Town\" = COALESCE(%5$s,\"players\".\"Home Town\");";
		//loading values to empty query
		query = String.format(query, Team,FirstName, LastName, UniformNumber, HomeTown);
		//System.out.println(query);
		//response for executed Query
		ResultSet response = this.executeQuery(query);
		try {
			FileWriter jsonFile = new FileWriter("requestFiles/requestFiles.json");
			JSONObject fileObject = new JSONObject();
			while(response.next()) {
				String playerID = response.getString("id");
				String playerName = response.getString("First Name");
				String playerLastName = response.getString("Last Name");
				String playerTown = response.getString("Home Town");
				String position = response.getString("Position");
				String playerState = response.getString("Home State");
				String playerCountry = response.getString("Home Country");
				//System.out.println("First name: " + playerName + ", Last Name: " + playerLastName+ ", Position: " + position +  ", Home Town: " + playerTown + ", State: " + playerState + ", Country: " + playerCountry);
				JSONObject userObject = new JSONObject();
				userObject.put("First Name", playerName);
				userObject.put("Last Name", playerLastName);
				userObject.put("Home Town", playerTown);
				userObject.put("Home State", playerState);
				userObject.put("Home Country", playerCountry);
				userObject.put("Position", position);
				fileObject.put(playerID, userObject);
			}
			jsonFile.write(fileObject.toString());
			jsonFile.close();
		} catch (SQLException | JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
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
