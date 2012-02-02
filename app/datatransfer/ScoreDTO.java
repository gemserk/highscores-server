package datatransfer;

import java.util.ArrayList;
import java.util.List;

import models.Score;

public class ScoreDTO {

	public String username;
	public String name;
	public long score;
	public String data;
	
	public ScoreDTO(Score score){
		this.username = score.user.username;
		this.name = score.user.name;
		this.score = score.score;
		this.data = score.data;
	}
	
	static public List<ScoreDTO> convert(List<Score> scores){
		List<ScoreDTO> scoreDTOs = new ArrayList<ScoreDTO>();
		for (Score score : scores) {
			scoreDTOs.add(new ScoreDTO(score));
		}
		return scoreDTOs;
	}
}
