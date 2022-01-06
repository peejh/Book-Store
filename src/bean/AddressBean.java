package bean;

public class AddressBean {

	private int id;
	private int aptNum;
	private int streetNum;
	private String streetName;
	private String city;
	private String province;
	private String country;
	private String postalCode;
	private int userId;				// who owns the Address
	
	public AddressBean(int aptNum, int streetNum, String streetName, String city, String province, String country, String postalCode,
			int userId) {
		super();
		this.aptNum = aptNum;
		this.streetNum = streetNum;
		this.streetName = streetName;
		this.city = city;
		this.province = province;
		this.country = country;
		this.postalCode = postalCode;
		this.userId = userId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAptNum() {
		return aptNum;
	}
	public void setAptNum(int aptNum) {
		this.aptNum = aptNum;
	}
	public int getStreetNum() {
		return streetNum;
	}
	public void setStreetNum(int streetNum) {
		this.streetNum = streetNum;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	@Override
	public String toString() {
		String result = "";
		
		if (aptNum != 0)
			result += aptNum + "-";
		
		result += streetNum + " ";
		result += streetName + ", ";
		result += city + ", ";
		result += province + ", ";
		result += country;
		
		if (postalCode != null && !postalCode.isEmpty())
			result += "  " + postalCode;
		
		return result;
	}
	
}
