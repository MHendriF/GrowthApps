package Master;

/**
 *   PT Trikarya Teknologi on 3/19/2016.
 */
public class User {
//    private int kode;
//    private int kd_kota;
//    private int kd_area;
//    private int kodeRole;
//    private int status;
//    private String NIK;
//    private String nama;
//    private String alamat;
//    private String telepon;
//    private String path_foto;
//    private String username;
//    private String password;
//    private String email;
//    private String gcmId;
//    private Integer toleransi;
//    public User(int kode, int kodeRole, int kd_kota,int kd_area,
//                String NIK, String nama, String alamat, String telepon, String path_foto,
//                String username, String password, String email, int status, String gcmId)
//    {
//        setKode(kode);
//        setKodeRole(kodeRole);
//        setNIK(NIK);
//        setNama(nama);
//        setAlamat(alamat);
//        setKd_kota(kd_kota);
//        setKd_area(kd_area);
//        setTelepon(telepon);
//        setPath_foto(path_foto);
//        setUsername(username);
//        setPassword(password);
//        setEmail(email);
//        setStatus(status);
//        setGcmId(gcmId);
//    }
//    public User(String username, String password)
//    {
//        setUsername(username);
//        setPassword(password);
//    }
//    public String getGcmId() {
//        return gcmId;
//    }
//
//    public void setGcmId(String gcmId) {
//        this.gcmId = gcmId;
//    }
//    public int getKd_area() {
//        return kd_area;
//    }
//
//    public void setKd_area(int kd_area) {
//        this.kd_area = kd_area;
//    }
//    public int getKode() {
//        return kode;
//    }
//
//    public void setKode(int kode) {
//        this.kode = kode;
//    }
//
//    public int getKodeRole() {
//        return kodeRole;
//    }
//
//    public void setKodeRole(int kodeRole) {
//        this.kodeRole = kodeRole;
//    }
//
//    public String getNIK() {
//        return NIK;
//    }
//
//    public void setNIK(String NIK) {
//        this.NIK = NIK;
//    }
//
//    public String getNama() {
//        return nama;
//    }
//
//    public void setNama(String nama) {
//        this.nama = nama;
//    }
//
//    public String getAlamat() {
//        return alamat;
//    }
//
//    public void setAlamat(String alamat) {
//        this.alamat = alamat;
//    }
//
//    public int getKd_kota() {
//        return kd_kota;
//    }
//
//    public void setKd_kota(int kd_kota) {
//        this.kd_kota = kd_kota;
//    }
//
//    public String getTelepon() {
//        return telepon;
//    }
//
//    public void setTelepon(String telepon) {
//        this.telepon = telepon;
//    }
//
//    public String getPath_foto() {
//        return path_foto;
//    }
//
//    public void setPath_foto(String path_foto) {
//        this.path_foto = path_foto;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username= username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password= password;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//
//    public int getStatus() {
//        return status;
//    }
//    public Integer getToleransi() {
//        return toleransi;
//    }
//
//    public void setToleransi(Integer toleransi) {
//        this.toleransi = toleransi;
//    }

    private int kode;
    private int kd_kota;
    private int kd_area;
    private int kodeRole;
    private int status;
    private String NIK;
    private String nama;
    private String alamat;
    private String telepon;
    private String path_foto;
    private String username;
    private String password;
    private String email;
    private String gcmId;
    private String area_code;
    private Integer toleransi;
    public User(int kode, int kodeRole, int kd_kota,int kd_area,
                String NIK, String nama, String alamat, String telepon, String path_foto,
                String username, String password, String email, int status, String gcmId)
    {
        setKode(kode);
        setKodeRole(kodeRole);
        setNIK(NIK);
        setNama(nama);
        setAlamat(alamat);
        setKd_kota(kd_kota);
        setKd_area(kd_area);
        setTelepon(telepon);
        setPath_foto(path_foto);
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setStatus(status);
        setGcmId(gcmId);
        //setArea_code(area_code);
    }
    public User(String username, String password)
    {
        setUsername(username);
        setPassword(password);
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }
    public int getKd_area() {
        return kd_area;
    }

    public void setKd_area(int kd_area) {
        this.kd_area = kd_area;
    }
    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public int getKodeRole() {
        return kodeRole;
    }

    public void setKodeRole(int kodeRole) {
        this.kodeRole = kodeRole;
    }

    public String getNIK() {
        return NIK;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
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

    public int getKd_kota() {
        return kd_kota;
    }

    public void setKd_kota(int kd_kota) {
        this.kd_kota = kd_kota;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getPath_foto() {
        return path_foto;
    }

    public void setPath_foto(String path_foto) {
        this.path_foto = path_foto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username= username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password= password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
    public Integer getToleransi() {
        return toleransi;
    }

    public void setToleransi(Integer toleransi) {
        this.toleransi = toleransi;
    }
}
