package master;

/**
 *   PT Trikarya Teknologi on 4/10/2016.
 */
public class PhotoActivity {
    private int id,kd_user,kd_outlet,kd_kompetitor,kd_tipe;
    private String nama,tgl_take,tgl_upload,alamat,keterangan,foto;
    public PhotoActivity(int id, int kd_user, int kd_outlet, int kd_kompetitor, int kd_tipe, String nama, String tgl_take, String alamat, String keterangan,String tgl_upload, String foto){
        setId(id);
        setKd_user(kd_user);
        setKd_outlet(kd_outlet);
        setKd_kompetitor(kd_kompetitor);
        setKd_tipe(kd_tipe);
        setNama(nama);
        setTgl_take(tgl_take);
        setAlamat(alamat);
        setKeterangan(keterangan);
        setTgl_upload(tgl_upload);
        setFoto(foto);
    }

    public int getKd_tipe() {
        return kd_tipe;
    }

    public void setKd_tipe(int kd_tipe) {
        this.kd_tipe = kd_tipe;
    }

    public void setTgl_upload(String tgl_upload) {
        this.tgl_upload = tgl_upload;
    }

    public String getTgl_upload() {
        return tgl_upload;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setKd_kompetitor(int kd_kompetitor) {
        this.kd_kompetitor = kd_kompetitor;
    }

    public void setKd_outlet(int kd_outlet) {
        this.kd_outlet = kd_outlet;
    }

    public void setKd_user(int kd_user) {
        this.kd_user = kd_user;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setTgl_take(String tgl_take) {
        this.tgl_take = tgl_take;
    }

    public int getId() {
        return id;
    }

    public int getKd_kompetitor() {
        return kd_kompetitor;
    }

    public int getKd_outlet() {
        return kd_outlet;
    }

    public int getKd_user() {
        return kd_user;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getNama() {
        return nama;
    }

    public String getTgl_take() {
        return tgl_take;
    }
}
