package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private final AccountService accountService;
    private final MessageService messageService;
    private final ObjectMapper mapper;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        this.mapper = new ObjectMapper();
    }

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
        app.delete("/messages/{message_id}", this::deleteMessageByID);
        app.patch("/messages/{message_id}", this::updateMessageByID);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountID);

        return app;
    }

    /**
     * Hander for creating a new Account for the user.
     * Response status code: 200 (by default) if successful, 400 (client error) otherwise.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void registerAccount(Context context) throws JsonProcessingException {
        Account account = mapper.readValue(context.body(), Account.class);
        try {
            Account newAccount = accountService.createAccount(account);
            context.json(mapper.writeValueAsString(newAccount));
        } catch (RuntimeException e) {
            context.status(400);
        }
        
    }

    /**
     * Hander for login process for users.
     * Response status code: 200 (by default) if successful, 401 (unauthorized) otherwise.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void loginAccount(Context context) throws JsonProcessingException {
        Account account = mapper.readValue(context.body(), Account.class);
        try {
            Account loginAccount = accountService.login(account);
            // Check if account exists
            if(loginAccount != null){
                context.json(mapper.writeValueAsString(loginAccount));
            } else {
                context.status(401);
            }
        } catch (RuntimeException e) {
            context.status(401);
        }
    }

    /**
     * Handler for creation of new messages
     * Response status code: 200 (by default) if successful, 400 (client error) otherwise.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void createMessage(Context context) throws JsonProcessingException {
        Message msg = mapper.readValue(context.body(), Message.class);
        try {
            Account account = accountService.getAccountByID(msg.getPosted_by());
            Message newMsg = messageService.createMessage(msg, account);

            context.json((mapper.writeValueAsString(newMsg)));
        } catch (RuntimeException e) {
            context.status(400);
        }
    }

    /**
     * Handler to retrieve all messages
     * Response status code: 200 (by default) 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessages(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    /**
     * Handler to retrieve a message by its ID.
     * Response status code: 200 (by default) 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageByID(Context context) {
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message msg = messageService.getMessageByID(id);
        // Check if message exists
        if(msg != null){
            context.json(msg);
        // Set response body to empty if message does not exists
        } else {
            context.result("");
        }
    }

    /**
     * Handler to delete a message by its ID.
     * Response status code: 200 (by default) if successful, 400 (client error) otherwise.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     */
    private void deleteMessageByID(Context context) throws JsonProcessingException {

        try {
            int id = Integer.parseInt(context.pathParam("message_id"));

            Message msgFromDb = messageService.getMessageByID(id);
            // Check if message exists in database
            if(msgFromDb == null){
                context.status(200);
            } else {
                // Check if deleted message was successful
                boolean isDeleted = messageService.deleteMessageByID(msgFromDb);
                if(isDeleted){
                    context.json(msgFromDb);
                } else{
                    context.status(400);
                }
            }

        } catch (RuntimeException e){
            context.status(400);
        }
    }


    /**
     * Handler to update a message by its ID.
     * Response status code: 200 (by default) if successful, 400 (client error) otherwise.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void updateMessageByID(Context context) throws JsonProcessingException {
        Message msg = mapper.readValue(context.body(), Message.class);
        try {
            int id = Integer.parseInt(context.pathParam("message_id"));

            Message msgFromDb = messageService.getMessageByID(id);
            // Set message text to the updated text 
            msgFromDb.setMessage_text(msg.getMessage_text());
            boolean isUpdated = messageService.updateMessageByID(id, msgFromDb);
            // Check if message was updated successfully
            if(isUpdated){
                context.json(msgFromDb);
            } else{
                context.status(400);
            }

        } catch (RuntimeException e){
            context.status(400);
        }
    }

    /**
     * Handler to retrieve all messages written by a particular user account.
     * Response status code: 200 (by default)
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesByAccountID(Context context) {
        int id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByAccountID(id);
        // Check if message list exists. If so, then send JSON body of message list as well as 200 OK
        if(!messages.isEmpty()){
            context.json(messages).status(200);
        } else{
            context.status(200);
        }
        context.json(messages);
    }

}