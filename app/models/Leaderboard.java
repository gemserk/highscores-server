package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"game_id","name"}))
public class Leaderboard extends Model{
	@Required  @ManyToOne public Game game;
	public String name;
}
