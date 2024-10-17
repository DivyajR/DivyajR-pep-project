package Service;
import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;

import static org.mockito.ArgumentMatchers.nullable;

import java.util.ArrayList;
import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
        accountDAO = new AccountDAO();
    }

    public Message addMessage(Message message){
        
        if(!message.getMessage_text().isEmpty() && message.getMessage_text().length() <= 255
        &&  accountDAO.accountExists(message.getPosted_by()))
        {
            Message addedMessage = messageDAO.insertMessage(message);
            return addedMessage;
        }
        
        return null;
    }

    public List<Message> getAllMessages()
    {
        List messages = messageDAO.getAllMessage();
        return messages;
    }

    public Message getMessageFromMessageID(int messageId)
    {  
        return messageDAO.getMessageById(messageId);

    }

    public Message deleteMessageFromID(int messageId)
    {
        return messageDAO.deleteMessage(messageId);
    }

    public Message updateMessage(int messageId, String messageText)
    {
        Message updatedMessage = messageDAO.updateMessage(messageId, messageText);

        if(updatedMessage != null && messageText.length() <= 255)
            return updatedMessage;
        else
            return null;

    }

    public List<Message> getMessagesFromAccountId(int accountId)
    {
        List messages = messageDAO.getMessageByAccountId(accountId);

        return messages;

    }
    
}
