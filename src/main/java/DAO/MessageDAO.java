package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    /**
     * insert method utilizes the DAO associated with creating and inserting a new message into the database
     * @param msg The Message class object which contains data associated with the new message
     * @return The Message object that contains the data associated with the message after insertion into the database
     */
    public Message insert(Message msg){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, msg.getPosted_by());
            ps.setString(2, msg.getMessage_text());
            ps.setLong(3, msg.getTime_posted_epoch());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    int id = rs.getInt(1);
                    return new Message(id, msg.getPosted_by(), msg.getMessage_text(), msg.getTime_posted_epoch());
                } else {
                    throw new SQLException("No account id found. Failed to insert message");
                }
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * getAllMessages method utilizes the DAO associated with retrieving all messages in the database
     * @return The List of Message objects that contains the data associated with all messages in the database after retrieval
     */
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Message msg = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));

                    messages.add(msg);
                }
            } catch (SQLException e){
                throw new SQLException("Failed to retrieve messages from result set");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * getMessageBy method utilizes the DAO associated with retrieving a message using its corresponding message_id
     * @return The Message object that contains the data associated with a message in the database after using message_id for retrieval
     */
    public Message getMessageByID(int id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                }
            } catch (SQLException e){
                throw new SQLException("Failed to retrieve messages from result set");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * delete method utilizes the DAO associated with deleting an existing message from the database
     * @param msg The Message class object which contains data associated with the message selected for deletion
     * @return True/False whether or not the message was successfully removed from the database
     */
    public boolean delete(Message msg) {
        Connection connection = ConnectionUtil.getConnection();
        int affectedRows = 0;
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, msg.getMessage_id());
            affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return affectedRows > 0;
    }

    /**
     * update method utilizes the DAO associated with updating an existing message from the database
     * @param id The message_id used to retrieve the specific message selected to update the values for
     * @param msg The Message class object which contains data associated with the message selected to update
     * @return True/False whether or not the message was successfully updated in the database
     */
    public boolean update(int id, Message msg){
        Connection connection = ConnectionUtil.getConnection();
        int affectedRows = 0;
        try {
            String sql = "UPDATE message SET posted_by = ?, message_text = ?, time_posted_epoch = ? WHERE message_id = ?";
            
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, msg.getPosted_by());
            ps.setString(2, msg.getMessage_text());
            ps.setLong(3, msg.getTime_posted_epoch());
            ps.setInt(4, id);
            affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return affectedRows > 0;
    }

    /**
     * getAllMessageByAccountID method utilizes the DAO associated with retrieving all existing messages of a specific account_id from the database
     * @param id The message_id used to retrieve the specific message selected to update the values for
     * @return The List of Message objects that contains the data associated with messages of a specific account_id after database retrieval
     */
    public List<Message> getAllMessagesByAccountID(int id) {
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Message msg = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));

                    messages.add(msg);
                }
            } catch (SQLException e){
                throw new SQLException("Exception: Failed to retrieve messages from result set");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return messages;
    }
}
