package Master;

/**
 *   PT Trikarya Teknologi on 4/14/2016.
 */
public class Competitor {
    private int id,kd_kota;

    public String getNm_competitor() {
        return nm_competitor;
    }

    public void setNm_competitor(String nm_competitor) {
        this.nm_competitor = nm_competitor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKd_kota() {
        return kd_kota;
    }

    public void setKd_kota(int kd_kota) {
        this.kd_kota = kd_kota;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    private String nm_competitor,alamat;

    public Competitor(int id, int kd_kota, String nm_competitor, String alamat) {
        this.id = id;
        this.kd_kota = kd_kota;
        this.nm_competitor = nm_competitor;
        this.alamat = alamat;
    }

    public Competitor(int kd_kota, String nm_competitor, String alamat) {
        this.kd_kota = kd_kota;
        this.nm_competitor = nm_competitor;
        this.alamat = alamat;
    }

    @Override
    public String toString() {
        return getNm_competitor();
    }
}
