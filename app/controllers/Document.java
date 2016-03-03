package controllers;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import play.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.*;

import views.html.*;

public class Document extends Controller {
	@Transactional
	public Result validate(String number,String option){
		try{
			Query query = JPA.em().createNamedQuery("Document.findByDocumentNumber",model.Document.class);
			query.setParameter("documentNumber", number);
			model.Document document = (model.Document)query.getSingleResult();
			if("validate".equals(option)){
				return ok(validateresult.render(document));
			}else{
				return ok(ver.render(document));	
			}
		}catch(NoResultException e){
			return ok(validateresultfail.render());
		}
	}
}
