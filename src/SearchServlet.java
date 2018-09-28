

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 6L;
	
	@Resource(name= "jdbc/moviedb")
	private DataSource dataSource;
	
	
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
		Connection database = dataSource.getConnection();
		
		System.out.println("1");
		String title = request.getParameter("title");
		System.out.println("2");
		String year = request.getParameter("year");
		System.out.println("3");
		String director = request.getParameter("director");
		String starName = request.getParameter("starName");
		
		request.setAttribute("title", title);
		request.setAttribute("year", year);
		request.setAttribute("director", director);
		request.setAttribute("starName", starName);
		
		String limit = "10";
		String offset = "0";
		
		System.out.println("In the search servlet!");
		response.sendRedirect("movie_list.html?title="+title+"&year="+year+"&director="+director+"&starName="+starName+"&limit="+limit+"&offset="+offset);
		//NEXT: get URL parameters (&title=asdf&year=asdf....etc) through js. kinda like how
		//it's done in single star servlet. js gets html URL parameters -> through ajax 
		//adds parameters to servlet URL -> servlet .getParameter each parameter and 
		//store into string -> execute SQL query -> convert key/value into JSON
		// -> print onto html id 
	}
	
	catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		
		
	}

}
