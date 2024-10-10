package controllers;

import play.mvc.*;

//--------------------------------------------------------------------------------
/**
 * This controller handles the HTTP requests related to rendering the
 * application's home page and provides an entry point for user login.
 */
//--------------------------------------------------------------------------------
public class HomeController extends Controller {

    //--------------------------------------------------------------------------------
    /**
     * Renders the login page when the application receives a GET request
     * at the root path ("/"). The rendered login page allows users to
     * authenticate into the system.
     *
     * @return a Result containing the rendered login page.
     */
    //--------------------------------------------------------------------------------
    public Result index() {
        return ok(views.html.login.render(null));
    }

}
