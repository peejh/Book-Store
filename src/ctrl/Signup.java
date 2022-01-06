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

import bean.AddressBean;
import bean.StoreUserBean;
import model.Hub;
import strings.Attr;
import strings.Country;
import strings.Pages;
import util.PasswordStorage.CannotPerformOperationException;

/**
 * Servlet implementation class Signup
 */
@WebServlet("/signup")
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ServletContext context;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Signup() {
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
		removeSessionAttributes(request, Attr.accountTaken, Attr.signupSuccess, Attr.orderSuccess);
		
		String dispatch = Pages.SIGNUP;				
		String exceptCountry3Code = "CAN";
		String exceptCountryName = Country.allWith3Codes(null).get(exceptCountry3Code);
		mySession.setAttribute(Attr.allCountry, Country.allWith3Codes(exceptCountry3Code));
		mySession.setAttribute(Attr.exceptCountry3Code, exceptCountry3Code);
		mySession.setAttribute(Attr.exceptCountryName, exceptCountryName);
		String logIn = request.getParameter("logIn");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String name = request.getParameter("firstName") + " " + request.getParameter("lastName");
		
		if (logIn != null) {
			HashMap<String, StoreUserBean> userWithEmail = new HashMap<String, StoreUserBean>();
			
			try {
				userWithEmail = myHub.retrieveAllUsersByEmail();
			} catch (SQLException e) {
				System.out.println("Failed to retrieve users by email ...");
			}
			
			if (userWithEmail.containsKey(email)) {
				mySession.setAttribute(Attr.accountTaken, true);
			}
			else {
				try {
					myHub.addStoreUser(email, name, password);
					userWithEmail = myHub.retrieveAllUsersByEmail();
				} catch (SQLException | CannotPerformOperationException e) {
					System.out.println("Failed to add user email " + email + "...");
				}
				
				String aptNo = request.getParameter("aptNo");
				String streetNo = request.getParameter("streetNo");
				String streetName = request.getParameter("streetName");
				String city = request.getParameter("city");
				String province = request.getParameter("province");
				String postalCode = request.getParameter("postalCode");
				String country = request.getParameter("country");
				boolean nullAddress = streetNo.isEmpty() || streetName.isEmpty() || city.isEmpty() ||
									  province.isEmpty() || postalCode.isEmpty();
				
				if (!nullAddress) {
					int uid = userWithEmail.get(email).getId();
					int aptNum = aptNo.isEmpty() ? 0 : Integer.parseInt(aptNo);
					int streetNum = Integer.parseInt(streetNo);
					AddressBean newAddress = new AddressBean(aptNum, streetNum, streetName, city, province, country, postalCode, uid);
					try {
						myHub.addAddress(newAddress);
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println("Failed to add address: " + newAddress.toString());
					}
				}

				mySession.setAttribute(Attr.signupSuccess, true);
				dispatch = Pages.INDEX;
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

}