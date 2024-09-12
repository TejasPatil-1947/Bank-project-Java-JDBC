import java.sql.*;
import java.util.Scanner;
public class Banking {

    private static final String url ="jdbc:mysql://localhost:3306/bank";
    private static final String userName ="root";
    private  static final String password ="Shivsena1947";

    public static void main(String[] args) {
    try
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }catch (ClassNotFoundException e)
    {
        e.printStackTrace();
    }
    try
    {
        Connection connection = DriverManager.getConnection(url,userName,password);
        Scanner scanner = new Scanner(System.in);
        accountManager accountManager = new accountManager(connection,scanner);
        Accounts accounts = new Accounts(connection,scanner);
        user user = new user(connection,scanner);

        String email;
        long account_number;

            while(true){
                System.out.println("Welcome to Patil's Banak");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit0");

                System.out.println("Enter your choice");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        user.register();
                        break;
                    case 2:
                       email= user.login();
                       if(email!=null){
                           System.out.println();
                           System.out.println("user logged in successfully!");
                           if(!accounts.account_exists(email)){
                               System.out.println();
                               System.out.println("1. Open new Account");
                               System.out.println("2. Exit");

                               if(scanner.nextInt() == 1){
                                   account_number = accounts.openAccount(email);
                                   System.out.println("Account created successfully");
                                   System.out.println("Account Number is: " + account_number);
                               }else{
                                   break;
                               }
                           }
                           account_number = accounts.getAccountNumber(email);
                           int choice2 =0;
                           while (choice2 != 5){
                               System.out.println();
                               System.out.println("1. Debit Money");
                               System.out.println("2. Credit Money");
                               System.out.println("3. Transfer Money");
                               System.out.println("4. check Balance");
                               System.out.println("5. Exit");
                               choice2 = scanner.nextInt();

                               switch (choice2){
                                   case 1:
                                       accountManager.debit_money(account_number);
                                       break;
                                   case 2:
                                       accountManager.creditMoney(account_number);
                                       break;
                                   case 3:
                                       accountManager.transferMoney(account_number);
                                       break;
                                   case 4:
                                       accountManager.getBalance(account_number);
                                       break;
                                   case 5:
                                       break;
                                   default:
                                       System.out.println("Enter valid choice!");
                                       break;
                               }


                           }
                       }else {
                           System.out.println("invalid email or password");
                       }
                    case 3:
                        System.out.println("Thank you for using Patil's banking system");
                        System.out.println("Existing system");
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            }

    }catch (Exception e)
    {
        e.printStackTrace();
    }

    }
}
