package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import bean.InventoryBean;

public class InventoryDAO {
	
	DataSource ds;
	
	public InventoryDAO() throws ClassNotFoundException{
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/BookStore");
		} catch (NamingException e){
			e.printStackTrace();
		}
	}
	
	public HashMap<Integer, InventoryBean> retrieveAll() throws SQLException{
		String query = "SELECT * FROM INVENTORY";
		HashMap<Integer, InventoryBean> rv = new HashMap<Integer, InventoryBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int bookId = r.getInt("book_id");
			int inStock = r.getInt("in_stock");
			InventoryBean inventory = new InventoryBean(bookId, inStock);
			rv.put(bookId, inventory);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
	
	/***
	 * 
	 * NOTES: invoked when orders are checked out to reflect updated stock levels
	 * 
	 * @param bookId
	 * @param newStock
	 * @return
	 * @throws SQLException
	 */
	public int updateStock(int bookId, int newStock) throws SQLException {
		int result;
		String query = "UPDATE BOOK  " +
					   "SET in_stock = ? " +
					   "WHERE book_id = ?";
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setInt(1, newStock);
		p.setInt(2, bookId);
		result = p.executeUpdate();
		p.close();
		con.close();			
		return result;
	}
}
