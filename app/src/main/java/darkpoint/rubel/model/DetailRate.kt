package darkpoint.rubel.model

import android.graphics.Color
import android.view.View
import java.math.BigDecimal
import java.math.RoundingMode

class DetailRate(previousRate: RateShort, actualRate: RateShort) {
    var rate: String = actualRate.Cur_OfficialRate.toString()
    var color = 0
    var grow: String
    var difference: String
    private var diff = 0.0
    var visibility = View.VISIBLE
    init {
        diff = BigDecimal(actualRate.Cur_OfficialRate - previousRate.Cur_OfficialRate).setScale(4, RoundingMode.UP).toDouble()
        when {
            diff > 0.0 -> {
                grow ="\uf106"
                color = Color.GREEN
                difference = "+$diff"
            }
            diff < 0.0 -> {
                grow = "\uf107"
                color = Color.RED
                difference = diff.toString()
            }
            diff == 0.0  -> {
                grow = "-"
                color = Color.WHITE
                difference = ""
            }
            else -> {
                grow = ""
                color = Color.WHITE
                difference = ""
            }
        }
    }

    constructor() : this(RateShort(999, "", 0.0), RateShort(999, "", 0.0)){
        rate = "-"
        difference = ""
        grow = ""
        visibility = View.GONE
    }
}