

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mysql.jdbc.StringUtils;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ParseStars2 {
	Document dom;
    public void runExample() {

        parseXmlFile();

        parseDocument();

    }

    private void parseXmlFile() {
       
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("actors63.xml");
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
        
        int actorId = 0;
        HashMap<String, String> actorMP = new HashMap<>();
        List<List<String>> actorsToInsert = new ArrayList<>();

        try {
        	Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        	NodeList el = docEle.getElementsByTagName("actor");
        	System.out.println(el);
        	String query0 = "SELECT max(id) as m from stars";
           	Statement statm0 = connection.createStatement();
           	ResultSet max = statm0.executeQuery(query0);
           	max.next();
           	String id = max.getString("m");
           	actorId = Integer.parseInt(id.substring(2, id.length()))+1;
           	
           	String queryActors = "SELECT * from stars";
           	Statement statmActors = connection.createStatement();
           	ResultSet actorSet = statmActors.executeQuery(queryActors);
           	while(actorSet.next()) {
           		actorMP.put(actorSet.getString("name"), actorSet.getString("birthYear"));
           		//movieMP.add(moviesSet.getString("title"));
           	}
           	
           	
            if (el != null && el.getLength() > 0) {
                for (int i = 0; i < el.getLength(); i++) {
                    Element actor = (Element) el.item(i);
                    String aname = getTextValue(actor, "stagename");
                    String byear = getTextValue(actor, "dob");
                    
                    System.out.println(aname);
                    System.out.println(byear);                   

//	                String query = "SELECT * FROM stars where name=? and birthYear=?";
//	                PreparedStatement statement = connection.prepareStatement(query);
//	                statement.setString(1, aname);
//	                statement.setString(2, byear);	                
//	                ResultSet rs1 = statement.executeQuery();
                    System.out.println("AA"+actorMP.get(aname));
	                if(actorMP.containsKey(aname) && actorMP.get(aname)!= null && actorMP.get(aname).equals(byear)) {
	                	System.out.println("This star already exists in the database!");;
	                }
	                else{
	                	String aid = "nm" + Integer.toString(actorId++);
	                   	System.out.println(aid);
	                   	//String queryIn = "INSERT INTO stars VALUES(?,?,?)";
	                   	ArrayList<String> tmp = new ArrayList<>();
		                actorMP.put(aname, byear);
	                    if(aname == null)
	                    	aname = "unknown";
	                    if(!StringUtils.isStrictlyNumeric(byear)) {
	                    	 System.out.println("Year of the movie is not valid!");
	                    	 byear = "1900";
	                    }
	                    tmp.add(aid);
	                    tmp.add(aname);
	                    tmp.add(byear);
	                    actorsToInsert.add(tmp);
//	                	System.out.println("Adding to the db now...");
//	                	String querymax = "SELECT max(id) as m from stars";
//	                   	Statement statm = connection.createStatement();
//	                   	ResultSet maxid = statm.executeQuery(querymax);
//	                   	maxid.next();
//	                   	System.out.println(maxid.getString("m"));
//	                   	String oldid = maxid.getString("m");
//	                	System.out.println(Integer.parseInt(oldid.substring(2, oldid.length()-1))+1);
//	                   	String newid = "nm" + Integer.toString((Integer.parseInt(oldid.substring(2, oldid.length()))+1));
//	                   	System.out.println(newid);
//	                	
//	                	String queryIn = "INSERT INTO stars VALUES(?,?,?)";
//	                    PreparedStatement statement1 = connection.prepareStatement(queryIn);
//	                    statement1.setString(1, newid);                    
//	                    statement1.setString(2, aname);
//	                    if(byear == null || !StringUtils.isStrictlyNumeric(byear)) {
//	                    	//System.out.println("This birthyear is not valid!");
//	                    	statement1.setNull(3, java.sql.Types.INTEGER);
////	                    }
//	                    else {
//		                    statement1.setString(3, byear);}
//		                    //System.out.println(statement1);
//		                    statement1.executeUpdate();
	                }
	             }
              }
            System.out.println("Adding to the db now...");
	       	//System.out.println(actorsToInsert.size());
	       	connection.setAutoCommit(false);
            String queryIn = "INSERT INTO stars VALUES(?,?,?)";
            PreparedStatement statement2 = connection.prepareStatement(queryIn);
 			 for(int i = 0; i<actorsToInsert.size(); i++) {
	       		 statement2.setString(1, actorsToInsert.get(i).get(0));
	       		 statement2.setString(2, actorsToInsert.get(i).get(1));
	       		 statement2.setString(3, actorsToInsert.get(i).get(2));
	       		 statement2.addBatch();
	       		 if(i%50 == 49) {
	       			statement2.executeBatch();
	       			connection.commit();
	       		 }
	       	 }
 			 statement2.executeBatch();
			 connection.commit();
			 System.out.println("All stars are parsed!");    
        }catch (SQLException e) {
			e.printStackTrace();}              
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
      
        ParseStars2 dpe = new ParseStars2();

        dpe.runExample();
    }

}

