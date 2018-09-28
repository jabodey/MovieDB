

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class InsertStarServlet
 */
@WebServlet("/InsertStarServlet")
public class InsertStarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource(name="jdbc/Master")
    private DataSource dataSource;
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Connection database = dataSource.getConnection();
			//PrintWriter out = response.getWriter();
			String query = "select max(id) from stars";
			String maxid = "";
			Statement statement = database.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next())
			{
				maxid = resultSet.getString("max(id)");
			}
			String[] splitMax = maxid.split("(?<=\\D)(?=\\d)");
			System.out.println(splitMax[0]);
			System.out.println(splitMax[1]);
			int maxInt = Integer.parseInt(splitMax[1]);
			maxInt++;
			splitMax[1] = Integer.toString(maxInt);
			maxid = String.join("", splitMax[0], splitMax[1]);
			
			statement.close();
			
			String starName = request.getParameter("starName");
			String birthYear = request.getParameter("birthYear");
			List<String> parameters = new ArrayList<String>();
			parameters.add(maxid);
			parameters.add(starName);
			if(birthYear != null && !birthYear.isEmpty())
				parameters.add(birthYear);
			else
				parameters.add(null);
			query = "insert into stars(id, name, birthYear) values(?, ?, ?);";
			PreparedStatement preparedStatement = database.prepareStatement(query);
			for(int x = 0; x < parameters.size(); x++)
			{
				preparedStatement.setString(x+1, parameters.get(x));
				System.out.println(parameters.get(x));
			}
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
			
			response.sendRedirect("_dashboard.html?message=" + "Star has been added.");
			
			preparedStatement.close();
			resultSet.close();
			database.close();
			
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
