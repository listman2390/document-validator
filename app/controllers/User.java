package controllers;

import play.mvc.Controller;

import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import play.*;
import play.data.DynamicForm;
import play.data.DynamicForm.Dynamic;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;

import views.html.*;

public class User extends Controller {

	@Transactional
	public Result signUp(){
		String response=null;
		try{
	        model.User user = Form.form(model.User.class).bindFromRequest().get();
	        user.setType("user");
	        JPA.em().persist(user);
	        response = "Usuario Registrado Correctamente";
		}catch(Exception e){
			response = "Error al Registrar el Usuario";
		}
		return ok(signupresponse.render(response));
	}
	
	@Transactional
	public Result login(){
		model.User userObj = Form.form(model.User.class).bindFromRequest().get();
		System.out.println(userObj.getEmail());
		System.out.println(userObj.getPassword());
		Query query=JPA.em().createQuery("select u from User u where email=:email and password=:password", model.User.class);
		query.setParameter("email", userObj.getEmail());
		query.setParameter("password", userObj.getPassword());
		
		try{
			userObj = (model.User) query.getSingleResult();
			session().put("email", userObj.getEmail());
			if("user".equals(userObj.getType())){
				return ok(user.render(userObj));
			}else if("admin".equals(userObj.getType())){
				return ok(admin.render());
			}else{
				session().clear();
			}
		}catch(NoResultException e){
			return ok(index.render("Error, datos invalidos"));
		}
		
		return ok(index.render("Error, permisos invalidos"));
	}
	
	@Transactional
	public Result logout(){
		session().clear();
		return ok(index.render("Logout ok"));
	}
	
	@Transactional
	public Result main(){

		Query query=JPA.em().createQuery("select u from User u where email=:email ", model.User.class);
		query.setParameter("email", session().get("email"));
		
		try{
			model.User userObj = (model.User) query.getSingleResult();
			session().put("email", userObj.getEmail());
			if("user".equals(userObj.getType())){
				return ok(user.render(userObj));
			}else if("admin".equals(userObj.getType())){
				return ok(admin.render());
			}
			
		}catch(NoResultException e){
			return ok(index.render("Error, datos invalidos"));
		}
		
		return ok(index.render("Error, permisos invalidos"));
	}
}
