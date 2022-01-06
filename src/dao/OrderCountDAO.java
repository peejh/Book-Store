package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class OrderCountDAO {

	DataSource ds;
	
	public OrderCountDAO() throws ClassNotFoundException{
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/BookStore");
		} catch (NamingException e){
			e.printStackTrace();
		}
	}
	
	public int retrieveOrderCount() throws SQLException {
		String query = "SELECT * FROM ORDERCOUNT";
		int orderCount = 0;
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		if (r.next()) {	
			orderCount = r.getInt("order_count");	
		}
		r.close();
		p.close();
		con.close();
		return orderCount;
	}
	
	public int updateOrderCount(int newValue) throws SQLException {
		int result;
		String query = "UPDATE ORDERCOUNT SET order_count = ?";
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setInt(1, newValue);	
		result = p.executeUpdate();
		p.close();
		con.close();			
		return result;			
	}
}
