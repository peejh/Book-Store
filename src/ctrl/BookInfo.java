package ctrl;

import java.io.IOException;
import java.sql.SQLException;

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
 * Servlet implementation class BookInfo
 */
@WebServlet("/bookinfo")
public class BookInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ServletContext context;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookInfo() {
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
		
		String dispatchPage = Pages.BOOKINFO;
		BookBean book = null;
		String isbn = request.getParameter("isbn");
		
		if (isbn != null) {
			try {
				book = myHub.retrieveAllBooks().get(isbn);
			} catch (SQLException e) {
				System.out.println("Failed to retrieve book with isbn " + isbn + "...");
			}
			if (book != null)
				mySession.setAttribute(Attr.isbnBookInfo, book);
			else
				dispatchPage = Pages.NOTFOUND;
		}
		else {
			dispatchPage = Pages.WRONG;
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
