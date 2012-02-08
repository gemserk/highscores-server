package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class Score extends Model{
	
	@ManyToOne(optional=false) public Leaderboard leaderboard;
	@ManyToOne(optional=false) public User user;
	public long score;
	public String data;
	public int year;
	public int month;
	public int week;
	public int day;
	public int scope;
	
	
}
