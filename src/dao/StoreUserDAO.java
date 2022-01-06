package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import bean.StoreUserBean;
import util.PasswordStorage;
import util.PasswordStorage.CannotPerformOperationException;
import util.PasswordStorage.InvalidHashException;

public class StoreUserDAO {

	DataSource ds;
	
	public StoreUserDAO() throws ClassNotFoundException{
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/BookStore");
		} catch (NamingException e){
			e.printStackTrace();
		}
	}
	
	public HashMap<String, StoreUserBean> retrieveAllByEmail() throws SQLException{
		String query = "SELECT * FROM STOREUSER";
		HashMap<String, StoreUserBean> rv = new HashMap<String, StoreUserBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int id = r.getInt("user_id");
			String email = r.getString("email");
			String name = r.getString("name");
			String password = r.getString("password");
			Integer phone = r.getInt("phone");
			boolean isOwner = r.getBoolean("is_owner");
			StoreUserBean user = new StoreUserBean(id, email, name, password, phone, isOwner);
			rv.put(email, user);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
	
	public StoreUserBean retrieveUserByEmail(String email) throws SQLException{
		String query = "SELECT * FROM STOREUSER WHERE email = ?";
		StoreUserBean user = null;
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setString(1, email);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int id = r.getInt("user_id");
			String name = r.getString("name");
			String password = r.getString("password");
			Integer phone = r.getInt("phone");
			boolean isOwner = r.getBoolean("is_owner");
			user = new StoreUserBean(id, email, name, password, phone, isOwner);
		}
		r.close();
		p.close();
		con.close();
		return user;
	}
	
	public HashMap<Integer, StoreUserBean> retrieveAllById() throws SQLException{
		String query = "SELECT * FROM STOREUSER";
		HashMap<Integer, StoreUserBean> rv = new HashMap<Integer, StoreUserBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int id = r.getInt("user_id");
			String email = r.getString("email");
			String name = r.getString("name");
			String password = r.getString("password");
			Integer phone = r.getInt("phone");
			boolean isOwner = r.getBoolean("is_owner");
			StoreUserBean user = new StoreUserBean(id, email, name, password, phone, isOwner);
			rv.put(id, user);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
	
	/**
	 * 
	 * NOTES: invoked when new account is created
	 * 
	 * @param storeUser
	 * @return
	 * @throws SQLException
	 */
	public int addStoreUser(StoreUserBean storeUser) throws SQLException {
		int result;
		
		String query = "INSERT INTO STOREUSER (user_id, email, name, password, phone, is_owner)" + 
					   "	VALUES(?,?,?,?,?,?)";
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setInt(1, storeUser.getId());
		p.setString(2, storeUser.getEmail());
		p.setString(3, storeUser.getName());
		p.setString(4, storeUser.getPassword());
		short phone = storeUser.getPhone().shortValue();
		if (phone != 0)
			p.setShort(5, phone);
		else
			p.setShort(5, (short) 0);
		p.setBoolean(6, storeUser.isOwner());
		
		result = p.executeUpdate();
		p.close();
		con.close();
		
		return result;
	}
		
	/***
	 * 
	 * NOTES: the only fields that can be updated are name and phone
	 * 
	 * @param storeUser
	 * @return
	 * @throws SQLException
	 */
	public int updateStoreUser(StoreUserBean storeUser) throws SQLException {
		int result;
		String query = "  UPDATE STOREUSER     " +
					   "	SET   name    = ? ," +
					   "		  phone   = ?  " +
					   "	WHERE user_id = ?  ";
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		p.setString(1, storeUser.getName());

		short phone = storeUser.getPhone().shortValue();
		if (phone != 0)
			p.setShort(2, phone);
		else
			p.setNull(2, Types.NULL);
		
		p.setInt(3, storeUser.getId());
		
		result = p.executeUpdate();
		p.close();
		con.close();
		
		return result;
	}
	
	public boolean validate(String email, String password) throws SQLException, CannotPerformOperationException, InvalidHashException {
		boolean result = false;
		
		String query = "SELECT * FROM STOREUSER WHERE email = ?";
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);		
		p.setString(1, email);
		ResultSet r = p.executeQuery();
		r.next();
		String correctHash = r.getString("password");
		result = PasswordStorage.verifyPassword(password, correctHash);
		
		p.close();
		con.close();		
		
		return result;
	}

}