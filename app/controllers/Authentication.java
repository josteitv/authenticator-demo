package controllers;

import static controllers.routes.Application;
import static controllers.routes.Authentication;
import static views.html.login.render;
import models.Login;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Authentication extends Controller {

    private static Form<Login> form = Form.form(Login.class);

    public static Result form() {
        return ok(render(form));
    }

    public static Result login() {
        Form<Login> filledForm = form.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(render(filledForm));
        } else {
            session("username", filledForm.get().username);
            return redirect(Application.index());
        }
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(Authentication.form());
    }

}
