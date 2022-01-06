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
import model.Hub;
import strings.Attr;
import strings.Pages;

/**
 * Servlet implementation class Search
 */
@WebServlet("/search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ServletContext context;
       
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
	public Search() {
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
		removeSessionAttributes(request, Attr.searchKeywords, Attr.searchSuccess, Attr.signupSuccess, Attr.orderSuccess);
		
		String dispatchPage = Pages.SEARCH;
		HashMap<String, BookBean> books = new HashMap<String, BookBean>();
		String keywords = request.getParameter("search");
		
		if (keywords != null) {
			try {
				books = myHub.retrieveAllBooksByKeyWords(keywords);
				mySession.setAttribute(Attr.searchKeywords, keywords);
			} catch (SQLException e) {
				System.out.println("Failed to retrieve book with title " + keywords + "...");
			}
		}
		
		if (!books.isEmpty()) {
			for (BookBean book : books.values()) {
				System.out.println(book.getTitle());
			}
			mySession.setAttribute(Attr.searchedBooks, books);
			mySession.setAttribute(Attr.searchSuccess, true);
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

}