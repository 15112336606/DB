package Serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

class User implements Serializable{
	private static final long serialVersionUID = 7592982384475575933L;
	private String username;
	private String phone;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public String toString() {
		return "user [username=" + username + ", phone=" + phone + "]";
	}
	private void writeObject(ObjectOutputStream out) throws Exception{
		byte[] encode = Base64.getEncoder().encode(username.getBytes());
		this.username=new String(encode);
		out.defaultWriteObject();
	}
	private void readObject(ObjectInputStream in) throws Exception{
		in.defaultReadObject();
		byte[] decode = Base64.getDecoder().decode(username.getBytes());
		this.username = new String(decode);
	}
}



public class TestSerializable01 {
public static void main(String[] args) throws Exception{
	ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("s1.txt"));
	User user=new User();
	user.setUsername("admin");
	user.setPhone("12346546");
	out.writeObject(user);
	out.close();
	System.out.println(user);
	ObjectInputStream in = new ObjectInputStream(new FileInputStream("s1.txt"));
	User readObject = (User)in.readObject();
	System.out.println(readObject);
	in.close();
}
}
