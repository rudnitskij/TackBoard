package morgun.dev.tackboard;

import javax.persistence.*;


/**
 * The class is used for describing users registered in the database
 * @author Vitaliy Morgun
 * @version 1.0
 * @since 01.11.2015
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    int id;

    /** login of the user, supposed to be spelled with latin letters */
    @Column(name = "log", unique = true)
    String log;

    /** password of the user, supposed to be spelled with latin letters */
    @Column(name = "pass")
    String pass;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User() {
    }

    public User(String log) {
        this.log = log;
    }

    public User(String log, String pass) {
        this.log = log;
        this.pass = pass;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", log='" + log + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
