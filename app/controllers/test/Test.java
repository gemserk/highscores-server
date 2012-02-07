package controllers.test;

import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import play.test.Fixtures;

public class Test extends Controller {

	@Before
	static void onlyRunInTest(){
		if(!Play.runingInTestMode())
			error(403,"Only available in test mode");
	}
	
	
	static public void init(){
		Fixtures.loadModels("test/testdata.yaml");
		renderText("Load Successful");
	}
	
	static public void delete(){
		Fixtures.deleteDatabase();
		renderText("DeleteLoad Successful");
	}
	
	static public void reset(){
		Fixtures.deleteDatabase();
		Fixtures.loadModels("test/testdata.yaml");
		renderText("ResetData Successful");
	}
	
}
