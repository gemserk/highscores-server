package controllers.test;

import play.mvc.Controller;
import play.test.Fixtures;

public class Test extends Controller {

	static public void init(){
		Fixtures.loadModels("test/testdata.yaml");
		renderText("Load Successful");
	}
	
	static public void delete(){
		Fixtures.deleteDatabase();
		renderText("DeleteLoad Successful");
	}
	
}
