package DAO;
import static org.mockito.ArgumentMatchers.eq;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Model.Message;
import Util.ConnectionUtil;


public class MessageDAO {
    public Message insertMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedMessageId = generatedKeys.getInt(1);
                return new Message(generatedMessageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            } else {
                throw new SQLException();
            }
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;


    }

    public List<Message> getAllMessage()
    {
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "Select * From message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));

                messages.add(message);
            }

            
            
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return messages;
    }


    public Message getMessageById(int message_id)
    {
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "Select * from message Where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next())
            {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                return message;
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message deleteMessage(int messageId)
    {
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMessage = null;
        try{
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, messageId);
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                deletedMessage = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
            }
            
            
            String deleteSql = "Delete from message Where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSql);

            preparedStatement.setInt(1, messageId);
            preparedStatement.executeUpdate();


        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return deletedMessage;
    }

    public Message updateMessage(int messageId, String messageText)
    {
        Connection connection = ConnectionUtil.getConnection();
        Message updatedMessage = null;

        try{

            String sql = "Update message Set message_text = ? Where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, messageText);
            preparedStatement.setInt(2, messageId);
            preparedStatement.executeUpdate();

            String selectSql = "Select * From message Where message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, messageId);
            ResultSet rs = selectStatement.executeQuery();

            if(rs.next())
            {
                updatedMessage = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"), rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
            }

            return updatedMessage;

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getMessageByAccountId(int accountId)
    {
       Connection connection = ConnectionUtil.getConnection();
       List<Message> messages = new ArrayList<>();

       try
       {
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, accountId);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
            );
            messages.add(message);
        }



       }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }



    
}
