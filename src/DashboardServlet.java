

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
 * Servlet implementation class DashboardServlet
 */
@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Resource(name="jdbc/moviedb")
    private DataSource dataSource;
    
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		
		try {
			Connection database = dataSource.getConnection();
			PrintWriter out = response.getWriter();
			JsonArray jsonArray = new JsonArray();
			String query = "select table_name, group_concat(concat(column_name, ' ', column_type)) as attributes ";
			query += "from information_schema.columns where table_schema = 'moviedb' group by table_name";
			Statement statement = database.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next())
			{
				String table_name = resultSet.getString("table_name");
				String attributes = resultSet.getString("attributes");
				
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("table_name", table_name);
				jsonObject.addProperty("attributes", attributes);
				
				jsonArray.add(jsonObject);
				
			}
			out.write(jsonArray.toString());
			response.setStatus(200);
			
			resultSet.close();
			statement.close();
			database.close();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
