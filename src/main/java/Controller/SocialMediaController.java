package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Service.AccountService;
import Model.Message;
import Service.MessageService;

import static org.mockito.ArgumentMatchers.nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerUserHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::insertMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getOneMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessageFromAcccountIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerUserHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            ctx.json(addedAccount);
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    } 

    private void loginHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account login = accountService.login(account);
        if(login!=null){
            ctx.json(login);
            ctx.status(200);
        }else{
            ctx.status(401);
        }
    }

    private void insertMessageHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage!=null){
            ctx.json(addedMessage);
            ctx.status(200);
        }else{
            ctx.status(400);
        }
    }

    public void getAllMessagesHandler(Context ctx) throws JsonProcessingException
    {

        List<Message> messages = messageService.getAllMessages();

        ctx.json(messages);
        ctx.status(200);
    }

    public void getOneMessageHandler(Context ctx) throws JsonProcessingException
    {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message message = messageService.getMessageFromMessageID(messageId);

        if (message != null) 
        {
            ctx.json(message);
            ctx.status(200);
        } else
        {
            ctx.result("");
            ctx.status(200);
        }
      
    }

    public void deleteMessageHandler(Context ctx) throws JsonProcessingException
    {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        Message deletedMessage = messageService.deleteMessageFromID(messageId);

        if (deletedMessage != null) {
            ctx.json(deletedMessage); 
        } else {
            ctx.status(200); 
            ctx.result(""); 
        }

    }

    public void updateMessageHandler(Context ctx) throws JsonProcessingException
    {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message updateMessage = mapper.readValue(ctx.body(), Message.class);
        String updateText = updateMessage.getMessage_text();
        
        if (updateText.isEmpty()) {
            ctx.status(400); 
            ctx.result(""); 
            return;
        }

        Message updatedMessage = messageService.updateMessage(messageId, updateText);

        if(updatedMessage != null)
        {
            ctx.json(updatedMessage);
            ctx.status(200);
        }
        else
            ctx.status(400);

    }

    public void getMessageFromAcccountIdHandler(Context ctx) throws JsonProcessingException
    {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));

        List<Message> messages = messageService.getMessagesFromAccountId(accountId);

        ctx.json(messages);
        ctx.status(200);
    }




    



}