public class Dynamic {

    private String dyid;
    private String userid; //用户id
    private String username; //用户昵称
    private String content; //内容
    private String year;
    private String month;
    private String date;
    private String hour;
    private String minute;
    private String second;
    private String zhuanfa; //转发数
    private String pinglun; //评论数
    private String dianzan; //点赞数

    public void setYear(String year) {
        this.year = year;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSecond() {
        return second;
    }

    public String getMinute() {
        return minute;
    }

    public String getHour() {
        return hour;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getDyid() {
        return dyid;
    }

    public String getDianzan() {
        return dianzan;
    }

    public String getUserid() {
        return userid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPinglun() {
        return pinglun;
    }

    public String getZhuanfa() {
        return zhuanfa;
    }

    public void setDianzan(String dianzan) {
        this.dianzan = dianzan;
    }

    public void setDyid(String dyid) {
        this.dyid = dyid;
    }

    public void setPinglun(String pinglun) {
        this.pinglun = pinglun;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setZhuanfa(String zhuanfa) {
        this.zhuanfa = zhuanfa;
    }
}
