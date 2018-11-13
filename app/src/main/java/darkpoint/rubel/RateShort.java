package darkpoint.rubel;

import java.util.Date;

public class RateShort {
    private int Cur_ID;
    Date Date;
    double Cur_OfficialRate;

    RateShort(int cur_Id, Date date, double cur_OfficialRate) {
        this.Cur_ID = cur_Id;
        this.Date = date;
        this.Cur_OfficialRate = cur_OfficialRate;
    }

    public int getCur_ID() {
        return Cur_ID;
    }

    public void setCur_ID(int cur_ID) {
        this.Cur_ID = cur_ID;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        this.Date = date;
    }

    public double getCur_OfficialRate() {
        return Cur_OfficialRate;
    }

    public void setCur_OfficialRate(double cur_OfficialRate) {
        this.Cur_OfficialRate = cur_OfficialRate;
    }
}
