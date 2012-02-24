package controllers.admin;

import play.mvc.With;
import controllers.CRUD;
import controllers.filters.LogFilter;

@With(LogFilter.class)
public class Games extends CRUD {

}
