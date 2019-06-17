package darkpoint.rubel.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "rubel_table")
data class Rate(

        @PrimaryKey
        @ColumnInfo(name = "cur_id")
        var Cur_ID: Int,

        @ColumnInfo(name = "date")
        var Date: String,

        @ColumnInfo(name = "abr")
        var Cur_Abbreviation: String,

        @ColumnInfo(name = "scale")
        var Cur_Scale: Int, var Cur_Name: String,

        @ColumnInfo(name = "rate")
        var Cur_OfficialRate: Double) : Serializable{

    @ColumnInfo(name = "difference")
    var difference: String = ""

    @ColumnInfo(name = "color")
    var color: Int = 0
}
//{"Cur_ID":145,"Date":"2018-09-03T00:00:00","Cur_Abbreviation":"USD","Cur_Scale":1,"Cur_Name":"Доллар США","Cur_OfficialRate":2.0839}
