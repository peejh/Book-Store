package bean;

public class StoreUserBean {

	private int id;				// randomly generated
	private String email;		// effectively the username for logging in
	private String name;		// real name
	private String password;	// salt + hash
	private Integer phone;		// Integer object to support NULL
	private boolean isOwner;	// owner has dashboard for reports
	
	public StoreUserBean(int id, String email, String name, String password, Integer phone, boolean isOwner) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
		this.phone = phone;
		this.isOwner = isOwner;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getPhone() {
		return phone;
	}
	public void setPhone(Integer phone) {
		this.phone = phone;
	}
	public boolean isOwner() {
		return isOwner;
	}
	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}
	
	public String getFirstName() {	
		String[] parts = this.getName().split("\\s+");	
		return parts[0];
	}
	
}
