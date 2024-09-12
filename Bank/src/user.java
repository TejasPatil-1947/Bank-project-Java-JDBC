import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class user {
    private Connection connection;
    private Scanner scanner;

    public user(Connection connection,Scanner scanner)
    {
        this.connection = connection;
        this.scanner=scanner;
    }

    public void register()
    {

            System.out.println("Enter full Name");
            String name = scanner.next();
            System.out.println("Enter Email");
            String email = scanner.next();
            System.out.println("Enter password");
            String password = scanner.next();

            if(user_exist(email))
            {
                System.out.println("user already exists");
            }

        try
        {
            String query ="insert into user(full_name,email,password) values (?,?,?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,name);
            pstmt.setString(2,email);
            pstmt.setString(3,password);

            int affected = pstmt.executeUpdate();
            if(affected >0)
            {
                System.out.println("User Register Successfully!");
            }else
            {
                System.out.println("Registration failed..");
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public String login()
    {
        System.out.println("Enter email:");
        String email = scanner.next();
        System.out.println("Enter password:");
        String password = scanner.next();

        String login_query ="select * from user where email =? and password =?";

        try
        {
            PreparedStatement pstmt = connection.prepareStatement(login_query);
            pstmt.setString(1,email);
            pstmt.setString(2,password);

            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next())
            {
                return email;
            }else{
                return null;
            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public boolean user_exist(String email)
    {
        String query ="select * from user where email =?";
        try
        {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,email);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
            {
                return true;
            }else {
                return false;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
