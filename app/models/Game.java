package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name="games")
public class Game extends GenericModel {

	@Id
	@SequenceGenerator(name = "game_seq", sequenceName = "game_seq", allocationSize = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_seq")
	public Long id;
	
	public String name;
	public String apikey;

}
