package my.com.tm.moapps.mitosolt;

/**
 * Created by joe on 3/5/2016.
 */
public class summarymodel {

    public int getTotalpending() {
        return totalpending;
    }

    public void setTotalpending(int totalpending) {
        this.totalpending = totalpending;
    }

    private String buildingid;
    private String basket;
    private String total;
    private  int totalpending;

    public String getBuildingid() {
        return buildingid;
    }

    public void setBuildingid(String buildingid) {
        this.buildingid = buildingid;
    }

    public String getBasket() {
        return basket;
    }

    public void setBasket(String basket) {
        this.basket = basket;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
