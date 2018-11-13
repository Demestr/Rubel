package darkpoint.rubel;

import java.io.Serializable;
import java.util.Date;


public class Rate implements Serializable {

        int Cur_ID;
        Date Date;
        String Cur_Abbreviation;
        int Cur_Scale;
        String Cur_Name;
        double Cur_OfficialRate;

        public Rate(int cur_Id, Date date, String cur_Abbreviation, int cur_Scale, String cur_Name, double cur_OfficialRate) {
            this.Cur_ID = cur_Id;
            this.Date = date;
            this.Cur_Abbreviation = cur_Abbreviation;
            this.Cur_Scale = cur_Scale;
            this.Cur_Name = cur_Name;
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

    public String getCur_Abbreviation() {
        return Cur_Abbreviation;
    }

    public void setCur_Abbreviation(String cur_Abbreviation) {
        this.Cur_Abbreviation = cur_Abbreviation;
    }

    public int getCur_Scale() {
        return Cur_Scale;
    }

    public void setCur_Scale(int cur_Scale) {
        this.Cur_Scale = cur_Scale;
    }

    public String getCur_Name() {
        return Cur_Name;
    }

    public void setCur_Name(String cur_Name) {
        this.Cur_Name = cur_Name;
    }

    public double getCur_OfficialRate() {
        return Cur_OfficialRate;
    }

    public void setCur_OfficialRate(double cur_OfficialRate) {
        this.Cur_OfficialRate = cur_OfficialRate;
    }

    //{"Cur_ID":145,"Date":"2018-09-03T00:00:00","Cur_Abbreviation":"USD","Cur_Scale":1,"Cur_Name":"Доллар США","Cur_OfficialRate":2.0839}
}
