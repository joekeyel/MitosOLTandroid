package my.com.tm.moapps.mitosolt;

/**
 * Created by joe on 8/29/2016.
 */
public class schedulemodel {

    private String targetcabinet;
    private String migrationdate;
    private String stopdate;
    private String state;
    private String pmwno;
    private String ckc1;
    private String ckc2;
    private String oldcabinet;
    private String abbr;
    private String sitename;
    private String projecttype;
    private String migrationstatus;

    public String getMigrationstatus() {
        return migrationstatus;
    }

    public void setMigrationstatus(String migrationstatus) {
        this.migrationstatus = migrationstatus;
    }

    public String getProjecttype() {
        return projecttype;
    }

    public void setProjecttype(String projecttype) {
        this.projecttype = projecttype;
    }



    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getOldcabinet() {
        return oldcabinet;
    }

    public void setOldcabinet(String oldcabinet) {
        this.oldcabinet = oldcabinet;
    }

    public String getStatuscabinet() {
        return statuscabinet;
    }

    public void setStatuscabinet(String statuscabinet) {
        this.statuscabinet = statuscabinet;
    }

    private String statuscabinet;

    public String getTargetcabinet() {
        return targetcabinet;
    }

    public void setTargetcabinet(String targetcabinet) {
        this.targetcabinet = targetcabinet;
    }

    public String getMigrationdate() {
        return migrationdate;
    }

    public void setMigrationdate(String migrationdate) {
        this.migrationdate = migrationdate;
    }

    public String getStopdate() {
        return stopdate;
    }

    public void setStopdate(String stopdate) {
        this.stopdate = stopdate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPmwno() {
        return pmwno;
    }

    public void setPmwno(String pmwno) {
        this.pmwno = pmwno;
    }

    public String getCkc1() {
        return ckc1;
    }

    public void setCkc1(String ckc1) {
        this.ckc1 = ckc1;
    }

    public String getCkc2() {
        return ckc2;
    }

    public void setCkc2(String ckc2) {
        this.ckc2 = ckc2;
    }
}
