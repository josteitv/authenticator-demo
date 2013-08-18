package controllers;

import static views.html.index.render;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(MyAuthenticator.class)
public class Application extends Controller {

    public static Result index() {
        return ok(render());
    }

}
