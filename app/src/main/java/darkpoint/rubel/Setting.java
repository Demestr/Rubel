package darkpoint.rubel;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Objects;

public final class Setting {

    private static int[] four_currency;
    private static int cur1, cur2, cur3, cur4;
    private static String cur_one_scale, cur_two_scale, cur_three_scale, cur_four_scale;
    private static float cur_one_value, cur_two_value, cur_three_value, cur_four_value;

    public static int[] loadSetting(Context context)
    {
        SharedPreferences preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE));
        cur1 = preferences.getInt("currency_1", 145);
        cur2 = preferences.getInt("currency_2", 292);
        cur3 = preferences.getInt("currency_3", 298);
        cur4 = preferences.getInt("currency_4", 293);
        four_currency = new int[]{cur1, cur2, cur3, cur4};
        return four_currency;
    }

    public static void saveSetting(Context context, int[] ids)
    {
        SharedPreferences preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("currency_1", ids[0]);
        editor.putInt("currency_2", ids[1]);
        editor.putInt("currency_3", ids[2]);
        editor.putInt("currency_4", ids[3]);
        editor.apply();
    }

    public static String[] loadCacheCurrencyNames(Context context)
    {
        SharedPreferences preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE));
        cur_one_scale = preferences.getString("cur1scale", "1 USD");
        cur_two_scale = preferences.getString("cur2scale", "1 EUR");
        cur_three_scale = preferences.getString("cur3scale", "100 RUB");
        cur_four_scale = preferences.getString("cur4scale", "10 PLN");
        return new String[] {cur_one_scale, cur_two_scale, cur_three_scale, cur_four_scale};
    }

    public static float[] loadCacheCurrencyValues(Context context)
    {
        SharedPreferences preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE));
        cur_one_value = preferences.getFloat("cur1value", 0);
        cur_two_value = preferences.getFloat("cur2value", 0);
        cur_three_value = preferences.getFloat("cur3value", 0);
        cur_four_value = preferences.getFloat("cur4value", 0);
        return new float[] {cur_one_value, cur_two_value, cur_three_value, cur_four_value};
    }

    public static String loadCacheDate(Context context){
        SharedPreferences preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE));
        return preferences.getString("date", "01-10-2018");
    }

    public static void saveCacheCurrencyNames(Context context, String one, String two, String  three, String four)
    {
        SharedPreferences preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cur1scale", one);
        editor.putString("cur2scale", two);
        editor.putString("cur3scale", three);
        editor.putString("cur4scale", four);
        editor.apply();
    }

    public static void saveCacheCurrencyValues(Context context, float one, float two, float three, float four)
    {
        SharedPreferences preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("cur1value", one);
        editor.putFloat("cur2value", two);
        editor.putFloat("cur3value", three);
        editor.putFloat("cur4value", four);
        editor.apply();
    }

    public static void saveCacheDate(Context context, String date)
    {
        SharedPreferences preferences = Objects.requireNonNull(context.getSharedPreferences("rubel_settings", Context.MODE_PRIVATE));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("date", date);
        editor.apply();
    }
}
