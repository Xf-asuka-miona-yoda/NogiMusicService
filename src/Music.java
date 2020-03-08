import java.sql.*;

public class Music {
    public String musicname;
    public String musicurl;
    public String musicpic;
    public String singer;

    public void setMusicname(String musicname) {
        this.musicname = musicname;
    }

    public void setMusicpic(String musicpic) {
        this.musicpic = musicpic;
    }

    public void setMusicurl(String musicurl) {
        this.musicurl = musicurl;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void getMusicbyid(int id){
        Connection conn = null;
        Statement stmt = null;
        Statement stmt1 = null;
        try {
            Class.forName(JdbcUTil.JDBC_DRIVER);
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(JdbcUTil.DB_URL, JdbcUTil.USER, JdbcUTil.PASS);
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            stmt1 = conn.createStatement();
            String sql = "select musicname, singerid, musicurl, musicpic from music where musicid='"+id+"'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                // 通过字段检索
                int singerid = rs.getInt("singerid");
                String sql_singer = "select singername from singer where singerid='"+singerid+"'";
                ResultSet rs1 = stmt1.executeQuery(sql_singer);
                while(rs1.next()){
                    String singername = rs1.getString("singername");
                    System.out.println("歌手: " + singername);
                    setSinger(singername);
                }
                rs1.close();
                String name = rs.getString("musicname");
                setMusicname(name);
                String url = rs.getString("musicurl");
                setMusicurl(url);
                String pic = rs.getString("musicpic");
                setMusicpic(pic);

                // 输出数据

                System.out.print(", 歌曲名称: " + name);
                System.out.print(", 播放 URL: " + url);
                System.out.print(", 图片 URL: " + pic);
                System.out.print("\n");
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
        Music r = new Music();
        r.getMusicbyid(2);
    }
}
