import java.sql.*;

public class UserScore {

    private int userid; //用户id

    /***
     * 各个类型的歌曲听歌数量
     */
    private int huayu;
    private int gufeng;
    private int riyu;
    private int acg;
    private int minyao;
    private int rap;
    private int huaijiu;
    private int erge;

    private int total; //听歌总量

    /**
     * 用户对各个类型的歌曲产生的评分
    */
    private double huayuscore;
    private double gufengscore;
    private double riyuscore;
    private double acgscore;
    private double minyaoscore;
    private double rapscore;
    private double huaijiuscore;
    private double ergescore;


    public void setUserid(int id){
        this.userid = id;
    }

    /**
     * 计算评分
     */
    public void calculatescore(){
        huayuscore = ((double)huayu)/total;
        gufengscore = ((double)gufeng)/total;
        riyuscore = ((double)riyu)/total;
        acgscore = ((double)acg)/total;
        minyaoscore = ((double)minyao)/total;
        rapscore = ((double)rap)/total;
        huaijiuscore = ((double)huaijiu)/total;
        ergescore = ((double)erge)/total;
    }

    /***
     * 获取用户听歌数据
     */
    public void getdata(){
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
                huayu = resultSet.getInt("华语流行");
                gufeng = resultSet.getInt("古风");
                riyu = resultSet.getInt("日语流行");
                acg = resultSet.getInt("ACG");
                minyao = resultSet.getInt("民谣");
                rap = resultSet.getInt("RAP");
                huaijiu = resultSet.getInt("怀旧");
                erge = resultSet.getInt("儿歌");
            }
            preparedStatement.close();
            conn.close();
        } catch (SQLException var23) {
            var23.printStackTrace();
        } catch (Exception var24) {
            var24.printStackTrace();
        }

        total = huayu + gufeng + riyu + acg + minyao + rap + huaijiu + erge;
    }

    public double calculatesimilarity(UserScore userScore){
        /***
         * 计算余弦相似度
         * 首先是分子，对应的评分相乘之后相加
         * 然后是分母，分为两部分，各自的评分平方和开根号之后相乘
         * 相除得到余弦相似度
         */
        double fenzi = this.huayuscore * userScore.huayuscore
                + this.gufengscore * userScore.gufengscore
                + this.riyuscore * userScore.riyuscore
                + this.acgscore * userScore.acgscore
                + this.minyaoscore * userScore.minyaoscore
                + this.rapscore * userScore.rapscore
                + this.huaijiuscore * userScore.huaijiuscore
                + this.ergescore * userScore.ergescore;

        double ziji = Math.sqrt(this.huayuscore * this.huayuscore
                                + this.gufengscore * this.gufengscore
                                + this.riyuscore * this.riyuscore
                                + this.acgscore * this.acgscore
                                + this.minyaoscore * this.minyaoscore
                                + this.rapscore * this.rapscore
                                + this.huaijiuscore * this.huaijiuscore
                                + this.ergescore * this.ergescore);

        double duifang = Math.sqrt(userScore.huayuscore * userScore.huayuscore
                                    + userScore.gufengscore * userScore.gufengscore
                                    + userScore.riyuscore * userScore.riyuscore
                                    + userScore.acgscore * userScore.acgscore
                                    + userScore.minyaoscore * userScore.minyaoscore
                                    + userScore.rapscore * userScore.rapscore
                                    + userScore.huaijiuscore * userScore.huaijiuscore
                                    + userScore.ergescore * userScore.ergescore);

        double fenmu = ziji * duifang;
        System.out.println("分子: " + fenzi + " 分母: " + fenmu);
        double result = fenzi/fenmu;
        return result;
    }

    public void show(){
        System.out.println("数量: " + huayu + " " + gufeng + " " + riyu + " " + acg + " " + minyao + " " + rap + " " + huaijiu + " " + erge);
        System.out.println("得分：" + huayuscore + " " + gufengscore + " " + riyuscore + " " + acgscore + " " + minyaoscore + " " + rapscore + " " + huaijiuscore + " " + ergescore);
    }

    public static void main(String[] args){
        UserScore userScore = new UserScore();
        userScore.setUserid(9);
        userScore.getdata();
        userScore.calculatescore();
        userScore.show();

        UserScore userScore1 = new UserScore();
        userScore1.setUserid(5);
        userScore1.getdata();
        userScore1.calculatescore();
        userScore1.show();

        double s = userScore.calculatesimilarity(userScore1);
        System.out.println("相似度为： " + s);
    }

}
