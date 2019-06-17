package darkpoint.rubel.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import darkpoint.rubel.activities.LoadingStatus
import darkpoint.rubel.model.Currency
import darkpoint.rubel.model.DetailRate
import darkpoint.rubel.model.RateShort
import darkpoint.rubel.network.APIFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DetailViewModel(val curId: Int) : ViewModel() {

    enum class Periods {WEEK, MONTH, YEAR}

    private val _status = MutableLiveData<LoadingStatus>()
    val status: LiveData<LoadingStatus>
        get() = _status

    private val _listPeriodRates = MutableLiveData<List<RateShort>>()
    val listPeriodRates: LiveData<List<RateShort>>
        get() = _listPeriodRates

    private val _currency = MutableLiveData<Currency>()
    val currency: LiveData<Currency>
        get() = _currency

    private val _rateYesterday = MutableLiveData<DetailRate>()
    val rateYesterday: LiveData<DetailRate>
        get() = _rateYesterday

    private val _rateToday = MutableLiveData<DetailRate>()
    val rateToday: LiveData<DetailRate>
        get() = _rateToday

    private val _rateTomorrow = MutableLiveData<DetailRate>()
    val rateTomorrow: LiveData<DetailRate>
        get() = _rateTomorrow

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _status.value = LoadingStatus.LOADING
        getCurrency()
        getPeriodRates(Periods.MONTH)
    }

    fun getPeriodRates(period: Periods){
        getAllRates(getDateStartString(period), getEndDateString())
    }

    private fun getAllRates(startDate: String, endDate: String) {
        coroutineScope.launch {
            val listRates = APIFactory.createApi().getRatePeriodAsync(curId, startDate, endDate)
            try {
                _listPeriodRates.value = listRates.await()
                if(checkDate(_listPeriodRates.value!!.last().Date)){
                    _rateYesterday.value = DetailRate(_listPeriodRates.value!![_listPeriodRates.value!!.size-3], _listPeriodRates.value!![_listPeriodRates.value!!.size-2])
                    _rateToday.value = DetailRate(_listPeriodRates.value!![_listPeriodRates.value!!.size-2], _listPeriodRates.value!!.last())
                    _rateTomorrow.value = DetailRate()
                }
                else{
                    _rateYesterday.value = DetailRate(_listPeriodRates.value!![_listPeriodRates.value!!.size-4], _listPeriodRates.value!![_listPeriodRates.value!!.size-3])
                    _rateToday.value = DetailRate(_listPeriodRates.value!![_listPeriodRates.value!!.size-3], _listPeriodRates.value!![_listPeriodRates.value!!.size-2])
                    _rateTomorrow.value = DetailRate(_listPeriodRates.value!![_listPeriodRates.value!!.size-2], _listPeriodRates.value!!.last())
                }
                _status.value = LoadingStatus.DONE
            } catch (e: Exception) {
                _status.value = LoadingStatus.ERROR
                _listPeriodRates.value = ArrayList()
            }
        }
    }

    private fun getCurrency() {
        coroutineScope.launch {
            val cur = APIFactory.createApi().getCurrencyAsync(curId)
            try {
                _currency.value = cur.await()
            } catch (e: Exception) {
                _currency.value = null
            }
        }
    }

    private fun getDateStartString(period: Periods) : String{
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = getEndDate()

        return when (period){
            Periods.YEAR ->{
                calendar.add(Calendar.DATE, -364)
                val startDateString = sdf.format(calendar.time)
                startDateString
            }
            Periods.MONTH ->{
                calendar.add(Calendar.MONTH, -1)
                val startDateString = sdf.format(calendar.time)
                startDateString
            }
            Periods.WEEK ->{
                calendar.add(Calendar.DATE, -7)
                val startDateString = sdf.format(calendar.time)
                startDateString
            }
        }
    }

    private fun getEndDate() : Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        return calendar
    }
    private fun getEndDateString() : String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(getEndDate().time)
    }

    private fun checkDate(date: String): Boolean{
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateNow = sdf.format(Calendar.getInstance().time)
        val dateFromAnswer = sdf.format(sdf.parse(date))
        return dateNow == dateFromAnswer
    }
}