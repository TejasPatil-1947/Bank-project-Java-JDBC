import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection,Scanner scanner)
    {
        this.connection = connection;
        this.scanner = scanner;
    }
    public boolean account_exists(String email)
    {
        String query ="select account_number from accounts where email =?";
        try
        {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1,email);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
            {
                return true;
            }else{
                return false;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public long generate_accountNumber()
    {
        String query ="select account_number from accounts order by account_number  desc limit 1";
        try
        {
            Statement stmt = connection.createStatement();
            ResultSet resultSet =stmt.executeQuery(query);
            if (resultSet.next())
            {
                long existing_accNO = resultSet.getLong("account_number");
                return existing_accNO +1;
            }else{
                return 10000100;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 10000100;
    }
    public long getAccountNumber(String email)
    {
        try{
         String query ="select account_number from accounts where email =?";
         PreparedStatement pstmt = connection.prepareStatement(query);
         pstmt.setString(1,email);
         ResultSet rs =pstmt.executeQuery();
         if(rs.next())
         {
             return rs.getLong("account_number");
         }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number doesn't exists...");
    }
    public long openAccount(String email)
    {
        if(!account_exists(email))
        {
            String query="Insert into accounts(account_number,full_name,email,balance,security_pin) values(?,?,?,?,?)";
            System.out.println("Enter your Name:");
            String name = scanner.next();
            System.out.println("Enter initial Balance:");
            double initialBalance = scanner.nextDouble();
            System.out.println("Enter security Pin:");
            String pin = scanner.next();
            try
            {
                long getAccountNo = generate_accountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1,getAccountNo);
                preparedStatement.setString(2,name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,initialBalance);
                preparedStatement.setString(5,pin);

                int affectedRows = preparedStatement.executeUpdate();
                if(affectedRows > 0) {

                    return getAccountNo;
                }else
                {
                    throw new RuntimeException("Account creation failed");
                }
            }catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account already exists");
    }
}
