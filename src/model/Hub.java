package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import bean.AddressBean;
import bean.BookBean;
import bean.CategoryBean;
import bean.OrderItemBean;
import bean.OrdersBean;
import bean.StoreUserBean;
import dao.AddressDAO;
import dao.BookDAO;
import dao.CategoryDAO;
import dao.InventoryDAO;
import dao.OrderCountDAO;
import dao.OrderItemDAO;
import dao.OrdersDAO;
import dao.StoreUserDAO;
import util.PasswordStorage;
import util.Tuple;
import util.PasswordStorage.CannotPerformOperationException;
import util.PasswordStorage.InvalidHashException;

public class Hub {

	private static Hub instance;

	private static final int IDSPAN = 1000000;			// 1 million users
	private static final int USER_ID = 1 * IDSPAN;		// user ids start with 1
	private static final int OIDSPAN = 100000000;		// 100 million orders
	private static final int ORDERS_ID = 2 * OIDSPAN;	// order ids start with 2
	
	private AddressDAO 		addresses;
	private BookDAO 		books;
	private CategoryDAO 	categories;
	private InventoryDAO	inventory;
	private OrderItemDAO 	orderItems;
	private OrdersDAO 		orders;
	private StoreUserDAO 	users;
	private OrderCountDAO	orderCount;
	
	private Hub() throws ClassNotFoundException {
		addresses = new AddressDAO();
		books = new BookDAO();
		categories = new CategoryDAO();
		inventory = new InventoryDAO();
		orderItems = new OrderItemDAO();
		orders = new OrdersDAO();
		users = new StoreUserDAO();
		orderCount = new OrderCountDAO();
	}
	
	public static Hub getInstance() throws ClassNotFoundException {
		if (instance == null) {
			instance = new Hub();
		}
		return instance;
	}
	
//____________________________ADDRESS OPERATIONS____________________________

	public HashMap<Integer, AddressBean> retrieveAllAddresses() throws SQLException {
		System.out.println("Retrieving all addresses...");
		return this.addresses.retrieveAll();
	}
	
	public ArrayList<AddressBean> retrieveAddressesOfUser(int userId) throws SQLException {
		System.out.println("Retrieving addresses of user " + userId + "...");
		ArrayList<AddressBean> result = new ArrayList<AddressBean>();
		
		for (AddressBean addr : addresses.retrieveAll().values()) {
			if (addr.getUserId() == userId)
				result.add(addr);
		}
		
		return result;
	}
	
	public int addAddress(AddressBean address) throws SQLException {
		System.out.println("Adding address " + 
							address.getAptNum() + "-" +
							address.getStreetNum() + " " +
							address.getStreetName() + " " +
							address.getCity() + " " +
							address.getProvince() + " " +
							address.getCountry() + " " +
							address.getPostalCode() + " " +
						   "for user id " + address.getUserId() + "...");
		return this.addresses.addAddress(address);
	}
		
	public int deleteAddress(int id) throws SQLException {
		System.out.println("Deleting address with id " + id + "...");
		return this.addresses.deleteAddress(id);
	}
	
//____________________________BOOK OPERATIONS____________________________
	
	public HashMap<String, BookBean> retrieveAllBooks() throws SQLException {
		System.out.println("Retrieving all books...");
		return this.books.retrieveAll();
	}
	
	public HashMap<String, BookBean> retrieveBooksByCategory(BookCategory category) throws SQLException {
		System.out.println("Retrieving category " + category.toString() + "...");
		HashMap<String, BookBean> result = null;
		
		switch (category) {
		case Math:
			result = this.books.retrieveByCategory(1);
			break;
		case Science:
			result = this.books.retrieveByCategory(2);
			break;
		case Engineering:
			result = this.books.retrieveByCategory(3);
			break;			
		case Programming:
			result = this.books.retrieveByCategory(4);
			break;
		case History:
			result = this.books.retrieveByCategory(5);
			break;
		case Art:
			result = this.books.retrieveByCategory(6);
			break;
		case Fiction:
			result = this.books.retrieveByCategory(7);
			break;
		case NonFiction:
			result = this.books.retrieveByCategory(8);
			break;
		case SelfHelp:
			result = this.books.retrieveByCategory(9);
			break;
		}
			
		return result;
	}
	
