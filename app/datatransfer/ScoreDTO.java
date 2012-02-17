package datatransfer;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import models.Score;

public class ScoreDTO {

	public long publickey;
	public String name;
	public long score;
	
	public ScoreDTO(Score score){
		this.publickey = score.user.publickey;
		this.name = score.user.name;
		this.score = score.score;
	}
	
	static public List<ScoreDTO> convert(List<Score> scores){
		List<ScoreDTO> scoreDTOs = new ArrayList<ScoreDTO>();
		for (Score score : scores) {
			scoreDTOs.add(new ScoreDTO(score));
		}
		return scoreDTOs;
	}
}
