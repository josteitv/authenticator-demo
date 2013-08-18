package controllers;

import static controllers.routes.Authentication;
import static views.html.newuser.render;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class RegisterNewUser extends Controller {

    private static Form<User> form = Form.form(User.class);

    public static Result form() {
        return ok(render(form));
    }

    public static Result newUser() {
        Form<User> filledForm = form.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(render(filledForm));
        } else {
            User.create(filledForm.get());
            return redirect(Authentication.login());
        }

    }

}
