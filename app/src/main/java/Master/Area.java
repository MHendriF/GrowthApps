package Master;

/**
 *   PT Trikarya Teknologi on 3/19/2016.
 */
public class Area {
    private int kode;
    private String nama;
    public Area(int kode, String nama)
    {
        setNama(nama);
        setKode(kode);
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public int getKode() {
        return kode;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }
}
