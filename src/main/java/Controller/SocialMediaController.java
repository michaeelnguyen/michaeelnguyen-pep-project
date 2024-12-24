package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerAccount);
        app.post("/login", this::loginAccount);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageByID);
        app.delete("/messages{message_id}", this::deleteMessageByID);
        app.patch("/messages/{message_id}", this::updateMessageByID);
        app.get("/accounts/{account_id}/messages", this::getMessageByAccountID);

        return app;
    }

    /**
     * Hander for creating a new Account for the user.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void registerAccount(Context context) throws JsonProcessingException {
        context.json("register account");
    }

    /**
     * Hander for login process for users.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void loginAccount(Context context) throws JsonProcessingException {
        context.json("login account");
    }

    /**
     * Handler for creation of new messages
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void createMessage(Context context) throws JsonProcessingException {
        context.json("create message");
    }

    /**
     * Handler to retrieve all messages
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessages(Context context) {
        context.json("retrieve all messages");
    }

    /**
     * Handler to retrieve a message by its ID.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageByID(Context context) {
        context.json("retrieve a message by its ID");
    }

    /**
     * Handler to delete a message by its ID.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void deleteMessageByID(Context context) {
        context.json("delete a message by its ID");
    }

    /**
     * Handler to update a message by its ID.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void updateMessageByID(Context context) throws JsonProcessingException {
        context.json("update a message by its ID");
    }

    /**
     * Handler to retrieve all messages written by a particular user account.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageByAccountID(Context context) {
        context.json("retrieve all messages written by specific account ID");
    }


}