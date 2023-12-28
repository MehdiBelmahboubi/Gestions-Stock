package Table_data;
import java.sql.*;

public class connection {
    private static final String url = "jdbc:mysql://localhost:3306/gestions_stock_java";
    private static final String username="root";
    private static final String passwd = "";

    public static Connection getConnection()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url,username,passwd);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean authentificateUser(String username,String passwrd)
    {
        Connection connection = getConnection();
        if(connection!=null)
        {
            try {
                String query ="Select * from admin where identifiant=? and passwrd=?";
                try(PreparedStatement preparedStatement = connection.prepareStatement(query))
                {
                    preparedStatement.setString(1,username);
                    preparedStatement.setString(2,passwrd);

                    try(ResultSet resultSet=preparedStatement.executeQuery())
                    {
                        return  resultSet.next();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
