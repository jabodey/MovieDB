
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ParseCasts {
	Document dom;
    public void runExample() {

        parseXmlFile();

        parseDocument();

    }

    private void parseXmlFile() {
       
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("casts124.xml");
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void parseDocument() {
    	String loginUser = "root";
        String loginPasswd = "TIAN950130";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
        Element docEle = dom.getDocumentElement();
        int ID = 0;
        int actorId = 0;
        HashMap<String, String> movieMP = new HashMap<>();
        HashMap<String, String> actorMP = new HashMap<>();
        HashSet<String> relationMP = new HashSet<>();
    	List<List<String>> moviesToInsert = new ArrayList<>();
    	List<List<String>> actorsToInsert = new ArrayList<>();
    	List<List<String>> relationsToInsert = new ArrayList<>();
    	
        try {
        	Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        	NodeList el = docEle.getElementsByTagName("m");
        	System.out.println(el);
        	
        	String querymax = "SELECT max(id) as m from movies";
           	Statement statm = connection.createStatement();
            ResultSet maxid = statm.executeQuery(querymax);
           	maxid.next();
           	String oldid = maxid.getString("m");
           	ID = Integer.parseInt(oldid.substring(2, oldid.length()))+1;
           	
           	String query0 = "SELECT max(id) as m from stars";
           	Statement statm0 = connection.createStatement();
           	ResultSet max = statm0.executeQuery(query0);
           	max.next();
           	String id = max.getString("m");
           	actorId = Integer.parseInt(id.substring(2, id.length()))+1;
           	
           	String queryMovies = "SELECT * from movies";
           	Statement statmMovies = connection.createStatement();
           	ResultSet moviesSet = statmMovies.executeQuery(queryMovies);
           	while(moviesSet.next()) {
           		movieMP.put(moviesSet.getString("title"), moviesSet.getString("id"));
           	}
           	
           	String queryActors = "SELECT * from stars";
           	Statement statmActors = connection.createStatement();
           	ResultSet actorSet = statmActors.executeQuery(queryActors);
           	while(actorSet.next()) {
           		actorMP.put(actorSet.getString("name"), actorSet.getString("id"));
           		//movieMP.add(moviesSet.getString("title"));
           	}
           	
           	String queryRelation = "SELECT * from stars_in_movies";
           	Statement statmRelations = connection.createStatement();
           	ResultSet relationSet = statmRelations.executeQuery(queryRelation);
           	while(relationSet.next()) {
           		relationMP.add(relationSet.getString("starId") + relationSet.getString("movieId"));
           		//movieMP.add(moviesSet.getString("title"));
           	}
           	
            if (el != null && el.getLength() > 0) {
                for (int i = 0; i < el.getLength(); i++) {
                    Element cast = (Element) el.item(i);
                    String mid = getTextValue(cast, "f");
                    String mname = getTextValue(cast, "t");
                    String aname = getTextValue(cast, "a");
                    String aid = "";
                    
                    System.out.println(mid);
                    System.out.println(mname); 
                    System.out.println(aname);

	                if(movieMP.containsKey(mname)) {
	                	mid = movieMP.get(mname);
	                }
	                else {
//	                	String querymax = "SELECT max(id) as m from movies";
//	                   	Statement statm = connection.createStatement();
//	                    ResultSet maxid = statm.executeQuery(querymax);
//	                   	maxid.next();
//	                	String oldid = maxid.getString("m");
	                	System.out.println(ID+1);
	                	mid = "tt" + Integer.toString(ID++);
	                	ArrayList<String> tmp = new ArrayList<>();
	                	tmp.add(mid);
	                	tmp.add(mname);
	                	tmp.add("1900");
	                	tmp.add("unknown");
	                	moviesToInsert.add(tmp);
	                	movieMP.put(mname, mid);
//	                   	String queryIn = "INSERT INTO movies VALUES(?,?,?,?)";
//	                   	PreparedStatement statement1 = connection.prepareStatement(queryIn);
//	                   	statement1.setString(1, mid);
//	                   	statement1.setString(2, "unknown");
//	                   	statement1.setString(3, "0000");
//	                   	statement1.setString(4, "unknown");
//	                   	statement1.executeUpdate();
	                }
	                
//	                String querys = "SELECT * FROM stars where name=?";
//	                PreparedStatement statements = connection.prepareStatement(querys);
//	                statements.setString(1, aname);                
//	                ResultSet rss = statements.executeQuery();
	                if(actorMP.containsKey(aname)) {
	                	aid = actorMP.get(aname);
	                }
	                else {
	                   	aid = "nm" + Integer.toString(actorId++);
	                   	System.out.println(aid);
	                   	//String queryIn = "INSERT INTO stars VALUES(?,?,?)";
	                   	ArrayList<String> tmp = new ArrayList<>();
		                actorMP.put(aname, aid);
	                    if(aname == null)
	                    	aname = "unknown";
	                    tmp.add(aid);
	                    tmp.add(aname);
//	                    statement0.setString(1, aid);                    
//	                    statement0.setString(2, aname);
//	                    statement0.setNull(3, java.sql.Types.INTEGER);
		                actorsToInsert.add(tmp);
	                }
	                
//	                
//	                String query1 = "SELECT * FROM stars_in_movies as sim where movieId=? and starId=?";
//	                PreparedStatement statement1 = connection.prepareStatement(query1);
//	                statement1.setString(1, mid); 
//	                statement1.setString(2, aid);
//	                ResultSet rscheck = statement1.executeQuery();
//	                
	                if(relationMP.contains(aid+mid)) {
	                	System.out.println("This cast already exists in the database!");;
	                }
	                else{
	                	ArrayList<String> tmp = new ArrayList<>();
	                	tmp.add(aid);
	                	tmp.add(mid);
	                	relationsToInsert.add(tmp);
	                	relationMP.add(aid+mid);
//	                	System.out.println("Adding to the db now...");	                	
//	                	String queryIn = "INSERT INTO stars_in_movies VALUES(?,?)";
//	                    PreparedStatement statementIn = connection.prepareStatement(queryIn);
//	                    statementIn.setString(1, aid);                    
//	                    statementIn.setString(2, mid);
//	                    statementIn.executeUpdate();
	                	}       
                	}
                }
            
             	System.out.println("Adding to the db now...");
		       	System.out.println(moviesToInsert.size());
		       	connection.setAutoCommit(false);
		       	String queryIn = "INSERT INTO movies VALUES(?,?,?,?)";
		       	PreparedStatement statement1 = connection.prepareStatement(queryIn);
		       	 for(int i = 0; i<moviesToInsert.size(); i++) {
		       		 statement1.setString(1, moviesToInsert.get(i).get(0));
		       		 statement1.setString(2, moviesToInsert.get(i).get(1));
		       		 statement1.setString(3, moviesToInsert.get(i).get(2));
		       		 statement1.setString(4, moviesToInsert.get(i).get(3));
		       		 statement1.addBatch();
		       		 if(i%50 == 49) {
		       			statement1.executeBatch();
		       			 connection.commit();
		       		 }
		       	 }
		       	 statement1.executeBatch();
	 			 connection.commit();
	 			 
	            queryIn = "INSERT INTO stars VALUES(?,?,?)";
	            PreparedStatement statement2 = connection.prepareStatement(queryIn);
	 			 for(int i = 0; i<actorsToInsert.size(); i++) {
		       		 statement2.setString(1, actorsToInsert.get(i).get(0));
		       		 statement2.setString(2, actorsToInsert.get(i).get(1));
		       		 statement2.setNull(3, java.sql.Types.INTEGER);
		       		 statement2.addBatch();
		       		 if(i%50 == 49) {
		       			statement2.executeBatch();
		       			connection.commit();
		       		 }
		       	 }
	 			 statement2.executeBatch();
				 connection.commit();
				 
				 queryIn = "INSERT INTO stars_in_movies VALUES(?,?)";
		         PreparedStatement statement3 = connection.prepareStatement(queryIn);
		         for(int i = 0; i<relationsToInsert.size(); i++) {
		        	 statement3.setString(1, relationsToInsert.get(i).get(0));
		        	 statement3.setString(2, relationsToInsert.get(i).get(1));
			         statement3.addBatch();
			       	 if(i%50 == 49) {
			       		 statement3.executeBatch();
			       		 connection.commit();
			       	 }
			     }
		 		 statement3.executeBatch();
				 connection.commit();
				 System.out.println("All Done!");
            }catch (SQLException e) {
            	e.printStackTrace();
			}              
    }
 
    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            if(el.getFirstChild() == null) {
            	return textVal;
            }
            textVal = el.getFirstChild().getNodeValue();
        }

        return textVal;
    }


    private int getIntValue(Element ele, String tagName) {
        if(getTextValue(ele, tagName) != null) {
        	return Integer.parseInt(getTextValue(ele, tagName));}      	
        else
        	return 0;
    }
 

    public static void main(String[] args) {
      
        ParseCasts dpe = new ParseCasts();

        dpe.runExample();
    }

}


