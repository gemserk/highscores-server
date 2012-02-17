package models;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

@Entity
@Table(name="users")
public class User extends GenericModel {
	
	@Id
	@SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	public Long id;
	
	public Long publickey; 
	
	public String name;

	public String privatekey;
	
	public static long getNextGuestNumber(){
		BigInteger guestNumber = (BigInteger) JPA.em().createNativeQuery("select nextval('" + UserSequenceHelper.SEQUENCENAME + "')").getSingleResult();
		return guestNumber.longValue();
	}
}
