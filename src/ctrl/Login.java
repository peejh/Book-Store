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
import bean.OrderItemBean;
import bean.OrdersBean;
import bean.StoreUserBean;
import model.CartItem;
import model.Hub;
import model.OrderStatus;
import strings.Attr;
import strings.Pages;
import strings.Services;
import util.PasswordStorage.CannotPerformOperationException;
import util.PasswordStorage.InvalidHashException;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ServletContext context;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
		System.out.println(request.getQueryString());
		// housekeeping for every request
		HttpSession mySession = request.getSession();
		Hub myHub = (Hub) context.getAttribute(Attr.myHub);
		removeSessionAttributes(request, Attr.invalidLogin, Attr.signupSuccess, Attr.orderSuccess);		

		String dispatch = Pages.LOGIN;
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		boolean loginSuccess = false;
	
		if (email != null && password != null) {
			try {
				loginSuccess = myHub.validateUser(email, password);
			} catch (SQLException e) {
				System.out.println("Database access failed during login...");
			} catch (CannotPerformOperationException e) {
				System.out.println("Verification process failed during login...");
			} catch (InvalidHashException e) {
				System.out.println("Hash format not recognized...");
			}
			
			if (loginSuccess) {
				StoreUserBean user = null;
				try {
					user = myHub.retrieveUserByEmail(email);
					mySession.setAttribute(Attr.user, user);
					mySession.setAttribute(Attr.userLoggedIn, true);
					
				//-------retrieve user's cart if it exists-------
					HashMap<String, CartItem> userCart = new HashMap<String, CartItem>();
					HashMap<String, CartItem> userDBCart = new HashMap<String, CartItem>();
					//HashMap<Integer, Integer> userOrderItems = null; // mapping of bookId to quantity
					
					// find order by user with status CART
					// NOTE: this can be a database operation
					//			not sure which one is more efficient
					Integer cartOrderId = 0;
					HashMap<Integer, OrdersBean> orders = myHub.retrieveAllOrders();
					for (OrdersBean order : orders.values()) {
						if (order.getUserId() == user.getId() && order.getStatus() == OrderStatus.CART) {
							cartOrderId = order.getId();
							userCart = new HashMap<String, CartItem>();
							break;	// we assume that there's only one
						}
					}
					
					if (cartOrderId != 0) {	// if CART is found
						userDBCart = new HashMap<String, CartItem>();
						//userOrderItems = new HashMap<Integer, Integer>();
						// find items of orderId
						for (OrderItemBean oi : myHub.retrieveItemsOfOrder(cartOrderId)) {
							BookBean b = myHub.retrieveBookById(oi.getBookId());
							userCart.put(b.getIsbn(), new CartItem(b, oi.getQuantity()));
							
							// temporary fix
							userDBCart.put(b.getIsbn(), new CartItem(b, oi.getQuantity()));
							//userOrderItems.put(b.getId(), oi.getQuantity());
						}
					}
					else { // if NO CART found
						cartOrderId = myHub.addOrder(user.getId()); // create a CART
					}
					
					mySession.setAttribute(Attr.cartOrderId, cartOrderId); // needed for updating ORDERITEM table
																		   //	during logout		
					mySession.setAttribute(Attr.userCart, userCart);
					mySession.setAttribute(Attr.userDBCart, userDBCart);
					//mySession.setAttribute("userOrderItems", userOrderItems);
				} catch (SQLException e) {
					System.out.println("Failed to retrieve user with email " + email + "...");
					e.printStackTrace();
				}
			
				if (getReferrer(request).equals(Pages.CHECKOUT))
					dispatch = Services.checkout;
				else
					dispatch = Pages.INDEX;
			}
			else {
				mySession.setAttribute(Attr.invalidLogin, true);
			}
		}
	
		RequestDispatcher rd = request.getRequestDispatcher(dispatch);
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
	
	private String getReferrer(HttpServletRequest request) {
		String[] slash = request.getHeader("referer").split("/");
		System.out.println(slash[slash.length-1]);
		String[] query = slash[slash.length-1].split("\\?");
		System.out.println(query[0]);
		return "/" + query[0] + ".jspx";
	}

}