	public BookBean retrieveBookByIsbn(String isbn) throws SQLException{
		System.out.println("Retrieving book with isbn " + isbn + "...");
		return this.books.retrieveBookByIsbn(isbn);
	}
	
	public BookBean retrieveBookById(int id) throws SQLException{
		System.out.println("Retrieving book with id " + id + "...");
		return this.books.retrieveBookById(id);
	}
	
	public HashMap<String, BookBean> retrieveAllBooksByKeyWords(String keywords) throws SQLException {
		System.out.println("Retrieving all books with keywords " + keywords + "...");
		return this.books.retrieveByKeyWords(keywords);
	}	
	
//____________________________CATEGORY OPERATIONS____________________________
//	probably not needed	
	
	public HashMap<Integer, CategoryBean> retrieveCategories() throws SQLException {
		System.out.println("Retrieving all categories...");
		return this.categories.retrieveAll();
	}
	
//____________________________INVENTORY OPERATIONS____________________________

	public int retrieveStock(int bookId) throws SQLException {
		System.out.println("Retrieving stock for book " + bookId + "...");
		return this.inventory.retrieveAll().get(bookId).getInStock();
	}
	
	public int updateStock(int bookId, int newStock) throws SQLException {
		System.out.println("Updating stock for book" + bookId + "...");		
		return this.inventory.updateStock(bookId, newStock);
	}
	
//____________________________ORDERITEM OPERATIONS____________________________

	public ArrayList<OrderItemBean> retrieveItemsOfOrder(int orderId) throws SQLException {
		System.out.println("Retrieving items for order " + orderId + "...");
		ArrayList<OrderItemBean> result = new ArrayList<OrderItemBean>();
		HashMap<Tuple<Integer, Integer>, OrderItemBean> current = this.orderItems.retrieveAll();
		
		for (Tuple t : current.keySet()) {
			Integer tupleOrderId = (Integer) t.x;
			if (tupleOrderId.intValue() == orderId)
				result.add(current.get(t));
		}
		
		return result;
	}
	
	public int addOrderItem(OrderItemBean orderItem) throws SQLException {
		System.out.println("Adding book " + orderItem.getBookId() + " to order " + orderItem.getOrderId() + "...");
		return this.orderItems.addOrderItem(orderItem);
	}

	public int deleteOrderItem(int orderId, int bookId) throws SQLException {
		System.out.println("Deleting book " + bookId + " from order " + orderId + "...");
		return this.orderItems.deleteOrderItem(orderId, bookId);		
	}
	
	public int updateOrderItem(int orderId, int bookId, int newQuantity) throws SQLException {
		System.out.println("Updating order item <" + orderId + "," + bookId + "> to quantity " + newQuantity + "...");
		return this.orderItems.updateOrderItem(orderId, bookId, newQuantity);
	}

//____________________________ORDER OPERATIONS____________________________
	
	public HashMap<Integer, OrdersBean> retrieveAllOrders() throws SQLException {
		System.out.println("Retrieving all orders...");
		return this.orders.retrieveAll();
	}
	
	public int addOrder(int userId) throws SQLException {
		System.out.println("Creating CART for user " + userId + "...");
		HashMap<Integer, OrdersBean> current = this.orders.retrieveAll();
		ThreadLocalRandom gen = ThreadLocalRandom.current();
		int orderId = 0;
		
		while (orderId == 0) {	// keep looking for an unused order id
			orderId = ORDERS_ID + gen.nextInt(0, OIDSPAN);
			System.out.println("Generated order ID: " + orderId);
			if (current.containsKey(orderId)) {
				orderId = 0;
			}
		}
		
		System.out.println("Adding order record " + orderId + "...");
		//Timestamp now = new Timestamp(System.currentTimeMillis());
		orders.addOrder(new OrdersBean(orderId, userId, null, 0, OrderStatus.CART));
		
		return orderId;	// unlike other methods, we're ignoring the return
						//	value of the database operation for the time being
	}
	
