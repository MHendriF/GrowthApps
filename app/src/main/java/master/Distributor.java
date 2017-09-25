package master;

/**
 *   PT Trikarya Teknologi on 3/27/2016.
 */
public class Distributor {
    private int id,kd_kota;
    private String kd_dist,kd_tipe,nm_dist,almt_dist,telp_dist;
    public Distributor(int id, String kd_dist, String kd_tipe, int kd_kota, String nm_dist, String almt_dist, String telp_dist)
    {
        setId(id);
        setKd_dist(kd_dist);
        setKd_tipe(kd_tipe);
        setKd_kota(kd_kota);
        setNm_dist(nm_dist);
        setAlmt_dist(almt_dist);
        setTelp_dist(telp_dist);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setKd_dist(String kd_dist) {
        this.kd_dist = kd_dist;
    }

    public String getKd_dist() {
        return kd_dist;
    }

    public void setKd_tipe(String kd_tipe) {
        this.kd_tipe = kd_tipe;
    }

    public String getKd_tipe() {
        return kd_tipe;
    }

    public void setKd_kota(int kd_kota) {
        this.kd_kota = kd_kota;
    }

    public int getKd_kota() {
        return kd_kota;
    }

    public void setNm_dist(String nm_dist) {
        this.nm_dist = nm_dist;
    }

    public String getNm_dist() {
        return nm_dist;
    }

    public void setAlmt_dist(String almt_dist) {
        this.almt_dist = almt_dist;
    }

    public String getAlmt_dist() {
        return almt_dist;
    }

    public void setTelp_dist(String telp_dist) {
        this.telp_dist = telp_dist;
    }

    public String getTelp_dist() {
        return telp_dist;
    }

    @Override
    public String toString() {
        return getNm_dist();
    }
}
