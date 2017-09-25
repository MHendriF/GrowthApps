package master;

/**
 *   PT Trikarya Teknologi on 4/1/2016.
 */
public class Produk {
    private int id;
    private String kd_produk,nm_produk;
    public Produk(int id, String kd_produk, String nm_produk)
    {
        setId(id);
        setKd_produk(kd_produk);
        setNm_produk(nm_produk);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKd_produk(String kd_produk) {
        this.kd_produk = kd_produk;
    }

    public void setNm_produk(String nm_produk) {
        this.nm_produk = nm_produk;
    }

    public int getId() {
        return id;
    }

    public String getKd_produk() {
        return kd_produk;
    }

    public String getNm_produk() {
        return nm_produk;
    }

    @Override
    public String toString() {
        return getNm_produk();
    }
}
