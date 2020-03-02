import java.sql.*;

public class User {
    public int id;
    public String username;
    public int age;

    public void setId(int id1){
        this.id = id1;
    }

    public void setUsername(String name){
        this.username = name;
    }

    public void setAge(int age1){
        this.age = age1;
    }



    public void getuser(String account){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql = "select ID,age,username from Users where account='"+account+"'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                int id = rs.getInt("ID");
                int age = rs.getInt("age");
                String username = rs.getString("username");
                System.out.println("登录的用户id: " + id);
                setId(id);
                System.out.println("登录的用户名: " + username);
                setUsername(username);
                System.out.println("用户年龄: " + age);
                setAge(age);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException var22) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException var21) {
                var21.printStackTrace();
            }

        }
        System.out.println("数据库操作完成");
    }

    public static void main(String[] args){
        User user = new User();
        user.getuser("admin");
    }
}
