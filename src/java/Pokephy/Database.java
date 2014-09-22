package Pokephy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database {
    /* *****************************************************************************************/

    //Constructor
    private Connection connexion;
    
    //SINGLETON
    private Database(String url, String user, String password) {
        //DEFINE JDBC objects for connection
        try {
            connexion = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to " + url + " successful for " + user);
        } catch (SQLException ex) {
            Logger.getLogger("ConnectBDD").log(Level.SEVERE, null, ex);
            System.out.println("failed to connect to " + url + "  for " + user);
        }
    }
    public static Database instance = new Database("jdbc:mysql://localhost:3306/pokephy", "root", "");

    /* *****************************************************************************************/
    /* JSP GETTERS AND SETTERS */ 
    public Connection getConnexion() {
        return connexion;
    }
    public void setConnexion(Connection connexion) {
        this.connexion = connexion;
    }
    
    /* CONVERSION METHODS */
    //FORMAT CONVERTER
    private static String extractString(ResultSet results, String fieldName) {
        String field = "";
        try {
            if (results.getString(fieldName)!=null)
                field = results.getString(fieldName).replaceAll("[ ]*$", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return field;
    }
    private static Integer extractNumber(ResultSet results, String fieldName) {
        Integer field = null;
        try {
            if (results.getString(fieldName)!=null)
                field = Integer.valueOf(results.getString(fieldName).replaceAll("[^0-9]*", ""));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return field;
    }
    private static Date extractDate(ResultSet results, String datename) {
        Date date = null;
        try {
            //sql date 
            java.sql.Date odate = results.getDate(datename);
            //convert the oracle date to kernel.date
            date = new Date(odate.getDay(), odate.getMonth(), odate.getYear());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return date;
    }
    private static String convertDateToString(Date date) {
        return date.getDay() + "/" + date.getMonth() + "/" + date.getYear();
    }
    
    //GLOBAL CODE 
    private int getResultsCount(ResultSet results){
        int rowCount = 0;
        try {
            while(results.next())
                rowCount++;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rowCount;
    }
    private ResultSet getResultsOfQuery(String query, boolean callNext){
        ResultSet results = null;
        try {
            //Statement request = this.connexion.createStatement();
            Statement request = this.getConnexion().createStatement();

            //request all the objects from the database
            results = request.executeQuery(query);
            
            //count the results if any
            int rowCount = 0;
            while(results.next())
                rowCount++;
            
            //if any : reexecute and return
            if (rowCount>0){
                System.out.println(rowCount+" RESULTS FOUND FOR "+query);
                results=request.executeQuery(query);
                if (callNext)
                    results.next();
            }
            else 
                System.out.println("NO RESULTS FOUND FOR "+query);
            
            request.closeOnCompletion();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return results;
    }
    private ResultSet getResultsOfQuery(String query){
        return getResultsOfQuery(query,false);
    }
    
    //PREPARED QUERIES
    private ResultSet getResultSetFromIdQuery(String table, int id) {
        ResultSet results;
        table = table.toUpperCase();
        String request = "SELECT * FROM table WHERE field = id";
        request=request.replace("table", table);
        request= request.replace("field", "ID_"+table);
        request=request.replace("id", ""+id);
        results = this.getResultsOfQuery(request,true);
        return results;
    }

    /* INSERTION METHODS */
    private void insertIntoTableAllValues(String table, Object... values){
        try {
            Statement s = this.connexion.createStatement();
            
            //create the query
            String insert = "INSERT INTO "+table.toUpperCase()+" values(";
            
            for (int i = 0; i<values.length;i++)
                insert+= values[i]+ (i==values.length-1 ? "" : ",");
            
            insert += ");";
            
            s.executeQuery(insert);
            s.closeOnCompletion();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }//OBSOLETE : should not be used (see rules)
    private int insertIntoTableValuesForFields(String table, String fields, Object...values){
        String insert = "";
        PreparedStatement statement = null;
        Statement getIDGenerated = null;
        ResultSet generatedID = null;
        
        int idGene = -1;
        try {
            //Statement s = this.connexion.createStatement();
            
            //create the query
            insert = "INSERT INTO "+table.toUpperCase()+fields+" values(";
            
            for (int i = 0; i<values.length;i++)
                insert+= (values[i] instanceof String ? "'" :"") + 
                        values[i]+
                        (values[i] instanceof String ? "'" :"")+
                        (i==values.length-1 ? "": ","
                        );
            
            insert += ")";
            
            System.out.println(insert);
            
            statement = this.connexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("ERROR : NO ROW AFFECTED");
            }
            
            //GET THE LAST ID GENERERATED
            getIDGenerated = this.connexion.createStatement();
            insert = "SELECT MAX(ID_" + table.toUpperCase() + ") FROM "+table.toUpperCase()+"";
            
            generatedID = getIDGenerated.executeQuery(insert);
            while(generatedID.next()) {
                idGene = generatedID.getInt(1);
            }
            
            statement.closeOnCompletion();
            return idGene;
            
            //TODO: Find a better way to get back the generated ID...
//            System.out.println("UpdateCount : " + statement.getUpdateCount());
//            generatedKeys = statement.getGeneratedKeys();
//            System.out.println("KEY GENERATED");
//            int c = 0;
//            while (generatedKeys.next()) {
//                c++;
//                //idGenerated = (int)generatedKeys.getInt(1);
//                System.out.println("ID GENERATED : " + generatedKeys.getLong(c));
//            }
////            else {
////                System.out.println("ERROR : NO ID GENERATED");
////            }
            
        } catch (SQLException ex) {
            System.out.println("ERROR ON : " + insert);
            System.out.println(ex);
            return -1;
        }
    }
    
    /* DELETION METHODS */
    public void close() throws SQLException{
        connexion.close();
    }
    
/********************************************************************************************************************************************
 *  PUBLIC QUERIES 
 */
    public void executeTestQuery()
    {
        ResultSet rs = this.getResultsOfQuery("SELECT * FROM OBJECT;");
    }
    
}

