package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Game extends Model {

	public String name;
	public String apikey;

}
