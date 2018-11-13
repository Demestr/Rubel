package darkpoint.rubel;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;

/**
 * Implementation of App Widget functionality.
 */
public class RubelWidget extends AppWidgetProvider {

    static private int[] four_currency;
    private static boolean connection;
    static private HashMap<Integer, Rate> rateHashMap = new HashMap<>();
    static private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    static private SimpleDateFormat simpleDateFormattop = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private static final String SYNC_CLICKED = "automaticWidgetSyncButtonClick";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent_start = new Intent(context, SplashActivity.class);
        PendingIntent pIntent_start = PendingIntent.getBroadcast(context, appWidgetId, intent_start, 0);
        connection = MainActivity.hasConnection(context);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.rubel_widget);
        views.setOnClickPendingIntent(R.id.appwidget_text, pIntent_start);
        views.setOnClickPendingIntent(R.id.area_curs, pIntent_start);
        views.setTextViewText(R.id.wcur_one_name, "");
        views.setTextViewText(R.id.wcur_one_value, "");
        views.setTextViewText(R.id.wcur_two_name, "");
        views.setTextViewText(R.id.wcur_two_value, "");
        views.setTextViewText(R.id.wcur_three_name, "");
        views.setTextViewText(R.id.wcur_three_value, "");
        views.setTextViewText(R.id.wcur_four_name, "");
        views.setTextViewText(R.id.wcur_four_value, "");
        Intent intent = new Intent(context, RubelWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                new int[]{appWidgetId});
        PendingIntent pIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.sync_button, pIntent);

        four_currency = Setting.loadSetting(context);
        if (connection) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    getCurrenciesMain(four_currency, Calendar.getInstance().getTime());
                }
            });
            thread.start();
            try {
                while (true) {
                    Thread.sleep(300);
                    if (!thread.isAlive()) {
                        for (int i = 0; i < four_currency.length; i++) {
                            if (i == 0) {
                                views.setTextViewText(R.id.wcur_one_name, rateHashMap.get(four_currency[i]).Cur_Scale + " " + rateHashMap.get(four_currency[i]).Cur_Abbreviation);
                                views.setTextViewText(R.id.wcur_one_value, String.valueOf(rateHashMap.get(four_currency[i]).Cur_OfficialRate));
                            }
                            if (i == 1) {
                                views.setTextViewText(R.id.wcur_two_name, rateHashMap.get(four_currency[i]).Cur_Scale + " " + rateHashMap.get(four_currency[i]).Cur_Abbreviation);
                                views.setTextViewText(R.id.wcur_two_value, String.valueOf(rateHashMap.get(four_currency[i]).Cur_OfficialRate));
                            }
                            if (i == 2) {
                                views.setTextViewText(R.id.wcur_three_name, rateHashMap.get(four_currency[i]).Cur_Scale + " " + rateHashMap.get(four_currency[i]).Cur_Abbreviation);
                                views.setTextViewText(R.id.wcur_three_value, String.valueOf(rateHashMap.get(four_currency[i]).Cur_OfficialRate));
                            }
                            if (i == 3) {
                                views.setTextViewText(R.id.wcur_four_name, rateHashMap.get(four_currency[i]).Cur_Scale + " " + rateHashMap.get(four_currency[i]).Cur_Abbreviation);
                                views.setTextViewText(R.id.wcur_four_value, String.valueOf(rateHashMap.get(four_currency[i]).Cur_OfficialRate));
                            }
                        }
                        CharSequence widgetText = context.getString(R.string.appwidget_text) + " " + simpleDateFormattop.format(Calendar.getInstance().getTime());
                        views.setTextViewText(R.id.appwidget_text, widgetText);
                        Toast.makeText(context, "Обновлено", Toast.LENGTH_SHORT).show();
                        Setting.saveCacheCurrencyNames(context, rateHashMap.get(four_currency[0]).getCur_Scale() + " " + rateHashMap.get(four_currency[0]).getCur_Abbreviation(),
                                rateHashMap.get(four_currency[1]).getCur_Scale() + " " + rateHashMap.get(four_currency[1]).getCur_Abbreviation(),
                                rateHashMap.get(four_currency[2]).getCur_Scale() + " " + rateHashMap.get(four_currency[2]).getCur_Abbreviation(),
                                rateHashMap.get(four_currency[3]).getCur_Scale() + " " + rateHashMap.get(four_currency[3]).getCur_Abbreviation());
                        Setting.saveCacheCurrencyValues(context, (float)rateHashMap.get(four_currency[0]).getCur_OfficialRate(), (float)rateHashMap.get(four_currency[1]).getCur_OfficialRate(),
                                (float)rateHashMap.get(four_currency[2]).getCur_OfficialRate(), (float)rateHashMap.get(four_currency[3]).getCur_OfficialRate());
                        Setting.saveCacheDate(context, simpleDateFormattop.format(Calendar.getInstance().getTime()));
                        break;
                    }
                }

            } catch (Exception e) {
                appWidgetManager.updateAppWidget(appWidgetId, views);
                Toast.makeText(context, "Ошибка, проверьте подключение к интернету!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            CharSequence widgetText = context.getString(R.string.appwidget_text) + " " + Setting.loadCacheDate(context);
            views.setTextViewText(R.id.appwidget_text, widgetText);
            String[] cacheNames = Setting.loadCacheCurrencyNames(context);
            float[] cacheValues = Setting.loadCacheCurrencyValues(context);
            views.setTextViewText(R.id.wcur_one_name, cacheNames[0]);
            views.setTextViewText(R.id.wcur_one_value, String.valueOf(cacheValues[0]));
            views.setTextViewText(R.id.wcur_two_name, cacheNames[1]);
            views.setTextViewText(R.id.wcur_two_value, String.valueOf(cacheValues[1]));
            views.setTextViewText(R.id.wcur_three_name, cacheNames[2]);
            views.setTextViewText(R.id.wcur_three_value, String.valueOf(cacheValues[2]));
            views.setTextViewText(R.id.wcur_four_name, cacheNames[3]);
            views.setTextViewText(R.id.wcur_four_value, String.valueOf(cacheValues[3]));
            Toast.makeText(context, "Подключение к интернету отсутствует! Загружены данные последнего сеанса.", Toast.LENGTH_LONG).show();
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static private void getCurrenciesMain(final int[] currencies_id, final Date date) {
        final ServerAPI serverAPI = APIFactory.createApi();

        for (int aCurrencies_id : currencies_id) {

            Call<Rate> currencyCall = serverAPI.getRateOnDate(aCurrencies_id, simpleDateFormat.format(date));

            try {
                Rate rate = currencyCall.execute().body();
                rateHashMap.put(aCurrencies_id, rate);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

