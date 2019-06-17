package darkpoint.rubel.customViews

import android.content.Context

import com.google.android.material.chip.Chip
import darkpoint.rubel.activities.CurrenciesIndex
import darkpoint.rubel.model.Rate

class CustomChip(context: Context) : Chip(context) {
    var selectedID: Int = 0
    lateinit var curIndex: CurrenciesIndex
    lateinit var rate: Rate
}
