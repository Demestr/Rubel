package darkpoint.rubel

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import darkpoint.rubel.activities.LoadingStatus
import darkpoint.rubel.database.RubelDatabase
import darkpoint.rubel.model.Rate
import darkpoint.rubel.network.APIFactory
import kotlinx.coroutines.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private const val SYNCCLICKED = "syncButtonClick"

class RubelWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so insert all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (SYNCCLICKED == intent!!.action) {
            if (!isAlive) {
                val appmngr = AppWidgetManager.getInstance(context!!)
                val id = intent.extras.getInt("key")
                updateAppWidget(context, appmngr, id)
            } else {
                val ids = Setting.loadSetting(context!!)
                getCurrenciesMain(ids, Calendar.getInstance().time)
            }
        }
    }

    companion object {

        private lateinit var dataBase: RubelDatabase
        private var isAlive = false
        private var fourCurrency: IntArray? = null
        @SuppressLint("ConstantLocale")
        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        private var job = Job()
        private val coroutineScope = CoroutineScope(job + Dispatchers.Main)
        private val curOne = MutableLiveData<Rate>()
        private val curTwo = MutableLiveData<Rate>()
        private val curThree = MutableLiveData<Rate>()
        private val curFour = MutableLiveData<Rate>()
        private val status = MutableLiveData<LoadingStatus>()

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            dataBase = RubelDatabase.getInstance(context)
            fourCurrency = Setting.loadSetting(context)
            val views = RemoteViews(context.packageName, R.layout.rubel_widget)
            views.setOnClickPendingIntent(R.id.sync_button, buildButtonPendingIntent(context, appWidgetId))

            status.observeForever {
                when (it) {
                    LoadingStatus.LOADING -> {
                        views.setViewVisibility(R.id.progressBarWidget, View.VISIBLE)
                        views.setViewVisibility(R.id.sync_button, View.GONE)
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                    LoadingStatus.DONE -> {
                        Toast.makeText(context, "Обновлено", Toast.LENGTH_SHORT).show()
                        views.setTextViewText(R.id.appwidget_text, context.getString(R.string.appwidget_text, DateFormat.getDateInstance(DateFormat.LONG).format(Calendar.getInstance().time)))
                        views.setViewVisibility(R.id.progressBarWidget, View.GONE)
                        views.setViewVisibility(R.id.sync_button, View.VISIBLE)
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                    else -> {
                        views.setViewVisibility(R.id.progressBarWidget, View.GONE)
                        views.setViewVisibility(R.id.sync_button, View.VISIBLE)
                        coroutineScope.launch {
                            val list = getRates()
                            if(list[0] == null){
                                Toast.makeText(context, "Ошибка! Проверьте подключение к интернету!", Toast.LENGTH_SHORT).show()
                                views.setTextViewText(R.id.appwidget_text, "Нет данных вообще!")
                                appWidgetManager.updateAppWidget(appWidgetId, views)
                                return@launch
                            }
                            curOne.value = list[0]
                            curTwo.value = list[1]
                            curThree.value = list[2]
                            curFour.value = list[3]
                            Toast.makeText(context, "Загружены данные последнего сеанса", Toast.LENGTH_SHORT).show()
                            views.setTextViewText(R.id.appwidget_text, context.getString(R.string.appwidget_text, DateFormat.getDateInstance(DateFormat.LONG).format(simpleDateFormat.parse(list[0]!!.Date))))
                            appWidgetManager.updateAppWidget(appWidgetId, views)
                        }
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                }
            }

            curOne.observeForever {
                views.setTextViewText(R.id.wcur_one_name, context.getString(R.string.setting_view, it.Cur_Scale, it.Cur_Abbreviation))
                views.setTextViewText(R.id.wcur_one_value, it.Cur_OfficialRate.toString())
                views.setTextViewText(R.id.wcur_one_move, it.difference)
                views.setTextColor(R.id.wcur_one_move, it.color)
            }

            curTwo.observeForever {
                views.setTextViewText(R.id.wcur_two_name, context.getString(R.string.setting_view, it.Cur_Scale, it.Cur_Abbreviation))
                views.setTextViewText(R.id.wcur_two_value, it.Cur_OfficialRate.toString())
                views.setTextViewText(R.id.wcur_two_move, it.difference)
                views.setTextColor(R.id.wcur_two_move, it.color)
            }

            curThree.observeForever {
                views.setTextViewText(R.id.wcur_three_name, context.getString(R.string.setting_view, it.Cur_Scale, it.Cur_Abbreviation))
                views.setTextViewText(R.id.wcur_three_value, it.Cur_OfficialRate.toString())
                views.setTextViewText(R.id.wcur_three_move, it.difference)
                views.setTextColor(R.id.wcur_three_move, it.color)
            }

            curFour.observeForever {
                views.setTextViewText(R.id.wcur_four_name, context.getString(R.string.setting_view, it.Cur_Scale, it.Cur_Abbreviation))
                views.setTextViewText(R.id.wcur_four_value, it.Cur_OfficialRate.toString())
                views.setTextViewText(R.id.wcur_four_move, it.difference)
                views.setTextColor(R.id.wcur_four_move, it.color)
            }

            getCurrenciesMain(fourCurrency!!, Calendar.getInstance().time)
            appWidgetManager.updateAppWidget(appWidgetId, views)
            isAlive = true
        }

        private fun getCurrenciesMain(currencies_id: IntArray, date: Date) {
            val dateString = simpleDateFormat.format(date)
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, -1)
            val dateStringY = simpleDateFormat.format(calendar.time)

            coroutineScope.launch {
                val getRateOne = APIFactory.createApi().getRateOnDateAsync(currencies_id[0], dateString)
                val getRateOneY = APIFactory.createApi().getRateOnDateAsync(currencies_id[0], dateStringY)
                val getRateTwo = APIFactory.createApi().getRateOnDateAsync(currencies_id[1], dateString)
                val getRateTwoY = APIFactory.createApi().getRateOnDateAsync(currencies_id[1], dateStringY)
                val getRateThree = APIFactory.createApi().getRateOnDateAsync(currencies_id[2], dateString)
                val getRateThreeY = APIFactory.createApi().getRateOnDateAsync(currencies_id[2], dateStringY)
                val getRateFour = APIFactory.createApi().getRateOnDateAsync(currencies_id[3], dateString)
                val getRateFourY = APIFactory.createApi().getRateOnDateAsync(currencies_id[3], dateStringY)

                try {
                    status.value = LoadingStatus.LOADING
                    val r1Y = getRateOneY.await()
                    val r1 = getRateOne.await()
                    val r2Y = getRateTwoY.await()
                    val r2 = getRateTwo.await()
                    val r3Y = getRateThreeY.await()
                    val r3 = getRateThree.await()
                    val r4Y = getRateFourY.await()
                    val r4 = getRateFour.await()
                    r1.difference = getMove(r1.Cur_OfficialRate - r1Y.Cur_OfficialRate)
                    r1.color = getMoveColor(r1.Cur_OfficialRate - r1Y.Cur_OfficialRate)
                    curOne.value = r1
                    r2.difference = getMove(r2.Cur_OfficialRate - r2Y.Cur_OfficialRate)
                    r2.color = getMoveColor(r2.Cur_OfficialRate - r2Y.Cur_OfficialRate)
                    curTwo.value = r2
                    r3.difference = getMove(r3.Cur_OfficialRate - r3Y.Cur_OfficialRate)
                    r3.color = getMoveColor(r3.Cur_OfficialRate - r3Y.Cur_OfficialRate)
                    curThree.value = r3
                    r4.difference = getMove(r4.Cur_OfficialRate - r4Y.Cur_OfficialRate)
                    r4.color = getMoveColor(r4.Cur_OfficialRate - r4Y.Cur_OfficialRate)
                    curFour.value = r4
                    insert(r1)
                    insert(r2)
                    insert(r3)
                    insert(r4)
                    status.value = LoadingStatus.DONE
                } catch (e: Exception) {
                    status.value = LoadingStatus.ERROR
                }
            }
        }

        private suspend fun getRates() : List<Rate?>{
            return withContext(Dispatchers.IO) {
                val list = listOf(
                dataBase.rubelDatabaseDao.get(fourCurrency!![0]),
                dataBase.rubelDatabaseDao.get(fourCurrency!![1]),
                dataBase.rubelDatabaseDao.get(fourCurrency!![2]),
                dataBase.rubelDatabaseDao.get(fourCurrency!![3]))
                list
            }
        }

        private suspend fun insert(rate: Rate) {
            withContext(Dispatchers.IO) {
                if(dataBase.rubelDatabaseDao.get(rate.Cur_ID) == null)
                    dataBase.rubelDatabaseDao.insert(rate)
                else
                    dataBase.rubelDatabaseDao.update(rate)
            }
        }

        private fun getMove(value: Double): String {
            return when {
                value > 0.0 -> "\uf106"
                value < 0.0 -> "\uf107"
                value == 0.0 -> "-"
                else -> ""
            }
        }

        private fun getMoveColor(value: Double): Int {
            return when {
                value > 0.0 -> Color.GREEN
                value < 0.0 -> Color.RED
                value == 0.0 -> Color.WHITE
                else -> 0
            }
        }

        private fun buildButtonPendingIntent(context: Context, key: Int): PendingIntent {
            val intent = Intent(context, RubelWidget::class.java)
            intent.action = SYNCCLICKED
            intent.putExtra("key", key)
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
}

