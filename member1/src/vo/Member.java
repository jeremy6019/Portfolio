package vo;

import java.sql.Date;

public class Member {

	private String email ;
	private String password;
    private String name; 
    private String nickname;
    private String image; 
	private Date birthday; 
	private String phone;
	
	public Member() {
		super();
		
	}
	
	public Member(String email, String password, String name, String nickname, String image, Date birthday,
			String phone) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.image = image;
		this.birthday = birthday;
		this.phone = phone;
	}

	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	} 
	
	@Override
	public String toString() {
		return "Member [email=" + email + ", password=" + password + ", name=" + name + ", nickname=" + nickname
				+ ", image=" + image + ", birthday=" + birthday + ", phone=" + phone + "]";
	}
	
}
