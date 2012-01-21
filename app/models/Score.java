package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import play.db.jpa.Model;

@Entity
public class Score extends Model{
	
	public Leaderboard leaderboard;
	@ManyToOne public User user;
	public long score;
	public String data;
	
	
}
