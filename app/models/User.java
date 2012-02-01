package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
public class User extends Model {
	@Column(unique = true)
	public String username;

	public String passwordHash;

	public boolean guest;

	public String privatekey;
}
