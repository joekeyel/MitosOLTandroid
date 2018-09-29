package my.com.tm.moapps.mitosolt;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joe on 7/17/2016.
 */
public class auditmodel implements Parcelable {

    private String No;
    private String TTno;
    private String ServiceNo;
    private String lastmodify;
    private String updateby;
    private String remark;

    protected auditmodel(Parcel in) {
        No = in.readString();
        TTno = in.readString();
        ServiceNo = in.readString();
        lastmodify = in.readString();
        updateby = in.readString();
        remark = in.readString();
    }

    public static final Creator<auditmodel> CREATOR = new Creator<auditmodel>() {
        @Override
        public auditmodel createFromParcel(Parcel in) {
            return new auditmodel(in);
        }

        @Override
        public auditmodel[] newArray(int size) {
            return new auditmodel[size];
        }
    };

    public auditmodel() {

    }

    public String getLastmodify() {
        return lastmodify;
    }

    public void setLastmodify(String lastmodify) {
        this.lastmodify = lastmodify;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getServiceNo() {
        return ServiceNo;
    }

    public void setServiceNo(String serviceNo) {
        ServiceNo = serviceNo;
    }

    public String getTTno() {
        return TTno;
    }

    public void setTTno(String TTno) {
        this.TTno = TTno;
    }

    public String getUpdateby() {
        return updateby;
    }

    public void setUpdateby(String updateby) {
        this.updateby = updateby;
    }

    @Override
    public int describeContents() {
            return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(No);
        dest.writeString(TTno);
        dest.writeString(ServiceNo);
        dest.writeString(lastmodify);
        dest.writeString(updateby);
        dest.writeString(remark);
    }
}
