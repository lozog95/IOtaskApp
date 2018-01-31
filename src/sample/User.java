package sample;

public class User {
    private String user_login;
    private String user_password;
    private String user_name;
    private String user_surname;
    private Integer user_ID;

    public User(String user_login, String user_password) {
        this(user_login,user_password, "Imie", "Nazwisko",1);
    }

    public User(String user_login){
        this(user_login, "admin", user_login,"default", 1);
    }

    public User(String user_login, String user_password, String user_name, String user_surname, Integer user_ID) {
        this.user_login = user_login;
        this.user_password = user_password;
        this.user_name = user_name;
        this.user_surname = user_surname;
        this.user_ID = user_ID;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_surname() {
        return user_surname;
    }

    public void setUser_surname(String user_surname) {
        this.user_surname = user_surname;
    }

    public Integer getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(Integer user_ID) {
        this.user_ID = user_ID;
    }
}
