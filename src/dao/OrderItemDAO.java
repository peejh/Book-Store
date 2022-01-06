package dao;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import bean.BookBean;
import bean.OrderItemBean;
import util.Tuple;

public class OrderItemDAO {
	
	DataSource ds;
	
	public OrderItemDAO() throws ClassNotFoundException{
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/BookStore");
		} catch (NamingException e){
			e.printStackTrace();
		}
	}
	
	public HashMap<Tuple<Integer, Integer>, OrderItemBean> retrieveAll() throws SQLException{
		String query = "SELECT * FROM ORDERITEM";
		HashMap<Tuple<Integer, Integer>, OrderItemBean> rv = new HashMap<Tuple<Integer, Integer>, OrderItemBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int orderId = r.getInt("order_id");
			int bookId = r.getInt("book_id");
			int quantity = r.getInt("quantity");			
			OrderItemBean orderItem = new OrderItemBean(orderId, bookId, quantity);
			rv.put(new Tuple<Integer, Integer>(orderId, bookId), orderItem);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
	
	public int addOrderItem(OrderItemBean orderItem) throws SQLException {
		int result;
		
		String query = "INSERT INTO ORDERITEM (order_id, book_id, quantity)" + 
					   "	VALUES(?,?,?)";
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);

		p.setInt(1, orderItem.getOrderId());
		p.setInt(2, orderItem.getBookId());
		p.setInt(3, orderItem.getQuantity());
		
		result = p.executeUpdate();
		p.close();
		con.close();
		
		return result;
	}
	
	/***
	 * 
	 * NOTES: invoked when user deletes order item or quantity of order item is
	 * 		  updated to 0
	 * 
	 * @param orderId
	 * @param bookId
	 * @return
	 * @throws SQLException
	 */
	public int deleteOrderItem(int orderId, int bookId) throws SQLException {
		int result;
		String query = "  DELETE FROM ORDERITEM " + 
					   "	WHERE order_id = ?  " +
					   "	AND   book_id  = ?  ";
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setInt(1, orderId);
		p.setInt(2, bookId);
		result = p.executeUpdate();
		p.close();
		con.close();
		return result;				
	}
	
	/***
	 * 
	 * NOTES: invoked when quantity of order item is changed
	 * 			or when session cart is
	 * 
	 * @param orderId
	 * @param bookId
	 * @param newQuantity
	 * @return
	 * @throws SQLException
	 */
	public int updateOrderItem(int orderId, int bookId, int newQuantity) throws SQLException {
		int result;
		String query = "  UPDATE ORDERITEM     " +
					   "	SET   quantity = ? " +
					   "	WHERE order_id = ? " +
					   "	AND   book_id  = ? ";
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setInt(1, newQuantity);
		p.setInt(2, orderId);
		p.setInt(3, bookId);
		result = p.executeUpdate();
		p.close();
		con.close();			
		return result;		
	}
	
	public Map<Tuple<Integer, Integer>, OrderItemBean> getOrderById(int id) throws SQLException{
		String query = "SELECT * FROM ORDERITEM WHERE ORDER_ID = " + id;
		Map<Tuple<Integer, Integer>, OrderItemBean> rv = new HashMap<Tuple<Integer, Integer>, OrderItemBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int orderId = r.getInt("order_id");
			int bookId = r.getInt("book_id");
			int quantity = r.getInt("quantity");			
			OrderItemBean orderItem = new OrderItemBean(orderId, bookId, quantity);
			rv.put(new Tuple<Integer, Integer>(orderId, bookId), orderItem);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
	
	public String getJsonByOrderId(int id) throws SQLException, ClassNotFoundException {
		Map<Tuple<Integer, Integer>, OrderItemBean> rv = getOrderById(id);
		BookDAO bd = new BookDAO();
		StringWriter sw = new StringWriter();
		sw.write("\n");
		int count = 0;
		for (OrderItemBean oib : rv.values()) {
			Map<String, BookBean> book = bd.getProductById(oib.getBookId());
			String bookTitle = "";
			for (BookBean bb : book.values()) {
				bookTitle = bb.getTitle();
			}
			count++;
			JsonObject value = Json.createObjectBuilder().add("ORDER_ID", oib.getOrderId()).add("BOOK_ID", oib.getBookId()).add
					("BOOK_TITLE", bookTitle).add("QUANTITY", Integer.toString(oib.getQuantity())).build();
			sw.write(value.toString());
			if (count < rv.size()) {
				sw.write(",\n");
			}
		}
		return sw.toString();
	}
}
