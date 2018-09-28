
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ParseMovies2 {
	Document dom;
    public void runExample() {

        parseXmlFile();

        parseDocument();

    }

    private void parseXmlFile() {
       
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse("mains243.xml");
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
        PreparedStatement statement1 = null;
        int ID = 0;
        int genreId = 0;
        Set<String> movieMP = new HashSet<>();
        Set<String> genreMP = new HashSet<>();
        
        try {
        	Connection connection = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
        	
        	String querymax = "SELECT max(id) as m from movies";
           	Statement statm = connection.createStatement();
            ResultSet maxid = statm.executeQuery(querymax);
           	maxid.next();
           	System.out.println(maxid.getString("m"));
           	String oldid = maxid.getString("m");
        	System.out.println(Integer.parseInt(oldid.substring(2, oldid.length()-1))+1);
           	ID = Integer.parseInt(oldid.substring(2, oldid.length()))+1;
           	
           	String query0 = "SELECT max(id) as m from genres";
           	Statement statm0 = connection.createStatement();
           	ResultSet max = statm0.executeQuery(query0);
           	max.next();
           	genreId = max.getInt("m") + 1;
           	
           	String queryMovies = "SELECT * from movies";
           	Statement statmMovies = connection.createStatement();
           	ResultSet moviesSet = statmMovies.executeQuery(queryMovies);
           	
           	while(moviesSet.next()) {
           		movieMP.add(moviesSet.getString("title") + moviesSet.getString("year") + moviesSet.getString("director"));
           	}
           	
           	String queryGenres = "SELECT * from genres";
           	Statement statmGenres = connection.createStatement();
           	ResultSet genresSet = statmGenres.executeQuery(queryGenres);
           	
           	while(genresSet.next()) {
           		genreMP.add(genresSet.getString("name"));
           	}
       	 
           	List<List<String>> moviesToInsert = new ArrayList<>();
           	
        	NodeList el = docEle.getElementsByTagName("directorfilms");
        	System.out.println(el);
            if (el != null && el.getLength() > 0) {
                for (int i = 0; i < el.getLength(); i++) {
                    Element moviesByd = (Element) el.item(i);
                    String director = getTextValue(moviesByd, "dirname");
                    NodeList movie = (NodeList) moviesByd.getElementsByTagName("film");
                    for (int j = 0; movie.item(j) != null && j < movie.getLength(); j++) {
                    	 String title = getTextValue((Element) movie.item(j), "t");
                         String mid = getTextValue((Element) movie.item(j), "fid");                     
                         String year = getTextValue((Element) movie.item(j), "year");
                         String genre = getTextValue((Element) movie.item(j), "cat");
                         
                         mid = "tt" + Integer.toString(ID++);
                         
                         if(title == null) {
                    		 title = "unknown";
                    	 }
                    	 if(!StringUtils.isStrictlyNumeric(year)) {
	                    	 System.out.println("Year of the movie is not valid!");
	                    	 year = "1900";
	                     }
                    	 if(director == null) {
                    		 director = "unknown";
                    	 }
                    	 
                         System.out.println(title);
                         System.out.println(mid);
                         System.out.println(director);
                         System.out.println(year);
                         System.out.println(genre);
                         
                         //NodeList genres = (NodeList) moviesByd.getElementsByTagName("cat");
                         //insert into movies
//	                     String query = "SELECT * FROM movies where title=? and director=? and year=?";
//	                     PreparedStatement statement = connection.prepareStatement(query);
//	                     statement.setString(1, title);
//	                     statement.setString(2, director);
//	                     statement.setString(3, year);	                     
//	                     ResultSet rs1 = statement.executeQuery();
	                     List<String> tmp = new ArrayList<>();
	                     
	                     
	                     if(movieMP.contains(title+year+director)) {
	                    	 System.out.println("This movie already exists in the database!");;
	                     }
	                     else{
	                    	 tmp.add(mid);
	                    	 tmp.add(title);
	                    	 tmp.add(year);
	                    	 tmp.add(director);
	                    	 movieMP.add(title+year+director);
	                     }
	                     
	                     //insert into genres
//	                     String queryg = "SELECT * FROM genres where name=?";
//	                     PreparedStatement statementg = connection.prepareStatement(queryg);
//	                     statementg.setString(1, genre);
//	                     ResultSet rsg = statementg.executeQuery();
	                     if(genreMP.contains(genre)) {
	                    	 System.out.println("This genre already exists in the database!");;
	                     }
	                     else if(genre != null){
	                    	 System.out.println("Adding to the db now...");
	                    	 String queryIn = "INSERT INTO genres VALUES(?,?)";
	                    	 PreparedStatement statement5 = connection.prepareStatement(queryIn);
	                    	 statement5.setInt(1, genreId);
	                         statement5.setString(2, genre);                
	                         statement5.executeUpdate();
	                         genreMP.add(genre);
	                         tmp.add(Integer.toString(genreId++));
	                     }
	                     else {
	                    	 System.out.println("There is no genre for this movie");
	                     }
	                     if(tmp.size()>=1) moviesToInsert.add(tmp);
                    }
                }
            }      	     
            
	       	 System.out.println("Adding to the db now...");
	       	 System.out.println(moviesToInsert.size());
	       	 connection.setAutoCommit(false);
	       	 String queryIn = "INSERT INTO movies VALUES(?,?,?,?)";
	       	 statement1 = connection.prepareStatement(queryIn);
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
  			 
             queryIn = "INSERT INTO genres_in_movies VALUES(?,?)";
             PreparedStatement statement2 = connection.prepareStatement(queryIn);
  			 for(int i = 0; i<moviesToInsert.size(); i++) if(moviesToInsert.get(i).size()>=5){
	       		 statement2.setString(1, moviesToInsert.get(i).get(4));
	       		 statement2.setString(2, moviesToInsert.get(i).get(0));
	       		 statement2.addBatch();
	       		 if(i%50 == 49) {
	       			statement2.executeBatch();
	       			connection.commit();
	       		 }
	       	 }
  			 statement2.executeBatch();
 			 connection.commit();

        }catch (SQLException e) {
			e.printStackTrace();
		}        
        System.out.println("All movies are parsed!");
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
        //in production application you would catch the exception
        return Integer.parseInt(getTextValue(ele, tagName));
    }
 

    public static void main(String[] args) {
      
        ParseMovies2 dpe = new ParseMovies2();

        dpe.runExample();
    }

}

