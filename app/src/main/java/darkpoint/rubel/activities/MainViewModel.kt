package darkpoint.rubel.activities

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import darkpoint.rubel.Setting
import darkpoint.rubel.model.Rate
import darkpoint.rubel.network.APIFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class CurrenciesIndex { BYN, ONE, TWO, THREE, FOUR }
enum class LoadingStatus { LOADING, ERROR, DONE }

class MainViewModel(application: Application) : ViewModel() {

    //region initialVariables

    private val _listCurrencies = MutableLiveData<List<Rate>>()
    val listCurrencies: LiveData<List<Rate>>
        get() = _listCurrencies

    private val _status = MutableLiveData<LoadingStatus>()
    val status: LiveData<LoadingStatus>
        get() = _status

    private val _dateRequest = MutableLiveData<Date>()
    val dateRequest: LiveData<Date>
        get() = _dateRequest

    private val _currencyBYN = MutableLiveData<Rate>()
    val currencyBYN: LiveData<Rate>
        get() = _currencyBYN

    private val _currencyOne = MutableLiveData<Rate>()
    val currencyOne: LiveData<Rate>
        get() = _currencyOne

    private val _currencyTwo = MutableLiveData<Rate>()
    val currencyTwo: LiveData<Rate>
        get() = _currencyTwo

    private val _currencyThree = MutableLiveData<Rate>()
    val currencyThree: LiveData<Rate>
        get() = _currencyThree

    private val _currencyFour = MutableLiveData<Rate>()
    val currencyFour: LiveData<Rate>
        get() = _currencyFour

    private val _curOneVisibility = MutableLiveData<Int>()
    val curOneVisibility: LiveData<Int>
        get() = _curOneVisibility

    private val _curTwoVisibility = MutableLiveData<Int>()
    val curTwoVisibility: LiveData<Int>
        get() = _curTwoVisibility

    private val _curThreeVisibility = MutableLiveData<Int>()
    val curThreeVisibility: LiveData<Int>
        get() = _curThreeVisibility

    private val _curFourVisibility = MutableLiveData<Int>()
    val curFourVisibility: LiveData<Int>
        get() = _curFourVisibility

    private val _curBynConverterValue = MutableLiveData<String>()
    val curBynConverterValue: LiveData<String>
        get() = _curBynConverterValue

    private val _curOneConverterValue = MutableLiveData<String>()
    val curOneConverterValue: LiveData<String>
        get() = _curOneConverterValue

    private val _curTwoConverterValue = MutableLiveData<String>()
    val curTwoConverterValue: LiveData<String>
        get() = _curTwoConverterValue

    private val _curThreeConverterValue = MutableLiveData<String>()
    val curThreeConverterValue: LiveData<String>
        get() = _curThreeConverterValue

    private val _curFourConverterValue = MutableLiveData<String>()
    val curFourConverterValue: LiveData<String>
        get() = _curFourConverterValue

    private val _countCurrencies = MutableLiveData<Int>()
    val countCurrencies: LiveData<Int>
        get() = _countCurrencies

    private val fourCurrency = Setting.loadSetting(application)
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    //endregion

    init {
        initialize()
    }

    fun initialize(){
        _status.value = LoadingStatus.LOADING
        _dateRequest.value = Calendar.getInstance().time
        getRates(sdf.format(_dateRequest.value))
        _currencyBYN.value = Rate(666, "", "BYN", 1, "Белорусский рубль", 1.0)
        _curBynConverterValue.value = ""
        _curOneConverterValue.value = ""
        _curTwoConverterValue.value = ""
        _curThreeConverterValue.value = ""
        _curFourConverterValue.value = ""
    }

    fun setRateNull(currenciesIndex: CurrenciesIndex) {
        when (currenciesIndex) {
            CurrenciesIndex.ONE -> {
                _currencyOne.value = null; _curOneVisibility.value = View.GONE
                _countCurrencies.value = _countCurrencies.value?.minus(1)
            }
            CurrenciesIndex.TWO -> {
                _currencyTwo.value = null; _curTwoVisibility.value = View.GONE
                _countCurrencies.value = _countCurrencies.value?.minus(1)
            }
            CurrenciesIndex.THREE -> {
                _currencyThree.value = null; _curThreeVisibility.value = View.GONE
                _countCurrencies.value = _countCurrencies.value?.minus(1)
            }
            CurrenciesIndex.FOUR -> {
                _currencyFour.value = null; _curFourVisibility.value = View.GONE
                _countCurrencies.value = _countCurrencies.value?.minus(1)
            }
            else -> {
            }
        }
    }

    fun setNewRate(currenciesIndex: CurrenciesIndex, rate: Rate) {
        when (currenciesIndex) {
            CurrenciesIndex.ONE -> {
                _currencyOne.value = rate; _curOneVisibility.value = View.VISIBLE
                _countCurrencies.value = _countCurrencies.value?.plus(1)
            }
            CurrenciesIndex.TWO -> {
                _currencyTwo.value = rate; _curTwoVisibility.value = View.VISIBLE
                _countCurrencies.value = _countCurrencies.value?.plus(1)
            }
            CurrenciesIndex.THREE -> {
                _currencyThree.value = rate; _curThreeVisibility.value = View.VISIBLE
                _countCurrencies.value = _countCurrencies.value?.plus(1)
            }
            CurrenciesIndex.FOUR -> {
                _currencyFour.value = rate; _curFourVisibility.value = View.VISIBLE
                _countCurrencies.value = _countCurrencies.value?.plus(1)
            }
            else -> {
            }
        }
    }

