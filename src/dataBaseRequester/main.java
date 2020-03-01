public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub  
		connectorPostgres connector = new connectorPostgres(config.username,config.credential,config.databaseName);
		//connector.executeQuery("SELECT * FROM \"players\" WHERE \"First Name\" = 'Edgar';");
		connector.playerFormRequest("NULL", "NULL", "Texas A&M", "NULL", "Plano");
	}
	

}
