package models;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
public class User extends Model {
	
	@Column(unique = true)
	public String username;
	
	public String name;

	public String passwordHash;

	public boolean guest;

	public String privatekey;
	
	public static long getNextGuestNumber(){
		BigInteger guestNumber = (BigInteger) JPA.em().createNativeQuery("select nextval('" + UserSequenceHelper.SEQUENCENAME + "')").getSingleResult();
		return guestNumber.longValue();
	}
}
