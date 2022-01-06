package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import bean.AddressBean;

public class AddressDAO {

	DataSource ds;
	
	public AddressDAO() throws ClassNotFoundException{
		try {
			ds = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/BookStore");
		} catch (NamingException e){
			e.printStackTrace();
		}
	}
	
	public HashMap<Integer, AddressBean> retrieveAll() throws SQLException{
		String query = "SELECT * FROM ADDRESS";
		HashMap<Integer, AddressBean> rv = new HashMap<Integer, AddressBean>();
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		ResultSet r = p.executeQuery();
		while (r.next()) {
			int id = r.getInt("addr_id");
			int aptNum = r.getShort("apt_num");
			int streetNum = r.getShort("street_num");
			String streetName = r.getString("street_name");
			String city = r.getString("city");
			String province = r.getString("province");
			String country = r.getString("country");
			String postalCode = r.getString("postal_code");
			Integer userId = r.getInt("user_id");
			AddressBean addr = new AddressBean(aptNum, streetNum, streetName, city, province, country, postalCode, userId);
			addr.setId(id);
			rv.put(id, addr);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
	
	public int addAddress(AddressBean address) throws SQLException {
		int result;
		
		String query = "INSERT INTO ADDRESS (apt_num, street_num, street_name, city, province, country, postal_code, user_id)" + 
					   "	VALUES(?,?,?,?,?,?,?,?)";
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setShort(1, (short) address.getAptNum());
		p.setShort(2, (short) address.getStreetNum());
		p.setString(3, address.getStreetName());
		p.setString(4, address.getCity());
		p.setString(5, address.getProvince());
		p.setString(6, address.getCountry());
		p.setString(7, address.getPostalCode());
		p.setInt(8, address.getUserId());
		
		result = p.executeUpdate();
		p.close();
		con.close();
		
		return result;
	}
		
	public int deleteAddress(int id) throws SQLException {
		int result;
		String query = "DELETE FROM ADDRESS WHERE addr_id = ?";
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setInt(1, id);
		result = p.executeUpdate();
		p.close();
		con.close();
		return result;				
	}
}