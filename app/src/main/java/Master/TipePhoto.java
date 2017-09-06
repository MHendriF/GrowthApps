package Master;

/**
 *   PT Trikarya Teknologi on 4/29/2016.
 */
public class TipePhoto {
    private int id;
    private String nama_tipe;

    public TipePhoto(int id, String nama_tipe) {
        this.id = id;
        this.nama_tipe = nama_tipe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_tipe() {
        return nama_tipe;
    }

    public void setNama_tipe(String nama_tipe) {
        this.nama_tipe = nama_tipe;
    }

    @Override
    public String toString() {
        return getNama_tipe();
    }
}
