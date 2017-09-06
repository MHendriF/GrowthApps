package Master;

/**
 *   PT Trikarya Teknologi on 3/19/2016.
 */
public class Outlet {
    private int kode;
    private int kode_kota;
    private int kode_distributor;
    private int kode_user;
    private int tipe;
    private int status_area;
    private String nama,alamat,rank,telpon,reg_status,longitude,latitude;
    public Outlet(int kd,int kd_kota,int kd_user,int kode_distributor,String nama, String alamat, int tipe, String rank, String telpon, String reg_status, String latitude, String longitude){
        setKode(kd);
        setKode_kota(kd_kota);
        setKode_user(kd_user);
        setNama(nama);
        setAlamat(alamat);
        setKode_distributor(kode_distributor);
        setTipe(tipe);
        setRank(rank);
        setTelpon(telpon);
        setReg_status(reg_status);
        setLatitude(latitude);
        setLongitude(longitude);
    }
    public int getStatus_area() {
        return status_area;
    }

    public void setStatus_area(int status_area) {
        this.status_area = status_area;
    }
    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public int getKode_kota() {
        return kode_kota;
    }

    public void setKode_kota(int kode_kota) {
        this.kode_kota = kode_kota;
    }

    public int getKode_distributor() {
        return kode_distributor;
    }

    public void setKode_distributor(int kode_distributor) {
        this.kode_distributor = kode_distributor;
    }

    public int getKode_user() {
        return kode_user;
    }

    public void setKode_user(int kode_user) {
        this.kode_user = kode_user;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public int getTipe() {
        return tipe;
    }

    public void setTipe(int tipe) {
        this.tipe = tipe;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTelpon() {
        return telpon;
    }

    public void setTelpon(String telpon) {
        this.telpon = telpon;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
         this.latitude = latitude;
    }

    public String getReg_status() {
        return reg_status;
    }

    public void setReg_status(String reg_status) {
        this.reg_status = reg_status;
    }

    @Override
    public String toString() {
        return this.getNama();
    }
}
