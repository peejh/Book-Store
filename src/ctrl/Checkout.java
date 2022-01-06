package ctrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

import bean.AddressBean;
import bean.OrderItemBean;
import bean.OrdersBean;
import bean.StoreUserBean;
import model.CartItem;
import model.Hub;
import model.OrderStatus;
import strings.Attr;
import strings.Country;
import strings.Pages;

/**
 * Servlet implementation class Checkout
 */
@WebServlet("/checkout")
public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ServletContext context;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Checkout() {
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
		removeSessionAttributes(request, Attr.signupSuccess, Attr.orderSuccess);		

		String dispatch = Pages.LOGIN;
		@SuppressWarnings("unchecked")
		HashMap<String, CartItem> sessionCart = (HashMap<String, CartItem>) mySession.getAttribute(Attr.sessionCart);
		@SuppressWarnings("unchecked")
		HashMap<String, CartItem> userCart = (HashMap<String, CartItem>) mySession.getAttribute(Attr.userCart);
		@SuppressWarnings("unchecked")
		HashMap<String, CartItem> userDBCart = (HashMap<String, CartItem>) mySession.getAttribute(Attr.userDBCart);
		StoreUserBean user = (StoreUserBean) mySession.getAttribute(Attr.user);
		Boolean userLoggedIn = (Boolean) mySession.getAttribute(Attr.userLoggedIn);
		ArrayList<AddressBean> storedAddress = null;
		HashMap<Integer, AddressBean> addresses = new HashMap<Integer, AddressBean>();

		// CHECKOUT action requests
		String placeOrder = request.getParameter("placeOrder");
		String addAddress = request.getParameter("addAddress");
		
		// for select country input
		String exceptCountry3Code = "CAN";
		String exceptCountryName = Country.allWith3Codes(null).get(exceptCountry3Code);
		mySession.setAttribute(Attr.allCountry, Country.allWith3Codes(exceptCountry3Code));
		mySession.setAttribute(Attr.exceptCountry3Code, exceptCountry3Code);
		mySession.setAttribute(Attr.exceptCountryName, exceptCountryName);		

		if (userLoggedIn != null && userLoggedIn) {
			dispatch = Pages.CHECKOUT;
			
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
			//-----------------------end of MERGING CARTS------------------------
						
			if (addAddress != null) {
				AddressBean addressToAdd = cleanAddressInput(request);
				addressToAdd.setUserId(user.getId());
				//System.out.println(addressToAdd.toString());
				
				try {
					myHub.addAddress(addressToAdd);
				} catch (SQLException e) {
					System.out.println("Failed to add new address for user " + user.getId() + "...");
				}
	
			}		
			else if (placeOrder != null) {			
				try {
					String address = request.getParameter("address");
					int addressId = (address.isEmpty()) ? 0 : Integer.parseInt(address);
					
					OrdersBean cart = myHub.retrieveOrderCartOfUser(user.getId());					
					int orderId = 0;
					if (cart != null) {
						orderId = cart.getId();
					}
					else { // TODO: temporary fix for when user makes another order
						   //			without logging in or out
						orderId = myHub.addOrder(user.getId());
						mySession.setAttribute(Attr.cartOrderId, orderId);
						mySession.setAttribute(Attr.userDBCart, new HashMap<String, CartItem>());
					}
					System.out.println("ORDER ID: " + orderId);
					// add each cart item from the session cart to the database
					// the following is actually the same logic from of storing
					// the cart during logout					
					// the only difference is we update the order status
					//--------------------------------------------------------------------
					// the following fulfills the project requirement of denying
					// every 3rd order
					int orderCount = myHub.retrieveOrderCount();
					System.out.println("Current order count is " + orderCount + "...");
					orderCount++;
					myHub.updateOrderCount(orderCount);
					if (orderCount % 3 == 0) {
						myHub.updateStatus(orderId, addressId, OrderStatus.DENIED);
						// set attribute 
						mySession.setAttribute(Attr.orderSuccess, false);
					}
					else {
						myHub.updateStatus(orderId, addressId, OrderStatus.ORDERED);
						// set attribute 
						mySession.setAttribute(Attr.orderSuccess, true);
					}
					//--------------------------------------------------------------------
					// save order to database
					storeCart(request, orderId);
					// clear all carts
					removeSessionAttributes(request,
										    Attr.userCart,
										    Attr.cartOrderId,
										    Attr.userDBCart,
										    Attr.sessionCart
										   );
					// redirect to welcome page
					dispatch = Pages.INDEX;
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Failed to place order...");
				}	
			}
			
			// for address input
			try {
				storedAddress = myHub.retrieveAddressesOfUser(user.getId());
				for (AddressBean a : storedAddress) {
					addresses.put(a.getId(), a);
				}
				mySession.setAttribute(Attr.addresses, addresses);
			} catch (SQLException e) {
				System.out.println("Failed to retrieve address(es) for user " + user.getId() + "...");
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

	private void storeCart(HttpServletRequest request, int orderId) throws SQLException {
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
				myHub.updateOrderItem(orderId, 
									  sessionCart.get(isbn).getBook().getId(),
									  sessionCart.get(isbn).getQuantity());
			}
			else { // order item has been deleted
				// delete in database
				myHub.deleteOrderItem(orderId, sessionCart.get(isbn).getBook().getId());
			}
		}
		
		// add items not previously there
		for (String isbn : sessionCart.keySet()) {
			if (!userDBCart.containsKey(isbn)) {
				OrderItemBean orderItem = new OrderItemBean(orderId,
														    sessionCart.get(isbn).getBook().getId(),
														    sessionCart.get(isbn).getQuantity());
				myHub.addOrderItem(orderItem);							
			}
		}			
	}
	
	private AddressBean cleanAddressInput(HttpServletRequest request) {
		String aptNo = request.getParameter("aptNo");
		int aptNum = aptNo.isEmpty() ? 0 : Integer.parseInt(aptNo);
		int streetNum = Integer.parseInt(request.getParameter("streetNo"));
		String streetName = request.getParameter("streetName").replaceAll("\\s+", " ").trim();
		String city = request.getParameter("city").replaceAll("\\s+", " ").trim();
		String province = request.getParameter("province").replaceAll("\\s+", " ").trim();
		String country = Country.allWith3Codes(null).get(request.getParameter("country"));
		String postalCode = request.getParameter("postalCode").replaceAll("\\s+", " ").trim().toUpperCase();
		
		streetName = capitalizeEveryFirstLetter(streetName);
		city = capitalizeEveryFirstLetter(city);
		province = capitalizeEveryFirstLetter(province);
		if (!postalCode.contains(" ")) {
			postalCode = postalCode.substring(0, 3) + " " + postalCode.substring(3, postalCode.length());
		}

		return new AddressBean(aptNum, streetNum, streetName, city, province, country, postalCode, 0);
	}
	
	private String capitalizeEveryFirstLetter(String in) {
		String out;
		
		// split into words
		String[] words = in.split(" ");

		// capitalize each word
		for (int i = 0; i < words.length; i++)
		{
		    words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
		}

		// rejoin back into a sentence
		out = String.join(" ", words);
		
		return out;
	}
	
}
