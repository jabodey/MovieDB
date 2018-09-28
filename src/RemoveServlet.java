import javax.annotation.Resource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet("/RemoveServlet")
public class RemoveServlet extends HttpServlet {
    private static final long serialVersionUID = 5L;
    @Resource(name="jdbc/Master")
    private DataSource dataSource;


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	System.out.println("remove got!");
        String id = request.getParameter("id");
        String title = request.getParameter("name");
        
        ShoppingCart cart = (ShoppingCart) request.getSession().getAttribute("ShoppingCart");        
        MovieInCart itemToRemove = new MovieInCart(id,title); 
        System.out.println("declare new  " + itemToRemove.toString());
        cart.removeMovie(itemToRemove);             
        System.out.println(cart.getItems());        
    }
}


