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

import bean.OrderItemBean;
import bean.OrdersBean;
import bean.StoreUserBean;
import model.CartItem;
import model.Hub;
import strings.Attr;
import strings.Pages;

/**
 * Servlet implementation class Logout
 */
@WebServlet({"/logout", "/signout"})
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ServletContext context;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
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
		
		String dispatchPage = Pages.LOGIN;
		@SuppressWarnings("unchecked")
		HashMap<String, CartItem> sessionCart = (HashMap<String, CartItem>) mySession.getAttribute(Attr.sessionCart);
		@SuppressWarnings("unchecked")
		HashMap<String, CartItem> userDBCart = (HashMap<String, CartItem>) mySession.getAttribute(Attr.userDBCart);
		// store existing cart (if any) to database
		StoreUserBean user = (StoreUserBean) mySession.getAttribute(Attr.user);
		
		if (sessionCart != null && user != null) { 
			// perhaps the condition (user != null) is not needed since
			//	logout button is not available if user is not logged in
			try {
				OrdersBean cart = myHub.retrieveOrderCartOfUser(user.getId());
				int cartOrderId = 0;
				
				if (cart != null) {
					cartOrderId = cart.getId();
					System.out.println("Retrieved cart " + cartOrderId + "...");
				}
				
				// if there is NO stored CART for user
				if (cartOrderId == 0) {
					cartOrderId = myHub.addOrder(user.getId()); // create a CART
					// store each session cart item to database
					for (CartItem ci : sessionCart.values()) {
						OrderItemBean orderItem = new OrderItemBean(cartOrderId, ci.getBook().getId(), ci.getQuantity());
						myHub.addOrderItem(orderItem);
					}					
				}
				else { // if the CART already exist in the database
					storeCart(request, cartOrderId);
				}
			} catch (SQLException e) {
				System.out.println("Failed to create or update cart in database during logout...");
			}
		}
			
		removeSessionAttributes(request,
								Attr.user,
								Attr.cartOrderId,
								Attr.userLoggedIn,
								Attr.userCart,
								Attr.userDBCart,
								Attr.sessionCart
							   );
		
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
	
	private void storeCart(HttpServletRequest request, int cartOrderId) throws SQLException {
		HttpSession mySession = request.getSession();
		Hub myHub = (Hub) context.getAttribute(Attr.myHub);
		@SuppressWarnings("unchecked")
		HashMap<String, CartItem> sessionCart = (HashMap<String, CartItem>) mySession.getAttribute(Attr.sessionCart);
		@SuppressWarnings("unchecked")
		HashMap<String, CartItem> userDBCart = (HashMap<String, CartItem>) mySession.getAttribute(Attr.userDBCart);
		
		// THREE actions to consider for each orderItem of user for given orderId
		// - delete if it doesn't exist in session cart
		// - update quantity if it's in the sesion cart
		// - add to database if no orderItem and session cartItem match
		
		// update existing items
		for (String isbn : userDBCart.keySet()) {
			if (sessionCart.containsKey(isbn)) { // order item still exist
				// update in database
				myHub.updateOrderItem(cartOrderId, 
									  sessionCart.get(isbn).getBook().getId(),
									  sessionCart.get(isbn).getQuantity());
			}
			else { // order item has been deleted
				// delete in database
				myHub.deleteOrderItem(cartOrderId, userDBCart.get(isbn).getBook().getId());
			}
		}
		
		// add items not previously there
		for (String isbn : sessionCart.keySet()) {
			if (!userDBCart.containsKey(isbn)) {
				OrderItemBean orderItem = new OrderItemBean(cartOrderId,
														    sessionCart.get(isbn).getBook().getId(),
														    sessionCart.get(isbn).getQuantity());
				myHub.addOrderItem(orderItem);							
			}
		}			
	}

}
