package master;

/**
 * Created by Hendry on 9/24/2017.
 */

public class Banner {
    public static String TABLE_NAME = "banner",
            COLUMN_ID = "id",
            COLUMN_FOTO = "foto",
            COLUMN_TANGGAL = "tanggal";
    int id;
    String image,tanggal;

    public Banner(int id, String image, String tanggal) {
        this.id = id;
        this.image = image;
        this.tanggal = tanggal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
