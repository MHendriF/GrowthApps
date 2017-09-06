package Master;

/**
 *   PT Trikarya Teknologi on 3/19/2016.
 */
public class City {
    private int kode,kodeArea;
    String nama;
    public City(int kode, int kodeArea, String nama)
    {
        setKode(kode);
        setKodeArea(kodeArea);
        setNama(nama);
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public int getKode() {
        return kode;
    }

    public int getKodeArea() {
        return kodeArea;
    }

    public void setKodeArea(int kodeArea) {
        this.kodeArea = kodeArea;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    @Override
    public String toString() {
        return getNama();
    }
}
