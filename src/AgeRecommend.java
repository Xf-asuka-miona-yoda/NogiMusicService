import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgeRecommend {
    private int userid;

    private int age;

    private List<Music> musicList = new ArrayList<>();

    public AgeRecommend(int id){
        this.userid = id;
    }

    public int getAge() {
        return age;
    }

    public void getage(){
        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql = "select * from users where ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setObject(1,this.userid);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int birthyear = resultSet.getInt("age");
                Calendar cal = Calendar.getInstance();//万年历
                int year = cal.get(Calendar.YEAR);   //获取年
                this.age = year - birthyear;
            }
            resultSet.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }
    }

    public void getmusic(){ //根据年龄获取推荐
        Connection conn = null;
        String sql = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            if (this.age < 15){
                sql = "select * from music where musictype = '儿歌' or musictype = 'ACG'";
            }else if (this.age > 14 && this.age <= 20){
                sql = "select * from music where musictype = '华语流行' or musictype = '日语流行'";
            }else if (this.age > 20 && this.age <= 30){
                sql = "select * from music where musictype = 'RAP' or musictype = '民谣'";
            }else {
                sql = "select * from music where musictype = '怀旧'";
            }
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("musicid");
                Music music = new Music();
                music.getMusicbyid(id);
                musicList.add(music);
            }
            resultSet.close();
            preparedStatement.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }
    }


    public void getrecommend(List<Music> musicList1){
        musicList1.addAll(this.musicList);
    }



    public static void main(String[] args){
        AgeRecommend ageRecommend = new AgeRecommend(9);
        ageRecommend.getage();
        System.out.println(ageRecommend.getAge());
        ageRecommend.getmusic();
    }
}
