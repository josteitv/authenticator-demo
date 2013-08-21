package controllers;

import static controllers.routes.Authentication;
import static views.html.newuser.render;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.TwoStepVerification;

public class RegisterNewUser extends Controller {

    private static Form<User> form = Form.form(User.class);

    public static Result form() {
        String secret = TwoStepVerification.generateSecret();
        String url = TwoStepVerification.createQrCodeUrl("JavaZone 2013 authenticator demo", secret);

        return ok(render(form, secret, url));
    }

    public static Result newUser() {
        Form<User> filledForm = form.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(render(filledForm, null, null));
        } else {
            User.create(filledForm.get());
            return redirect(Authentication.login());
        }

    }

}
