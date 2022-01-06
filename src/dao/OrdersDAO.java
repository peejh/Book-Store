package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import bean.OrdersBean;
import model.OrderStatus;

public class OrdersDAO {

	DataSource ds;
	
	public OrdersDAO() throws ClassNotFoundException{
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/BookStore");
		} catch (NamingException e){
			e.printStackTrace();
		}
	}
	
	public HashMap<Integer, OrdersBean> retrieveAll() throws SQLException{
		String query = "SELECT * FROM ORDERS";
		HashMap<Integer, OrdersBean> rv = new HashMap<Integer, OrdersBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int id = r.getInt("order_id");
			int userId = r.getInt("user_id");
			Timestamp orderTimestamp = r.getTimestamp("order_timestamp");
			int addressId = r.getInt("addr_id");
			OrderStatus status = OrderStatus.valueOf(r.getString("status"));
			OrdersBean order = new OrdersBean(id, userId, orderTimestamp, addressId, status);
			rv.put(id, order);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
	
	public OrdersBean retrieveCartOfUser(int userId) throws SQLException {
		String query = "SELECT * FROM ORDERS WHERE user_id = ? AND status = ?";
		OrdersBean cart = null;
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setInt(1, userId);
		p.setString(2, OrderStatus.CART.toString());
		ResultSet r = p.executeQuery();
		if (r.next()) {
			int id = r.getInt("order_id");
			Timestamp orderTimestamp = r.getTimestamp("order_timestamp");
			int addressId = r.getInt("addr_id");
			cart = new OrdersBean(id, userId, orderTimestamp, addressId, OrderStatus.CART);			
		}
		r.close();
		p.close();
		con.close();
		return cart;
	}
	
	public HashMap<Integer, OrdersBean> retrieveOrdersOfUser(int userId) throws SQLException {
		String query = "SELECT * FROM ORDERS WHERE user_id = ? AND status != ?";
		HashMap <Integer, OrdersBean> orders = new HashMap<Integer, OrdersBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setInt(1, userId);
		p.setString(2, OrderStatus.CART.toString());
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int id = r.getInt("order_id");
			Timestamp orderTimestamp = r.getTimestamp("order_timestamp");
			int addressId = r.getInt("addr_id");
			OrderStatus status = OrderStatus.valueOf(r.getString("status"));
			OrdersBean order = new OrdersBean(id, userId, orderTimestamp, addressId, status);
			orders.put(order.getId(), order);			
		}
		r.close();
		p.close();
		con.close();
		return orders;		
	}
	
	public int addOrder(OrdersBean order) throws SQLException {
		int result;
		
		String query = "INSERT INTO ORDERS (order_id, user_id, status)" + 
					   "	VALUES(?,?,?)";

		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		p.setInt(1, order.getId());
		p.setInt(2, order.getUserId());
		p.setString(3, order.getStatus().toString());
		
		result = p.executeUpdate();
		p.close();
		con.close();
		
		return result;		
	}
	
	/***
	 * 
	 * NOTES: in model method, take new status as parameter and update bean
	 * 
	 * @param order
	 * @return
	 * @throws SQLException 
	 */
	public int updateStatus(OrdersBean order) throws SQLException {
		int result;
		String query;
		Connection con = this.ds.getConnection();
		PreparedStatement p;
		
		// upon checkout i.e. ORDERED, some other fields are updated
		if (order.getStatus() == OrderStatus.ORDERED) {
			query = "  UPDATE ORDERS               " +
					"	SET   status 		  = ? ," +
					"		  order_timestamp = ? ," +
					"		  addr_id		  = ?  " +
					"	WHERE order_id		  = ?  ";
			p = con.prepareStatement(query);
			p.setString(1, order.getStatus().toString());
			p.setTimestamp(2, order.getOrderTimestamp());
			p.setInt(3, order.getAddrId());
			p.setInt(4, order.getId());
		}
		else {
			query = "  UPDATE ORDERS              " +
					"	SET   status 		  = ? " +
					"	WHERE order_id		  = ? ";
			p = con.prepareStatement(query);
			p.setString(1, order.getStatus().toString()); //
			p.setInt(2, order.getId());			
		}
		
		result = p.executeUpdate();
		p.close();
		con.close();			
		return result;			
	}
}
