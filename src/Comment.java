public class Comment {
    private String username; //用户昵称
    private String content; //内容
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String second;

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getSecond() {
        return second;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
