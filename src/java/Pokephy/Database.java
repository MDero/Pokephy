package Pokephy;

import Pokephy.Pokephy.Named;
import Pokephy.Pokephy.PhysicalSkill;
import Pokephy.Pokephy.Pokemon;
import Pokephy.Pokephy.Skill;
import Pokephy.Pokephy.Skill.SkillType;
import Pokephy.Pokephy.Trainer;
import Pokephy.Pokephy.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "database", eager = true)
@SessionScoped
public class Database {
/******************************************************************************************
 * DEFINITION
*****************************************************************************************/

    //MEGATABLE name
    private final String megatable = "entity INNER JOIN entity_caracteristic on entity.idEntity=entity_caracteristic.idE INNER JOIN caracteristic on entity_caracteristic.idC=caracteristic.idCaracteristic";
    
    //Constructor
    private Connection connexion;
    
    public Database() {
        this("jdbc:mysql://localhost:3306/pokephy", "root", "");
    }
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
/* JSP GETTERS AND SETTERS 
/* *****************************************************************************************/
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
            if (results.getString(fieldName) != null) {
                field = results.getString(fieldName).replaceAll("[ ]*$", "");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return field;
    }
    private static Integer extractNumber(ResultSet results, String fieldName) {
        Integer field = null;
        try {
            if (results.getString(fieldName) != null) {
                field = Integer.valueOf(results.getString(fieldName).replaceAll("[^0-9]*", ""));
            }
            else 
                System.out.println("results.getString("+fieldName+") is null !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return field;
    }
    private static Double extractDouble(ResultSet results, String fieldName){
        Double field = null;
        try {
            if (results.getString(fieldName) != null) {
                field = Double.valueOf(results.getString(fieldName).replaceAll("[^0-9\\.]*", ""));
            }
            else 
                System.out.println("results.getString("+fieldName+") is null !");
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
    private int getResultsCount(ResultSet results) {
        int rowCount = 0;
        try {
            while (results.next()) {
                rowCount++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rowCount;
    }
    private void executeSQLQuery(String query)
    {
        try {
            //Statement request = this.connexion.createStatement();
            Statement request = this.getConnexion().createStatement();
            request.executeUpdate(query);
            request.closeOnCompletion();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private ResultSet getResultsOfQuery(String query) {
        return getResultsOfQuery(query, false);
    }
    private ResultSet getResultsOfQuery(String query, boolean callNext) {
        ResultSet results = null;
        try {
            //Statement request = this.connexion.createStatement();
            Statement request = this.getConnexion().createStatement();

            //request all the objects from the database
            results = request.executeQuery(query);

            //count the results if any
            int rowCount = 0;
            while (results.next()) {
                rowCount++;
            }

            //if any : reexecute and return
            if (rowCount > 0) {
                System.out.println(rowCount + " RESULTS FOUND FOR " + query);
                results = request.executeQuery(query);
                if (callNext) {
                    results.next();
                }
            } else {
                System.out.println("NO RESULTS FOUND FOR " + query);
            }

            //request.closeOnCompletion();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return results;
    }

    //PREPARED QUERIES
    private ResultSet getResultSetFromIdQuery(String table, int id) {
        ResultSet results;
        table = table.toUpperCase();
        String request = "SELECT * FROM table WHERE field = id";
        request = request.replace("table", table);
        request = request.replace("field", "ID_" + table);
        request = request.replace("id", "" + id);
        results = this.getResultsOfQuery(request, true);
        return results;
    }

    /* INSERTION METHODS */
    private void insertIntoTableAllValues(String table, Object... values) {
        try {
            Statement s = this.connexion.createStatement();

            //create the query
            String insert = "INSERT INTO " + table.toUpperCase() + " values(";

            for (int i = 0; i < values.length; i++) {
                insert += "'" + values[i] + "'" + (i == values.length - 1 ? "" : ",");
            }

            insert += ");";

            System.out.println(insert);

            s.executeUpdate(insert);
            s.closeOnCompletion();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

    }//OBSOLETE : should not be used (see rules)
    private int insertIntoTableValuesForFields(String table, String fields, Object... values) {
        String insert = "";
        PreparedStatement statement = null;
        Statement getIDGenerated = null;
        ResultSet generatedID = null;

        int idGene = -1;
        try {
            //Statement s = this.connexion.createStatement();

            //create the query
            insert = "INSERT INTO " + table.toUpperCase() + fields + " values(";

            for (int i = 0; i < values.length; i++) {
                insert += (values[i] instanceof String ? "'" : "")
                        + values[i]
                        + (values[i] instanceof String ? "'" : "")
                        + (i == values.length - 1 ? "" : ",");
            }

            insert += ")";

            System.out.println(insert);

            statement = this.connexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("ERROR : NO ROW AFFECTED");
            }

            //GET THE LAST ID GENERERATED
            getIDGenerated = this.connexion.createStatement();
            insert = "SELECT MAX(id" + table.substring(0, 1).toUpperCase() + table.substring(1).toLowerCase() + ") FROM " + table.toUpperCase() + "";

            generatedID = getIDGenerated.executeQuery(insert);
            while (generatedID.next()) {
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
    public void close() throws SQLException {
        connexion.close();
    }
    public void clearAllData(){
        this.executeSQLQuery("DELETE FROM entity_caracteristic");
        this.executeSQLQuery("DELETE FROM entity");
    }
    public void clearUnusedEntities(){
        this.executeSQLQuery("DELETE FROM entity_caracteristic WHERE idE NOT IN(SELECT idEntity FROM entity)");
    }
    public void clear(String typeEntity){
        ResultSet rs = this.getResultsOfQuery("SELECT * FROM "+megatable+" WHERE typeEntity='"+typeEntity+"'");
        ArrayList<String> entries = new ArrayList<>();
        try {
            while (rs.next())
            {
                String s = extractString(rs,"idEntity");
                if (!entries.contains(s))
                    entries.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (String e : entries)
        {   
            System.out.println(e);
            this.executeSQLQuery("DELETE FROM entity_caracteristic WHERE idE="+e);
            this.executeSQLQuery("DELETE FROM entity WHERE idEntity="+e);
        }
    }
    
/** ******************************************************************************************************************************************
 * PUBLIC QUERIES
 * ******************************************************************************************************************************************/
    /* SOME ADVANCED TESTS */
    public void executeTestQuery() {
        ResultSet rs = this.getResultsOfQuery("SELECT * FROM ENTITY;");
    }
    public Skill executeSkillExtraction(){
        return getSkillFromCurrentRow(getResultsOfQuery("SELECT * FROM "+megatable+" WHERE typeEntity='SKILL'",true));
    }
    public void executeTestTrainerAndPokemonInsertion(){
        //clear test data
        clear("POKEMON");
        clear("TRAINER");
        
        //create dummy Pokemons 
        Pokemon p1 = new Pokemon("bulbasaur", Type.Grass, 45, 49, 49, 65, 65, 45);
        Pokemon p2 = new Pokemon("charmander", Type.Fire, 39, 52, 43, 60, 50, 65);
        Pokemon p3 = new Pokemon("squirtle", Type.Water, 44, 48, 65, 50, 64, 43);
        
        Pokemon p12 = new Pokemon("ivysaur",Type.Grass, 60,62,63,80,80,60);
        Pokemon p22 = new Pokemon("charmeleon", Type.Fire, 58,64,58,80,65,80);
        Pokemon p32 = new Pokemon("wartortle", Type.Water, 59,63,80,65,80,58);
        
        Pokemon p13 = new Pokemon("venusaur",Type.Grass, 80,82,83,100,100,80);
        Pokemon p23 = new Pokemon("charizard", Type.Fire, 78,84,78,109,85,100);
        Pokemon p33 = new Pokemon("blastoise", Type.Water, 79,83,100,85,105,78);
        
        //create a dummy trainer
        Trainer sacha = new Trainer("Sacha");
        sacha.pool.add(p1);
        sacha.pool.add(p2);
        sacha.pool.add(p3);
        
        //insert pokemons
        this.insertPokemon(p1);
        this.insertPokemon(p2);
        this.insertPokemon(p3);
        
        this.insertPokemon(p12);
        this.insertPokemon(p22);
        this.insertPokemon(p32);
        
        this.insertPokemon(p13);
        this.insertPokemon(p23);
        this.insertPokemon(p33);
        
        //insert trainer
        this.insertTrainer(sacha);
    }
    
    /* INSERTION METHODS */
    private int insertNamedEntity(Named n, String typeEntity) {
        int id = this.insertIntoTableValuesForFields(
                "entity",
                "(valueEntity,typeEntity)",
                n.getName(),
                typeEntity.toUpperCase()
        );
        n.setId(id);
        return id;
    }
    public void insertType(Type type) {
        //insert entity
        int id = this.insertIntoTableValuesForFields(
                "entity",
                "(valueEntity,typeEntity)",
                type.getName(),
                "TYPE"
        );
        type.setId(id);

        //map its caracteristics values
        this.insertIntoTableAllValues(
                "entity_caracteristic",
                id,
                17,//STRONG_AGAINST
                type.strongAgainst.name()
        );
        this.insertIntoTableAllValues(
                "entity_caracteristic",
                id,
                18,//WEAK_AGAINST
                type.weakAgainst.name()
        );
    }
    public void insertSkill(Skill skill) {
        //insert entry
        int id = insertNamedEntity(skill, "SKILL");

        //map its characteristics values
        this.insertIntoTableAllValues(
                "entity_caracteristic",
                id,
                10,//POWER
                skill.power
        );
        this.insertIntoTableAllValues(
                "entity_caracteristic",
                id,
                11,//SKILLTYPE
                skill.skilltype.toString()
        );
        this.insertIntoTableAllValues(
                "entity_caracteristic",
                id,
                12,//TYPE
                skill.type.getName()
        );
    }
    public void insertPokemon(Pokemon pokemon){
        //insert entry
        int id = insertNamedEntity(pokemon, "POKEMON");
        
        /* map its characteristics */
        //TYPE
        this.insertIntoTableAllValues(
                "entity_caracteristic",
                id,
                12,//TYPE
                pokemon.getType().getName()
        );

        //STATS
        this.insertIntoTableAllValues("entity_caracteristic",id,2/*HP*/,pokemon.getHP());
        this.insertIntoTableAllValues("entity_caracteristic",id,3/*ATK*/,pokemon.getATK());
        this.insertIntoTableAllValues("entity_caracteristic",id,4/*DEF*/,pokemon.getDEF());
        this.insertIntoTableAllValues("entity_caracteristic",id,5/*SP_ATK*/,pokemon.getSPATK());
        this.insertIntoTableAllValues("entity_caracteristic",id,6/*SP_DEF*/,pokemon.getSPDEF());
        this.insertIntoTableAllValues("entity_caracteristic",id,7/*SPEED*/,pokemon.getSPEED());
        
        //SKILLS
        this.insertIntoTableAllValues("entity_caracteristic",id,14/*SKILL_SPECIAL*/,pokemon.specialSkill.getName());
        this.insertIntoTableAllValues("entity_caracteristic",id,15/*SKILL_PHYSICAL*/,pokemon.physicalSkill.getName());
    }
    public void insertTrainer(Trainer trainer){
        //insert entry
        int id = insertNamedEntity(trainer, "TRAINER");
        
        //map its characteristics
//        this.insertIntoTableAllValues(
//                "entity_caracteristic",
//                id,
//                9,//PASSWORD
//                trainer.getPassword()
//        );
        
        /* map his/her pokemons */
        String idChain ="";

        //get the own string => structure like "pokeId(number),pokeId(number),pokeId(number)"...
        ResultSet rs = this.getResultsOfQuery("SELECT * FROM "+megatable+" WHERE typeEntity='TRAINER' AND idC=13 AND valueEntity='"+trainer.getName()+"'" /*13=> OWNED POKEMON*/, true);
        
        //count the results if any
        int rowCount = 0;
        try {
            while (rs.next()) {
                rowCount++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (rowCount > 0)
        {
            rs = this.getResultsOfQuery("SELECT * FROM "+megatable+" WHERE typeEntity='TRAINER' AND idC=13 AND valueEntity="+trainer.getName() /*13=> OWNED POKEMON*/, true);
            String owned = extractString(rs, "value");
            HashMap<Integer,Integer> pokemap = new HashMap<>(); 
            for (String id_number : owned.split(","))//id_number structure : pokeId(number)
            {
                int ind = id_number.indexOf("(");
                String poke_id = id_number.substring(0,ind);//pokeId
                String poke_number = id_number.substring(ind);//(number)
                poke_number = poke_number.replaceAll("[^0-9\\.]", "");//number

                pokemap.put(Integer.valueOf(poke_id), Integer.valueOf(poke_number));
            }


            for (Pokemon p : trainer.pool)
            {
                idChain+=p.getId();
                //check existence of the id of p 
                Integer ni = pokemap.get(p.getId());
                if (ni!=null)//if exists => increment
                {
                    ni++;
                }
                else
                {
                    ni = 1;
                }
                idChain+="("+ni+"),";
            }
        }
        else
        {
            for (Pokemon p : trainer.pool)
                idChain+=p.getId()+"(1),";//TODO: add something to count
        }
        
        this.insertIntoTableAllValues("entity_caracteristic",id,13/*OWN*/,idChain);
    }

    /* CONVERSION/PARSING METHODS */
    private Type getTypeFromCurrentRow(ResultSet results){
        Integer id = extractNumber(results,"idEntity");
//        String typeEntity = extractString(results, "typeEntity");
        String valueEntity = extractString(results, "valueEntity");
        return 
                valueEntity.equals("FIRE") ? Type.Fire :
                valueEntity.equals("WATER") ? Type.Water :
                valueEntity.equals("GRASS") ? Type.Grass : null ;
//        Type type = getOrCreateEntity(id,"TYPE");
    }
    private Type getTypeNamed(String name){
        return getTypeFromCurrentRow(this.getResultsOfQuery("SELECT * FROM entity WHERE typeEntity='TYPE' AND valueEntity='"+name+"'",true));
    }
    
    private Skill getSkillFromCurrentRow(ResultSet results){
        Integer id = extractNumber(results,"idEntity");
        String valueEntity = extractString(results, "valueEntity");
        Skill s = new Skill(valueEntity,null, null, 0);
        s.setId(id);
        
        //other statement to get all the lines bound to this entity
        ResultSet rs = this.getResultsOfQuery("SELECT * FROM "+megatable+" WHERE idEntity = "+id);
        try {
            while (rs.next())
            {
                String name = extractString(rs, "name");
                String value = extractString(rs , "value");
                if (name.equals("POWER"))
                    s.power = extractDouble(rs, "value");
                if (name.equals("SKILLTYPE"))
                    s.skilltype = value.toUpperCase().equals("SPECIAL") ? SkillType.Special : SkillType.Physical;
                if (name.equals("TYPE"))
                    s.type = value.equals("FIRE") ? Type.Fire : value.equals("WATER") ? Type.Water : Type.Grass;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return s;
    }
    private Skill getSkillNamed(String name){
        return getSkillFromCurrentRow(this.getResultsOfQuery("SELECT * FROM entity WHERE typeEntity='SKILL' AND valueEntity='"+name+"'",true));
    }
    
    private Pokemon getPokemonFromCurrentRow(ResultSet results){
        Integer id = extractNumber(results,"idEntity");
        String valueEntity = extractString(results, "valueEntity");
        Pokemon p = new Pokemon(valueEntity,null,0,0,0,0,0,0);
        p.setId(id);
        
        //other statement to get all the lines bound to this entity
        ResultSet rs = this.getResultsOfQuery("SELECT * FROM "+megatable+" WHERE idEntity = "+id);
        try {
            while (rs.next())
            {
                String name = extractString(rs, "name");
                String value = extractString(rs , "value");
                
                switch (name) {
                    case "TYPE":
                        p.setType(getTypeNamed(value));
                        break;
                    case "HP":
                        p.setHP(extractDouble(rs,"value"));
                        break;
                    case "ATK":
                        p.setATK(extractDouble(rs,"value"));
                        break;
                    case "DEF":
                        p.setDEF(extractDouble(rs,"value"));
                        break;
                    case "SP_ATK":
                        p.setSPATK(extractDouble(rs,"value"));
                        break;
                    case "SP_DEF":
                        p.setSPDEF(extractDouble(rs,"value")); 
                        break;
                    case "SPEED":
                        p.setSPEED(extractDouble(rs,"value"));
                        break;
                    case "SKILL_SPECIAL":
                        p.specialSkill = getSkillNamed(value);
                        break;
                    case "SKILL_PHYSICAL":
                        p.physicalSkill = getSkillNamed(value);
                        break;
                }
                    
                
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return p;
    }
    private Pokemon getPokemonNamed(String name){
        return getPokemonFromCurrentRow(this.getResultsOfQuery("SELECT * FROM entity WHERE typeEntity='POKEMON' AND valueEntity='"+name+"'", true));
    }
    
    private Trainer getTrainerFromCurrentRow(ResultSet results)
    {
        Integer id = extractNumber(results,"idEntity");
        String valueEntity = extractString(results, "valueEntity");
        Trainer t = new Trainer(valueEntity);
        t.setId(id);
        
        ResultSet rs = this.getResultsOfQuery("SELECT * FROM "+megatable+" WHERE idEntity = "+id);
        try {
            while (rs.next())
            {
                String name = extractString(rs, "name");
                String value = extractString(rs , "value");
                
                switch (name) {
                    case "OWN":
                        String[] pokemon_ids = value.split(",");
                        for (String p_id_number : pokemon_ids)
                        {
                            String p_id = p_id_number.substring(0,p_id_number.indexOf("("));
                            System.out.println(p_id);
                            for (Pokemon p : getAllPokemon())
                                if (p.getId() == Integer.valueOf(p_id))
                                    t.pool.add(p);
                        }
                        break;
                }
                    
                
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return t;
    }
    
    /* LISTS OF ALL */
    public List<Named> getAll(String typeEntity){
      ArrayList<Named> list = new ArrayList<>();
      
      ResultSet rs = this.getResultsOfQuery("SELECT * FROM entity WHERE typeEntity='"+typeEntity+"'");
        try {
            while (rs.next())
            {
                switch (typeEntity){
                    case "POKEMON":
                        list.add(getPokemonFromCurrentRow(rs));
                        break;
                    case "SKILL":
                        list.add(getSkillFromCurrentRow(rs));
                        break;
                    case "TRAINER":
                        list.add(getTrainerFromCurrentRow(rs));
                        break;
                }
                    
                            
            } 
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
      
      return list;
    }
    public List<Pokemon> getAllPokemon(){
        ArrayList<Pokemon> pokelist = new ArrayList<>();
        for (Named n : getAll("POKEMON"))
            pokelist.add((Pokemon) n);
        
        return pokelist;
    }
    public List<Type> getAllTypes(){
        ArrayList<Type> typelist = new ArrayList<>();
        typelist.addAll(Arrays.asList(Type.values()));
        return typelist;
    }
    private List<Skill> getAllSkills(SkillType st)
    {
        ArrayList<Skill> skills = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for (Named n : getAll("SKILL"))
            if (((Skill)n).skilltype==st && !(names.contains(n.getName())))
            {
                names.add(n.getName());
                skills.add((Skill)n);
            }
        return skills;
    }
    public List<Skill> getAllPhysicalSkills()
    {
        return getAllSkills(SkillType.Physical);
    }
    public List<Skill> getAllSpecialSkills()
    {
        return getAllSkills(SkillType.Special);
    }
    public List<Trainer> getAllTrainer()
    {
        ArrayList<Trainer> trainers = new ArrayList<>();
        for (Named n : getAll("TRAINER"))
            trainers.add((Trainer) n);
        return trainers;
    }
    
    
    /* SORTED LISTS */
    private List<Pokemon> sortedList(String caracteristic,String orderBy)
    {
      String typeEntity = "POKEMON";
      ArrayList<Pokemon> list = new ArrayList<>();
      
      ResultSet rs = this.getResultsOfQuery("SELECT * FROM "+megatable+" WHERE typeEntity='"+typeEntity+"' AND name='"+caracteristic+"' ORDER BY value "+orderBy);
        try {
            while (rs.next())
            {
                switch (typeEntity){
                    case "POKEMON":
                        list.add(getPokemonFromCurrentRow(rs));
                        break;
                }
            } 
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
      
      return list;
    }
    public List<Pokemon> getPokemonSortedByHP_DESC()
    {
        return sortedList("HP","DESC");
    }
    public List<Pokemon> getPokemonSortedByHP_ASC()
    {
        return sortedList("HP","ASC");
    }
    public List<Pokemon> getPokemonSortedByATK_DESC()
    {
        return sortedList("ATK","DESC");
    }
    public List<Pokemon> getPokemonSortedByATK_ASC()
    {
        return sortedList("ATK","ASC");
    }
    public List<Pokemon> getPokemonSortedByDEF_DESC()
    {
        return sortedList("DEF","DESC");
    }
    public List<Pokemon> getPokemonSortedByDEF_ASC()
    {
        return sortedList("DEF","ASC");
    }
    public List<Pokemon> getPokemonSortedBySPDEF_DESC()
    {
        return sortedList("SP_DEF","DESC");
    }
    public List<Pokemon> getPokemonSortedBySPDEF_ASC()
    {
        return sortedList("SP_DEF","ASC");
    }
    public List<Pokemon> getPokemonSortedBySPATK_DESC()
    {
        return sortedList("SP_ATK","DESC");
    }
    public List<Pokemon> getPokemonSortedBySPATK_ASC()
    {
        return sortedList("SP_ATK","ASC");
    }
    public List<Pokemon> getPokemonSortedBySPEED_DESC()
    {
        return sortedList("SPEED","DESC");
    }
    public List<Pokemon> getPokemonSortedBySPEED_ASC()
    {
        return sortedList("SPEED","ASC");
    }  
                    
}
