import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Recommend {
    private int userid;
    private UserScore ziji = new UserScore();

    private List<UserScore> Adjacentuserilist = new ArrayList<>();

    private List<Music> musicList = new ArrayList<>();


    public void getmusic(List<Music> musicList1){
        musicList1.addAll(musicList);
    }

    public Recommend(int id){
        this.userid = id;
        ziji.setUserid(this.userid);
        ziji.getdata();
        ziji.calculatescore();
        ziji.show();
    }

    public void adjacentgetdata(){
        for (int i = 0; i < Adjacentuserilist.size(); i++){  //去掉重复的邻用户
            for (int j = Adjacentuserilist.size() - 1; j > i ; j--){
                if (Adjacentuserilist.get(j).getUserid() == Adjacentuserilist.get(i).getUserid()){
                    Adjacentuserilist.remove(j);
                }
            }
        }

        for (int i = 0; i < Adjacentuserilist.size(); i++){
            Adjacentuserilist.get(i).getdata();
            Adjacentuserilist.get(i).calculatescore();
        }
    }


    public void showadjacent(){
        for (int i = 0; i < Adjacentuserilist.size(); i++){
            Adjacentuserilist.get(i).show();
        }
    }




    public void getadjacent(){
        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            String sql = "select * from collection where userid= ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setObject(1,this.userid);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int musicid = resultSet.getInt("musicid");
                String sql1 = "select * from collection where musicid= ? and userid != ?";
                PreparedStatement ps = conn.prepareStatement(sql1);
                ps.setObject(1,musicid);
                ps.setObject(2,this.userid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int id = rs.getInt("userid");
                    UserScore userScore = new UserScore();
                    userScore.setUserid(id);
                    Adjacentuserilist.add(userScore);
                }
                rs.close();
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


    public void calculatesimilarity(){ // 计算邻用户的相似度
        for (int i = 0; i < Adjacentuserilist.size(); i++){
            ziji.calculatesimilarity(Adjacentuserilist.get(i));
            Adjacentuserilist.get(i).showsimilarity();
        }

        Collections.sort(Adjacentuserilist, new Comparator<UserScore>() { //按照相似度进行排序
            @Override
            public int compare(UserScore o1, UserScore o2) {
                double i = o1.getSimilarity() - o2.getSimilarity();
                if (i > 0){   //o1的相似度较高
                    return -1;
                }else if (i < 0){ //o2较高
                    return 1;
                }
                return 0;  //相等
            }
        });
    }


    public static void main(String[] args){
        Recommend recommend = new Recommend(9);
        recommend.getadjacent();
        recommend.adjacentgetdata();
        recommend.showadjacent();
        recommend.calculatesimilarity();
        recommend.showadjacent();
    }


}