    private fun calculateRate(value: Double, rate: Rate): Double {
        val newRateValue: Double = 1 / rate.Cur_OfficialRate * rate.Cur_Scale
        val rateFinal: Double
        rateFinal = newRateValue * value
        return rateFinal
    }

    fun setConvertValue(currenciesIndex: CurrenciesIndex, value: Double, update: Boolean) {
        when (currenciesIndex) {
            CurrenciesIndex.BYN -> {
                if(update){
                    _curBynConverterValue.value = value.toString()
                    if (value == 0.0)
                        _curBynConverterValue.value = ""
                }
                _curOneConverterValue.value = formatConvValues(value, 1.0, _currencyOne.value!!)
                _curTwoConverterValue.value = formatConvValues(value, 1.0, _currencyTwo.value!!)
                _curThreeConverterValue.value = formatConvValues(value, 1.0, _currencyThree.value!!)
                _curFourConverterValue.value = formatConvValues(value, 1.0, _currencyFour.value!!)
            }
            CurrenciesIndex.ONE -> {
                val oneEntity = 1 / _currencyOne.value!!.Cur_OfficialRate * _currencyOne.value!!.Cur_Scale
                _curBynConverterValue.value = formatConvValues(value, oneEntity, _currencyBYN.value!!)
                _curTwoConverterValue.value = formatConvValues(value, oneEntity, _currencyTwo.value!!)
                _curThreeConverterValue.value = formatConvValues(value, oneEntity, _currencyThree.value!!)
                _curFourConverterValue.value = formatConvValues(value, oneEntity, _currencyFour.value!!)
            }
            CurrenciesIndex.TWO -> {
                val oneEntity = 1 / _currencyTwo.value!!.Cur_OfficialRate * _currencyTwo.value!!.Cur_Scale
                _curOneConverterValue.value = formatConvValues(value, oneEntity, _currencyOne.value!!)
                _curBynConverterValue.value = formatConvValues(value, oneEntity, _currencyBYN.value!!)
                _curThreeConverterValue.value = formatConvValues(value, oneEntity, _currencyThree.value!!)
                _curFourConverterValue.value = formatConvValues(value, oneEntity, _currencyFour.value!!)
            }
            CurrenciesIndex.THREE -> {
                val oneEntity = 1 / _currencyThree.value!!.Cur_OfficialRate * _currencyThree.value!!.Cur_Scale
                _curOneConverterValue.value = formatConvValues(value, oneEntity, _currencyOne.value!!)
                _curTwoConverterValue.value = formatConvValues(value, oneEntity, _currencyTwo.value!!)
                _curBynConverterValue.value = formatConvValues(value, oneEntity, _currencyBYN.value!!)
                _curFourConverterValue.value = formatConvValues(value, oneEntity, _currencyFour.value!!)
            }
            CurrenciesIndex.FOUR -> {
                val oneEntity = 1 / _currencyFour.value!!.Cur_OfficialRate * _currencyFour.value!!.Cur_Scale
                _curBynConverterValue.value = formatConvValues(value, oneEntity, _currencyBYN.value!!)
                _curOneConverterValue.value = formatConvValues(value, oneEntity, _currencyOne.value!!)
                _curTwoConverterValue.value = formatConvValues(value, oneEntity, _currencyTwo.value!!)
                _curThreeConverterValue.value = formatConvValues(value, oneEntity, _currencyThree.value!!)
            }
        }
    }

    fun changeDate(date: Date) {
        _dateRequest.value = date
        getRates(sdf.format(date))
        updateConverter()
    }

    fun updateConverter() {
        if (!_curBynConverterValue.value!!.isEmpty())
            setConvertValue(CurrenciesIndex.BYN, _curBynConverterValue.value!!.toDouble(), true)
    }

    private fun formatConvValues(value: Double, oneEntity: Double, rate: Rate): String {
        return if (value != 0.0)
            String.format(Locale.ENGLISH, "%.2f", calculateRate(value / oneEntity, rate))
        else
            ""
    }

    private fun getAllRates() {
        coroutineScope.launch {
            val listRates = APIFactory.createApi().getAllRatesAsync(0)
            try {
                _listCurrencies.value = listRates.await()
            } catch (e: Exception) {
                _status.value = LoadingStatus.ERROR
            }
        }
    }

    private fun getRates(date: String) {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            val getRateOne = APIFactory.createApi().getRateOnDateAsync(fourCurrency[0], date)
            val getRateTwo = APIFactory.createApi().getRateOnDateAsync(fourCurrency[1], date)
            val getRateThree = APIFactory.createApi().getRateOnDateAsync(fourCurrency[2], date)
            val getRateFour = APIFactory.createApi().getRateOnDateAsync(fourCurrency[3], date)
            try {
                _status.value = LoadingStatus.LOADING
                if(_listCurrencies.value == null)
                    getAllRates()
                _currencyOne.value = getRateOne.await()
                _currencyTwo.value = getRateTwo.await()
                _currencyThree.value = getRateThree.await()
                _currencyFour.value = getRateFour.await()
                _countCurrencies.value = 4
                _status.value = LoadingStatus.DONE
            } catch (e: Exception) {
                _status.value = LoadingStatus.ERROR
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}