
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ParseStars {
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

        try {
        	Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        	NodeList el = docEle.getElementsByTagName("actor");
        	System.out.println(el);
            if (el != null && el.getLength() > 0) {
                for (int i = 0; i < el.getLength(); i++) {
                    Element actor = (Element) el.item(i);
                    String aname = getTextValue(actor, "stagename");
                    String byear = getTextValue(actor, "dob");
                    
                    System.out.println(aname);
                    System.out.println(byear);                   

	                String query = "SELECT * FROM stars where name=? and birthYear=?";
	                PreparedStatement statement = connection.prepareStatement(query);
	                statement.setString(1, aname);
	                statement.setString(2, byear);	                
	                ResultSet rs1 = statement.executeQuery();
	                
	                if(rs1.next()) {
	                	System.out.println("This star already exists in the database!");;
	                }
	                else{
	                	System.out.println("Adding to the db now...");
	                	String querymax = "SELECT max(id) as m from stars";
	                   	Statement statm = connection.createStatement();
	                   	ResultSet maxid = statm.executeQuery(querymax);
	                   	maxid.next();
	                   	System.out.println(maxid.getString("m"));
	                   	String oldid = maxid.getString("m");
	                	System.out.println(Integer.parseInt(oldid.substring(2, oldid.length()-1))+1);
	                   	String newid = "nm" + Integer.toString((Integer.parseInt(oldid.substring(2, oldid.length()))+1));
	                   	System.out.println(newid);
	                	
	                	String queryIn = "INSERT INTO stars VALUES(?,?,?)";
	                    PreparedStatement statement1 = connection.prepareStatement(queryIn);
	                    statement1.setString(1, newid);                    
	                    statement1.setString(2, aname);
	                    if(byear == null || !StringUtils.isStrictlyNumeric(byear)) {
	                    	//System.out.println("This birthyear is not valid!");
	                    	statement1.setNull(3, java.sql.Types.INTEGER);
	                    }
	                    else {
		                    statement1.setString(3, byear);}
		                    //System.out.println(statement1);
		                    statement1.executeUpdate();
	                }
	             }
              }       
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
      
        ParseStars dpe = new ParseStars();

        dpe.runExample();
    }

}

