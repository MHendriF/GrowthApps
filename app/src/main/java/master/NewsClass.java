package master;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Hendry on 9/26/2017.
 */

public class NewsClass implements Serializable, Comparable<NewsClass>{

    public static String table_name = "article",column_id = "id",column_status = "status",column_judul = "judul",
            column_headline = "headline",column_content = "content",column_image = "image",column_date = "date_upload";
    //public static
    int id, status;
    String judul,headline,content,image,date_upload;

    public NewsClass(int id, int status, String judul, String headline, String content, String image, String date_upload) {
        this.id = id;
        this.status = status;
        this.judul = judul;
        this.headline = headline;
        this.content = content;
        this.image = image;
        this.date_upload = date_upload;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate_upload() {
        return date_upload;
    }

    public void setDate_upload(String date_upload) {
        this.date_upload = date_upload;
    }

    @Override
    public int compareTo(NewsClass another) {
        //yyyy-mm-dd hh:ii:ss
        Calendar current = Calendar.getInstance();
        String[] date = getDate_upload().split(" ");
        String[] tanggal = date[0].split("-");
        String[] jam = date[1].split(":");
        current.set(Integer.valueOf(tanggal[0]),Integer.valueOf(tanggal[1])-1,Integer.valueOf(tanggal[2])
                ,Integer.valueOf(jam[0]),Integer.valueOf(jam[1]),Integer.valueOf(jam[2]));
        Calendar  other = Calendar.getInstance();
        String[] date1 = another.getDate_upload().split(" ");
        String[] tanggal1 = date1[0].split("-");
        String[] jam1 = date1[1].split(":");
        other.set(Integer.valueOf(tanggal1[0]),Integer.valueOf(tanggal1[1])-1,Integer.valueOf(tanggal1[2])
                ,Integer.valueOf(jam1[0]),Integer.valueOf(jam1[1]),Integer.valueOf(jam1[2]));
        return current.getTimeInMillis() > other.getTimeInMillis() ? -1
                : current.getTimeInMillis() < other.getTimeInMillis() ? 1
                : 0;
    }
}
