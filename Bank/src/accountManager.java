import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import  java.util.Scanner;
public class accountManager {
    private Connection connection;
    private Scanner scanner;

    public accountManager(Connection connection,Scanner scanner)
    {
        this.connection = connection;
        this.scanner = scanner;
    }
    public void debit_money(long account_number) throws SQLException
    {
        System.out.println("Enter money for debit");
        long dmoney = scanner.nextLong();
        System.out.println("Enter security pin");
        int security = scanner.nextInt();
        try
        {
            connection.setAutoCommit(false);
            if(account_number!=0)
            {
                String query ="select * from accounts where account_number =? and security_pin =?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setLong(1,account_number);
                pstmt.setInt(2,security);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next())
                {
                    double current_balance = rs.getDouble("balance");
                    if(dmoney <=current_balance)
                    {
                        String debit="update accounts set balance = balance - ? where account_number =?";
                        PreparedStatement prepare = connection.prepareStatement(debit);
                        prepare.setDouble(1,dmoney);
                        prepare.setLong(2,account_number);
                        int affect = prepare.executeUpdate();
                        if(affect > 0)
                        {
                            System.out.println("Rs " + dmoney + " debited successfully..");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient Balance");
                    }
                }else {
                    System.out.println("Invalid pin");
                }
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }
    public void creditMoney (long account_number) throws SQLException
    {
        System.out.println("Enter money to Credit");
        Long cMoney =scanner.nextLong();
        System.out.println("Enter security pin");
        int pin = scanner.nextInt();

        if(account_number !=0)
        {
            try {
                connection.setAutoCommit(false);

                String query ="select * from accounts where account_number =? and security_pin=?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setLong(1,account_number);
                pstmt.setInt(2,pin);
                ResultSet rs =pstmt.executeQuery();
                if(rs.next())
                {
                    String cQuery ="update accounts set balance = balance + ? where account_number=?";
                    PreparedStatement ps = connection.prepareStatement(cQuery);
                    ps.setLong(1,cMoney);
                    ps.setLong(2,account_number);
                    int aff = ps.executeUpdate();
                    if(aff >0)
                    {
                        System.out.println("Rs "+ cMoney + " Credited Successfully..!");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }else {
                        System.out.println("Transaction Failed");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }

                }else {
                    System.out.println("Invalid security pin");
                }
            }catch (SQLException e)
            {
                e.printStackTrace();
            }
            connection.setAutoCommit(true);
        }
    }
    public void getBalance(long account_number)
    {
        System.out.println("Enter security pin");
        int pin = scanner.nextInt();
        try{
            String query ="select balance from accounts where account_number=? and security_pin=?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setLong(1,account_number);
            pstmt.setInt(2,pin);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
            {
                double balance = rs.getDouble("balance");
                System.out.println("Available balance :"+balance);
            }else{
                System.out.println("Invalid security pin");
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
    public void transferMoney(long senderAC){
        System.out.println("Enter receiver Account Number:");
        long receiverAc = scanner.nextLong();
        System.out.println("Enter Amount to transfer");
        double amount = scanner.nextDouble();
        System.out.println("Enter security pin");
        int pin = scanner.nextInt();

        try{
            connection.setAutoCommit(false);
            if(senderAC !=0 && receiverAc!=0) {
                String query = "Select * from accounts where account_number =? and security_pin =?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setLong(1,senderAC);
                ps.setInt(2,pin);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    double cuBalance = rs.getDouble("balance");
                    if(amount <= cuBalance)
                    {
                        String debQue ="update accounts set balance =balance -? where account_number =?";
                        String crQue ="update accounts set balance = balance +? where account_number =?";
                        PreparedStatement p = connection.prepareStatement(debQue);
                        PreparedStatement p2 = connection.prepareStatement(crQue);
                        p.setDouble(1,amount);
                        p.setLong(2,senderAC);
                        p2.setDouble(2,amount);
                        p2.setLong(2,receiverAc);
                        int aff1 = p.executeUpdate();
                        int aff2 = p2.executeUpdate();
                        if(aff1!=0 && aff2 !=0){
                            System.out.println("Money transfer successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient balance");
                    }
                }else{
                    System.out.println("Invalid security pin");
                }

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
