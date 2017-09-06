package Master;

/**
 *   PT Trikarya Teknologi on 3/29/2016.
 */
public class Tipe {
    private int id;
    private String nm_tipe;
    public Tipe(int id,String nm_tipe)
    {
        setId(id);
        setNm_tipe(nm_tipe);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNm_tipe(String nm_tipe) {
        this.nm_tipe = nm_tipe;
    }

    public String getNm_tipe() {
        return nm_tipe;
    }

    @Override
    public String toString() {
        return getNm_tipe();
    }
}
