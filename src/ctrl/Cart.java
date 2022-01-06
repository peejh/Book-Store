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
import model.CartItem;
import model.Hub;
import strings.Attr;
import strings.Pages;


/**
 * Servlet implementation class Cart
 */
@WebServlet("/cart")
public class Cart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ServletContext context;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Cart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
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
		
		String dispatchPage = Pages.CART;
		@SuppressWarnings("unchecked")
		HashMap<String, CartItem> sessionCart = (HashMap<String, CartItem>) mySession.getAttribute(Attr.sessionCart);
		@SuppressWarnings("unchecked")
		HashMap<String, CartItem> userCart = (HashMap<String, CartItem>) mySession.getAttribute(Attr.userCart);
		// REQUEST PARAMETERS for CART service
		String isbn = request.getParameter("isbn");
		String quantityParam = request.getParameter("quantity");
		String removeItem = request.getParameter("removeItem");
		
		//---------------------------MERGING CARTS---------------------------
		// One of the four conditions will happen
		if (userCart != null && sessionCart != null) {	// both carts exist
			// merge both into session cart
			for (CartItem ci : userCart.values()) {
				sessionCart.put(ci.getBook().getIsbn(), ci);
			}
			// delete the user cart retrieved during login
			mySession.removeAttribute(Attr.userCart);
			// NOTE: database copy of user cart is not deleted
			//			and is only updated during logout
		}
		else if (userCart != null && sessionCart == null) {	// only the retrieved user cart exist
			// copy the user cart to session cart
			sessionCart = new HashMap<String, CartItem>(userCart);
			// delete the user cart
			mySession.removeAttribute(Attr.userCart);
		}
		else if (sessionCart == null) {	// there is no user cart and session cart
			// initialize a session cart
			sessionCart = new HashMap<String, CartItem>();
		}
		//------------------------------------------------------------------
		
		// ACTION REQUESTS for CART servlet
		//----------------remove an item from cart--------------------------
		// request uri has both isbn and removeitem parameters
		if (removeItem != null) {
			System.out.println("Removing item " + isbn + " from cart...");
			sessionCart.remove(isbn);
		}
		//----------------add an item to cart-------------------------------
		// request uri only have isbn parameter
		else if (isbn != null ) {
			System.out.println("Adding item " + isbn + " to cart...");
			BookBean book = null;
			try {
				book = myHub.retrieveBookByIsbn(isbn);
			} catch (SQLException e) {
				System.out.println("Failed to retrieve book with isbn " + isbn + "...");
			}

			int quantity = (quantityParam!=null)?Integer.parseInt(quantityParam):1;
			
			if (sessionCart.containsKey(isbn)) {
				int oldValue = sessionCart.get(isbn).getQuantity();
				quantity += oldValue;
			}

			sessionCart.put(isbn, new CartItem(book, quantity));			
		}
		//-----------------view cart----------------------------------------
		// there is no parameter so just display the session cart
		//------------------------------------------------------------------
		
		// set the attribute for session cart
		mySession.setAttribute(Attr.sessionCart, sessionCart);
		
		double total = 0.0;
		for (CartItem c : sessionCart.values()) {
			total += c.getSubtotal();
		}
		total = ((int) (total * 100)) / 100.0;
		// set the attribute for the cart total
		mySession.setAttribute(Attr.sessionCartTotal, total);
		
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
