package chapter4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MysqlDemo {
    public static final String URL = "jdbc:mysql://9.134.216.159:3306/click_test";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "123456";
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            statement = conn.prepareStatement("SELECT * FROM mysql_table");
            resultSet = statement.getResultSet();
            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                System.out.println(id+"   "+name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(resultSet!=null){
                resultSet.close();
            }
            if(statement!=null){
                statement.close();
            }
            if(conn!=null){
                conn.close();
            }
        }
    }
}
