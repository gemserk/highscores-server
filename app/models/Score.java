package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name="scores")
public class Score extends GenericModel{
	
	@Id
	@SequenceGenerator(name = "score_seq", sequenceName = "score_seq", allocationSize = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "score_seq")
	public Long id;
	
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
