package darkpoint.rubel

import android.content.Context
import java.util.*

object Setting {

    private lateinit var four_currency: IntArray
    private var cur1: Int = 0
    private var cur2: Int = 0
    private var cur3: Int = 0
    private var cur4: Int = 0
    private lateinit var cur_one_scale: String
    private lateinit var cur_two_scale: String
    private lateinit var cur_three_scale: String
    private lateinit var cur_four_scale: String
    private var cur_one_value: Float = 0f
    private var cur_two_value: Float = 0f
    private var cur_three_value: Float = 0f
    private var cur_four_value: Float = 0f

    fun loadSetting(context: Context): IntArray {
        val preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE))
        cur1 = preferences.getInt("currency_1", 145)
        cur2 = preferences.getInt("currency_2", 292)
        cur3 = preferences.getInt("currency_3", 298)
        cur4 = preferences.getInt("currency_4", 293)
        four_currency = intArrayOf(cur1, cur2, cur3, cur4)
        return four_currency
    }

    fun saveSetting(context: Context, ids: IntArray) {
        val preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE))
        val editor = preferences.edit()
        editor.putInt("currency_1", ids[0])
        editor.putInt("currency_2", ids[1])
        editor.putInt("currency_3", ids[2])
        editor.putInt("currency_4", ids[3])
        editor.apply()
    }

    fun loadCacheCurrencyNames(context: Context): Array<String> {
        val preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE))
        cur_one_scale = preferences.getString("cur1scale", "1 USD")
        cur_two_scale = preferences.getString("cur2scale", "1 EUR")
        cur_three_scale = preferences.getString("cur3scale", "100 RUB")
        cur_four_scale = preferences.getString("cur4scale", "10 PLN")
        return arrayOf(cur_one_scale, cur_two_scale, cur_three_scale, cur_four_scale)
    }

    fun loadCacheCurrencyValues(context: Context): FloatArray {
        val preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE))
        cur_one_value = preferences.getFloat("cur1value", 0f)
        cur_two_value = preferences.getFloat("cur2value", 0f)
        cur_three_value = preferences.getFloat("cur3value", 0f)
        cur_four_value = preferences.getFloat("cur4value", 0f)
        return floatArrayOf(cur_one_value, cur_two_value, cur_three_value, cur_four_value)
    }

    fun loadCacheDate(context: Context): String {
        val preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE))
        return preferences.getString("Date", "01-10-2018")
    }

    fun saveCacheCurrencyNames(context: Context, one: String, two: String, three: String, four: String) {
        val preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE))
        val editor = preferences.edit()
        editor.putString("cur1scale", one)
        editor.putString("cur2scale", two)
        editor.putString("cur3scale", three)
        editor.putString("cur4scale", four)
        editor.apply()
    }

    fun saveCacheCurrencyValues(context: Context, one: Float, two: Float, three: Float, four: Float) {
        val preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE))
        val editor = preferences.edit()
        editor.putFloat("cur1value", one)
        editor.putFloat("cur2value", two)
        editor.putFloat("cur3value", three)
        editor.putFloat("cur4value", four)
        editor.apply()
    }

    fun saveCacheDate(context: Context, date: String) {
        val preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE))
        val editor = preferences.edit()
        editor.putString("Date", date)
        editor.apply()
    }
}
