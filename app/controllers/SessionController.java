package controllers;

import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.time.Duration;

//--------------------------------------------------------------------------------
/**
 * Controller for managing user session-related actions such as login, logout,
 * and rendering the home page. It interacts with session and cookie management.
 */
//--------------------------------------------------------------------------------
public class SessionController extends Controller {

    private final FormFactory formFactory;

    @Inject
    public SessionController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    //--------------------------------------------------------------------------------
    /**
     * Handles user login by storing the email in the session.
     * @param request the HTTP request.
     * @return a redirect to the home page with the email added to the session.
     */
    //--------------------------------------------------------------------------------
    public Result login(Http.Request request) {
        DynamicForm requestData = formFactory.form().bindFromRequest(request);
        String username = requestData.get("username");
        String email = requestData.get("email");

        if (username == null || username.trim().isEmpty()) {
            return badRequest(views.html.login.render("Username is required."));
        }

        if (email == null || email.trim().isEmpty()) {
            return badRequest(views.html.login.render("Email is required."));
        }

        return redirect("/home")
            .addingToSession(request, "username", username)
            .addingToSession(request, "email", email)
            .withCookies(
                Http.Cookie.builder("username", username)
                    .withMaxAge(Duration.ofDays(7 * 24 * 60 * 60))
                    .withHttpOnly(false)
                    .withPath("/")
                    .build(),
                Http.Cookie.builder("email", email)
                    .withMaxAge(Duration.ofDays(7 * 24 * 60 * 60))
                    .withHttpOnly(false)
                    .withPath("/")
                    .build()
            );
    }

    //--------------------------------------------------------------------------------
    /**
     * Renders the home page with the email from the session.
     * @param request the HTTP request.
     * @return the rendered home page.
     */
    //--------------------------------------------------------------------------------
    public Result home(Http.Request request) {
        Http.Cookie usernameCookie = request.cookies().get("username").orElse(null);
        Http.Cookie emailCookie = request.cookies().get("email").orElse(null);

        if (usernameCookie == null || emailCookie == null) {
            return ok(views.html.login.render(null));
        }

        return ok(views.html.home.render());
    }

    //--------------------------------------------------------------------------------
    /**
     * Logs the user out by clearing the session and discarding cookies for username and email.
     * Redirects the user to the login page.
     * @return a redirect result to the login page.
     */
    //--------------------------------------------------------------------------------
    public Result logout() {
        return redirect(routes.HomeController.index())
            .withNewSession()
            .discardingCookie("username")
            .discardingCookie("email");
    }
}
