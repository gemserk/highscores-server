package datatransfer;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import models.Score;

public class ScoreDTO {

	public String username;
	public String name;
	public long score;
	public String data;
	public String date;
	
	public ScoreDTO(Score score){
		this.username = score.user.username;
		this.name = score.user.name;
		this.score = score.score;
		this.data = score.data;
		this.date = new DateTime().dayOfYear().setCopy(score.day).toString();
	}
	
	static public List<ScoreDTO> convert(List<Score> scores){
		List<ScoreDTO> scoreDTOs = new ArrayList<ScoreDTO>();
		for (Score score : scores) {
			scoreDTOs.add(new ScoreDTO(score));
		}
		return scoreDTOs;
	}
}
