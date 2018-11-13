package darkpoint.rubel;

import java.util.Date;

public class Currency extends Rate {
    public Currency(int cur_Id, Date date, String cur_Abbreviation, int cur_Scale, String cur_Name, double cur_OfficialRate) {
        super(cur_Id, date, cur_Abbreviation, cur_Scale, cur_Name, cur_OfficialRate);
    }
}
