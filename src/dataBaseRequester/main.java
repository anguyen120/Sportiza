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
        //connector.playerFormRequest("NULL", "NULL", "Texas A&M", "NULL", "NULL");
        connector.playerStatsRequest("1041700","Johnny", "Manziel","kerriville");
    }


}
