package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

/**
 * createMessage method handles the business logic and interaction with the DAO associated
 * with creating and inserting a new message into the database.
 * @param msg The Message class objet, which contains the data of the new message.
 * @param account The Account class object which contains the specific account associated with the message.
 * @return The Message object that contains the data associated with the message after insertion into the database.
 */
public Message createMessage(Message msg, Account account) {
    try {
        validateNewMessage(msg, account);

        Message newMsg = new Message(msg.getPosted_by(), msg.getMessage_text(), msg.getTime_posted_epoch());
        
        return messageDAO.insert(newMsg);
        
    } catch (RuntimeException e) {
        throw new RuntimeException("Exception during createMessage");
    }
}

/**
 * validateNewMessage method is a helper function to perform validation on the message text requirements.
 * Message text cannot be blank, over 255 characters, and should be posted by an existing user account.
 * @param msg The Message class object, which contains the data of the new message.
 * @param account The Account class object which contains the specific account associated with the message.
 */
public void validateNewMessage(Message msg, Account account){
    try{
        // Validate to check if new message to be added satisfies text requirements
        if(msg.message_text.isBlank()){
            throw new RuntimeException("Message cannot be blank");
        }
        if(msg.message_text.length() > 254){
            throw new RuntimeException("Message cannot exceed 255 characters");
        }
        if(account == null){
            throw new RuntimeException("Cannot create message without existing account");
        }

    } catch (RuntimeException e) {
        throw new RuntimeException(e.getMessage());
    }
}

/**
 * getAllMessages method handles the business logic and interaction with the DAO associated
 * with retrieving all messages in the database.
 * @return A List of Message objects containing all the messages retrieved from the database.
 */
public List<Message> getAllMessages() {
    try {
        return messageDAO.getAllMessages();      
    } catch (RuntimeException e) {
        throw new RuntimeException("Exception during getAllMessages");
    }
}

/**
 * getMessageByID method handles the business logic and interaction with the DAO associated
 * with retrieving a specific id associated with a message in the database.
 * @param id The message id of the associated message.
 * @return The Message object that contains the data corresponding to a specific message id in the database.
 */
public Message getMessageByID(int id) {
    try {
        return messageDAO.getMessageByID(id);      
    } catch (RuntimeException e) {
        throw new RuntimeException("Exception during getMessageByID");
    }
}

/**
 * deleteMessageByID method handles the business logic and interaction with the DAO associated
 * with deleting an existing message from the database.
 * @param msg The Message class object , which contains the message_id of the message to be deleted.
 * @return True/False whether or not the message was deleted sucessfully from the database.
 */
public boolean deleteMessageByID(Message msg) {
    try {
        return messageDAO.delete(msg);

    } catch (RuntimeException e) {
        throw new RuntimeException("Exception during updateMessageByID");
    }
}

/**
 * updateMessageByID method handles the business logic and interaction with the DAO associated
 * with updating an existing message from the database.
 * @param id The message_id associated with a specific message targeted for updating.
 * @param msg The instance of the Message class, which contains the updated data of the message.
 * @return True/False whether or not the message was updated sucessfully from an existing record in the database.
 */
public boolean updateMessageByID(int id, Message msg) {
    try {
        // Validation to check if message text is blank and over 255 characters
        if(msg.message_text.isBlank()){
            throw new RuntimeException("Message cannot be blank");
        }
        if(msg.message_text.length() > 254){
            throw new RuntimeException("Message cannot exceed 255 characters");
        }

        return messageDAO.update(id, msg);
        
    } catch (RuntimeException e) {
        throw new RuntimeException("Exception during updateMessageByID");
    }
}

/**
 * getAllMessagesByAccountID method handles the business logic and interaction with the DAO associated
 * with retrieving all messages in the database from a specific account_id.
 * @param id The account_id that is associated with all the messages that are targeted for retrieval.
 * @return A List of Message objects corresponding to a specific user account id in the database.
 */
public List<Message> getAllMessagesByAccountID(int id) {
    try {
        return messageDAO.getAllMessagesByAccountID(id);      
    } catch (RuntimeException e) {
        throw new RuntimeException("Exception during getAllMessagesByAccountID");
    }
}

}
