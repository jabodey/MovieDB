import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@WebServlet("/movie-suggestion")
public class MovieSuggestion extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
	
	public HashMap<String, List<String>> queries = new HashMap<>();
	
	
	public MovieSuggestion() {
	    super();
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// setup the response json arrray
			JsonArray jsonArray = new JsonArray();
			
			// get the query string from parameter
			String query = request.getParameter("query");
			
			// return the empty json array if query is null or empty
			if (query == null || query.trim().isEmpty()) {
				response.getWriter().write(jsonArray.toString());
				return;
			}	
			
			jsonArray = getMovies(query);					
			response.getWriter().write(jsonArray.toString());
			return;
		} catch (Exception e) {
			System.out.println(e);
			response.sendError(500, e.getMessage());
		}
	}
	
	protected JsonArray getMovies(String query) {
		JsonArray j = new JsonArray();
		List<String> result = new ArrayList<String>();
		if(queries.containsKey(query)) {
			System.out.println("This query is already in the cache!");
			for(String m : queries.get(query)) {
				j.add(generateJsonObject(m));	
			}
		}
		
		else {
			try {
				String q = "";
				Connection database = dataSource.getConnection();
				String[] splited = query.split("\\s+");
				String words = "";
				for(String str : splited) {
						words += "+" + str + "* ";
				}
				
				if(splited.length <= 1) {
					 q = "SELECT * FROM movies WHERE MATCH (title) AGAINST ('" + words + "' IN BOOLEAN MODE) OR edrec('" + query + "', title, 1) LIMIT 10";
				}
				else {
					 q = "SELECT * FROM movies WHERE MATCH (title) AGAINST ('" + words + "' IN BOOLEAN MODE) OR edth('" + query + "', title, 1) LIMIT 10";
				}
				
				PreparedStatement preparedStatement = database.prepareStatement(q);
				ResultSet rs = preparedStatement.executeQuery();						
				
				while(rs.next()) {
					String title = rs.getString("title");
					result.add(title);
					j.add(generateJsonObject(title));
				}
				
				queries.put(query, result);	
				
				}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		return j;
	}
	
	private static JsonObject generateJsonObject(String title) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", title);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("category","movie");
		//additionalDataJsonObject.addProperty("heroID", heroID);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}
	
}

