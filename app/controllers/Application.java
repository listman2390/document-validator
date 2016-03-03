package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public Result index() {
        return ok(index.render(""));
    }

    public Result signUp(){
    	return ok(signup.render());
    }
    
    public Result validate(){
    	return ok(validate.render());
    }
}
