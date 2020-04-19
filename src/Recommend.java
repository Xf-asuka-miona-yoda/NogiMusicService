import java.sql.*;
import java.util.*;

public class Recommend {
    private int userid;
    private UserScore ziji = new UserScore();

    private List<UserScore> Adjacentuserilist = new ArrayList<>(); //邻用户

    private List<Music> musicList = new ArrayList<>(); //推荐歌曲


    public void getmusic(List<Music> musicList1){  //获取到音乐
        //musicList1.addAll(musicList);
        for (int i = 0; i < 15; i++){
            Random random = new Random();//默认构造方法
            int j = random.nextInt(musicList.size());
            musicList1.add(musicList.get(j));
        }

        for (int i = 0; i < musicList1.size(); i++){  //去掉重复的推荐
            for (int j = musicList1.size() - 1; j > i ; j--){
                if (musicList1.get(j).musicid.equals(musicList1.get(i).musicid)){
                    musicList1.remove(j);
                }
            }
        }
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




    public void getadjacent(){  //获取邻用户
        Connection conn = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 在获取邻用户啦...");
            String sql = "select * from collection where userid= ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setObject(1,this.userid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                return;
            }else {
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


    public void calculatesimilarity(){ // 计算邻用户的相似度,并按照相似度顺序排序
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

    public void getrecommendmusic(){
        for (int i = 0; i < Adjacentuserilist.size(); i++){
            if (i == 5){
                break;
            }
            getcollection(Adjacentuserilist.get(i).getUserid());
        }

        for (int i = 0; i < musicList.size(); i++){  //去掉重复的历史记录
            for (int j = musicList.size() - 1; j > i ; j--){
                if (musicList.get(j).musicid.equals(musicList.get(i).musicid)){
                    musicList.remove(j);
                }
            }
        }
    }

    public void getcollection(int realuserid){ //获取用户的收藏歌曲

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql = "select * from collection where userid ='"+realuserid+"'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                int musicid = rs.getInt("musicid");
                Music music = new Music();
                music.getMusicbyid(musicid);
                musicList.add(music);
                // 输出数据
                System.out.println("歌曲id"+ musicid);
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

    }


    public static void main(String[] args){
        Recommend recommend = new Recommend(9);
        recommend.getadjacent();  //获取邻用户
        recommend.adjacentgetdata(); //计算邻用户的得分情况
        recommend.showadjacent();  //输出看看
        recommend.calculatesimilarity(); //计算邻用户的相似度并排序
        recommend.showadjacent(); //再看看
        recommend.getrecommendmusic(); //拿到推荐歌曲
    }


}
