package controllers;

import static controllers.routes.Authentication;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class MyAuthenticator extends Security.Authenticator {

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(Authentication.login());
    }

}