package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name="leaderboards",uniqueConstraints=@UniqueConstraint(columnNames={"game_id","name"}))
public class Leaderboard extends GenericModel{
	
	@Id
	@SequenceGenerator(name = "leaderboard_seq", sequenceName = "leaderboard_seq", allocationSize = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leaderboard_seq")
	public Long id;
	
	@Required  @ManyToOne public Game game;
	public String name;
}
