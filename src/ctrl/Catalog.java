package ctrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.BookBean;
import model.BookCategory;
import model.Hub;
import strings.Attr;
import strings.Pages;

/**
 * Servlet implementation class Catalog
 */
@WebServlet("/catalog")
public class Catalog extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ServletContext context;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Catalog() {
        super();
        // TODO Auto-generated constructor stub
    }

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = this.getServletContext();
		try {
			context.setAttribute("myHub", Hub.getInstance());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// for debugging
		System.out.println(request.getRequestURI());
		// housekeeping for every request
		HttpSession mySession = request.getSession();
		Hub myHub = (Hub) context.getAttribute(Attr.myHub);
		removeSessionAttributes(request, Attr.signupSuccess, Attr.orderSuccess);
		
		String dispatchPage = Pages.CATALOG;
		HashMap<String, BookBean> books = null;
		String category = request.getParameter("category");
		
		if (category != null && !category.isEmpty() && bookCategoryHas(category)) {
			try {
				books = myHub.retrieveBooksByCategory(BookCategory.valueOf(category));
				mySession.setAttribute(Attr.booksByCategory, books);
			} catch (SQLException e) {
				System.out.println("Failed to retrieve " + category + " books...");
			}				
		}
		else {
			dispatchPage = Pages.NOTFOUND;
		}

		RequestDispatcher rd = request.getRequestDispatcher(dispatchPage);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void removeSessionAttributes(HttpServletRequest request, String... attributes) {
		for (String attribute : attributes) {
			request.getSession().removeAttribute(attribute);
		}	
	}
	
	private boolean bookCategoryHas(String in) {
	    for (BookCategory category : BookCategory.values()) {
	        if (category.toString().equals(in))
	            return true;
	    }
	    
	    return false;
	}

}