	public int updateStatus(int orderId, int addressId, OrderStatus status) throws SQLException {
		System.out.println("Updating order " + orderId + " to " + status.toString() + "...");
		OrdersBean order;
		Timestamp now = new Timestamp(System.currentTimeMillis());
		if (status == OrderStatus.ORDERED)
			order = new OrdersBean(orderId, 0, now, addressId, status);
		else
			order = new OrdersBean(orderId, 0, null, 0, status);
				
		return this.orders.updateStatus(order);
	}
	
	public OrdersBean retrieveOrderCartOfUser(int userId) throws SQLException {
		System.out.println("Retrieving CART for user " + userId + "...");
		return this.orders.retrieveCartOfUser(userId);
	}
	
	public HashMap<Integer, OrdersBean> retrieveOrdersOfUser(int userId) throws SQLException {
		System.out.println("Retrieving ORDERS for user " + userId + "...");
		return this.orders.retrieveOrdersOfUser(userId);		
	}
	
//____________________________STOREUSER OPERATIONS____________________________
	
	public HashMap<String, StoreUserBean> retrieveAllUsersByEmail() throws SQLException{
		System.out.println("Retrieving all users by email...");
		return users.retrieveAllByEmail();
	}
	
	public StoreUserBean retrieveUserByEmail(String email) throws SQLException {
		System.out.println("Retrieving user with email " + email + "...");
		return users.retrieveUserByEmail(email);
	}
	
	public HashMap<Integer, StoreUserBean> retrieveAllUsersById() throws SQLException{
		System.out.println("Retrieving all users by id...");
		return users.retrieveAllById();
	}
	
	/***
	 * NOTES: Only used to create customer user accounts, not owners
	 */
	public int addStoreUser(String email, String name, String password) throws SQLException, CannotPerformOperationException {
		System.out.println("Creating BookHub account for email " + email + "...");
		HashMap<Integer, StoreUserBean> current = this.users.retrieveAllById();
		ThreadLocalRandom gen = ThreadLocalRandom.current();
		int userId = 0;
		
		while (userId == 0) {	// keep looking for an unused user id
			userId = USER_ID + gen.nextInt(0, IDSPAN);
			System.out.println("Generated user ID: " + userId);
			if (current.containsKey(userId)) {
				userId = 0;
			}
		}
		
		System.out.println("Adding user account " + userId + "...");	
		String hash = PasswordStorage.createHash(password);
		return users.addStoreUser(new StoreUserBean(userId, email, name, hash, 0, false));
	}
		
	public boolean validateUser(String email, String password) throws SQLException, CannotPerformOperationException, InvalidHashException {
		System.out.println("Validating login for " + email + "...");
		return users.validate(email, password);
	}
	
//___________________________________REST OPERATIONS____________________________________

	public String exportBookJsonById(int bid) throws SQLException {
		System.out.println("Exporting Json for Book ID: " + bid);
		return books.getJsonByProductId(bid);
	}
	
	public String exportOrderJsonById(int id) throws SQLException {
		System.out.println("Exporting Json for Order ID: " + id);
		return books.getJsonByProductId(id);
	}	
	
//___________________________________ORDERCOUNT OPERATIONS____________________________________

	public int retrieveOrderCount() throws SQLException {
		System.out.println("Retrieving order count...");
		return orderCount.retrieveOrderCount();
	}

	public int updateOrderCount(int newValue) throws SQLException {
		System.out.println("Updating order count to " + newValue + "...");
		return orderCount.updateOrderCount(newValue);
	}

}


