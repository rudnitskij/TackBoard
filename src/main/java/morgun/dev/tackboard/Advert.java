package morgun.dev.tackboard;

import javax.persistence.*;
import java.util.Calendar;

/**
 * The class is used for describing advertisements registered in the database
 *
 * @author Vitaliy Morgun
 * @version 1.0
 * @since 01.11.2015
 */
@Entity
@Table(name = "ads")
public class Advert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ad_id")
    int id;

    /**
     * login of the user written the advertisement
     */
    @Column(name = "author", length = 30, nullable = false)
    public String author;

    /**
     * topic of the advertisement
     */
    @Column(name = "topic", length = 10, nullable = false)
    public String topic;

    /**
     * moment of the advertisement publishing
     */
    @Column(name = "pubdate")
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar issueDate;

    /**
     * header of the advertisement
     */
    @Column(name = "header", length = 30, nullable = false)
    public String header;

    /**
     * content of the advertisement
     */
    @Column(name = "advert", length = 511, nullable = false)
    public String text;

    /**
     * ending of the path to the picture illustrating the advertisement
     */
    @Column(name = "picturelink")
    public String pictureLink;


    public Advert() {
    }

    public Advert(String topic, Calendar issueDate, String header, String text, String pictureLink, String author) {
        this.topic = topic;
        this.issueDate = issueDate;
        this.header = header;
        this.text = text;
        this.pictureLink = pictureLink;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Calendar getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Calendar issueDate) {
        this.issueDate = issueDate;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * method returning the topic of the advertisement translated to Russian
     */
    public static String getTopicRus(String topic) {
        String rusResult = "";
        switch (topic) {
            case "sale":
                rusResult = "продажа";
                break;
            case "buy":
                rusResult = "покупка";
                break;
            case "rent":
                rusResult = "аренда";
                break;
            case "service":
                rusResult = "услуги";
                break;
            case "dating":
                rusResult = "знакомства";
                break;
            case "etc":
                rusResult = "разное";
                break;
            default:
                rusResult = "без рубрики";
        }
        return rusResult;
    }

    @Override
    public boolean equals(Object advertToCompare) {
        if (this == advertToCompare) return true;
        if (advertToCompare == null || getClass() != advertToCompare.getClass()) return false;

        Advert advert = (Advert) advertToCompare;

        if (id != advert.id) return false;
        if (!author.equals(advert.author)) return false;
        if (!topic.equals(advert.topic)) return false;
        if (!issueDate.equals(advert.issueDate)) return false;
        if (!header.equals(advert.header)) return false;
        if (!text.equals(advert.text)) return false;
        return pictureLink.equals(advert.pictureLink);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + author.hashCode();
        result = 31 * result + topic.hashCode();
        result = 31 * result + issueDate.hashCode();
        result = 31 * result + header.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + pictureLink.hashCode();
        return result;
    }
}
