package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name="userSequenceHelper")
public class UserSequenceHelper extends GenericModel {

	public static final String SEQUENCENAME = "guestplayer_seq" ;
	
	@Id
	@SequenceGenerator(name = "useless_seq", sequenceName = SEQUENCENAME, allocationSize = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "useless_seq")
	public Long id;
}
