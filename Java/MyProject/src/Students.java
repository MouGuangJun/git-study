import java.util.Date;

public class Students {
	@Value("11")
	private int ide;
	@Value("hlfan")
	private String name;
	@Value("云南省昆明市西山区桂林溪街道")
	private String address;
	@Value("2021/12/14 09:58:10")
	private Date date;
	
	public Students() {
		
	}
	
	public Students(int ide, String name, String address, Date date) {
		super();
		this.ide = ide;
		this.name = name;
		this.address = address;
		this.date = date;
	}

	public int getIde() {
		return ide;
	}
	
	public void setIde(int ide) {
		this.ide = ide;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Students [ide=" + ide + ", name=" + name + ", address=" + address + ", date=" + date + "]";
	}
}
