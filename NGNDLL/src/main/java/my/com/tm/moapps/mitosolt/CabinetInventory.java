package my.com.tm.moapps.mitosolt;

/**
 * Created by joe on 2/24/2016.
 */
public class CabinetInventory {

    private  int _id;
    private  String _cabinetid;
    private  String dsidepair;
    private  String esidepair;
    private  String dslin;
    private  String dslout;

    private  String subnumber;

    public String getEsidepair() {
        return esidepair;
    }

    public void setEsidepair(String esidepair) {
        this.esidepair = esidepair;
    }

    public String getDslin() {
        return dslin;
    }

    public void setDslin(String dslin) {
        this.dslin = dslin;
    }

    public String getDslout() {
        return dslout;
    }

    public void setDslout(String dslout) {
        this.dslout = dslout;
    }

    public String getSubnumber() {
        return subnumber;
    }

    public void setSubnumber(String subnumber) {
        this.subnumber = subnumber;
    }

    public String getBnumber() {
        return bnumber;
    }

    public void setBnumber(String bnumber) {
        this.bnumber = bnumber;
    }

    private  String bnumber;




    public CabinetInventory(){


    }

    public CabinetInventory(String cabinetid){

        this._cabinetid = cabinetid;
    }

    public void set_cabinetid(String _cabinetid) {
        this._cabinetid = _cabinetid;
    }

    public String getCabinetid() {
        return _cabinetid;
    }



    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDsidepair() {
        return dsidepair;
    }

    public void setDsidepair(String dsidepair) {
        this.dsidepair = dsidepair;
    }
}
