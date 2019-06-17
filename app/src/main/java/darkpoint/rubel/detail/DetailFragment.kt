package darkpoint.rubel.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import darkpoint.rubel.R
import darkpoint.rubel.activities.LoadingStatus
import darkpoint.rubel.customViews.CustomMarkerView
import darkpoint.rubel.databinding.FragmentDetailBinding
import darkpoint.rubel.model.RateShort
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var args: DetailFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        args = DetailFragmentArgs.fromBundle(arguments!!)
        binding.lifecycleOwner = this
        val viewModelFactory = DetailViewModelFactory(args.rateForDetail.Cur_ID)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.rateDetailVM = viewModel

        prepareViews()

        viewModel.status.observe(this, androidx.lifecycle.Observer {
            when(it){
                LoadingStatus.DONE -> {
                    binding.progressBarDetail.visibility = View.GONE
                    binding.containerDetail.visibility = View.VISIBLE
                }
                else -> {
                    binding.progressBarDetail.visibility = View.VISIBLE
                    binding.containerDetail.visibility = View.GONE
                }
            }
        })

        viewModel.currency.observe(this, androidx.lifecycle.Observer {
            binding.toolbarDetail.title = it.Cur_Name
        })

        viewModel.listPeriodRates.observe(this, androidx.lifecycle.Observer {
            setDataSet(it)
        })

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
             when (it.itemId) {
                R.id.year -> {
                    viewModel.getPeriodRates(DetailViewModel.Periods.YEAR)
                }
                R.id.month -> {
                    viewModel.getPeriodRates(DetailViewModel.Periods.MONTH)
                }
                R.id.week -> {
                    viewModel.getPeriodRates(DetailViewModel.Periods.WEEK)
                }
            }
            it.isChecked = true
            false
        }
        return binding.root
    }

    private fun setDataSet(rateShorts: List<RateShort>){

        var duration = 2000
        val color = ContextCompat.getColor(context!!, args.colorRate)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val entries = ArrayList<Entry>()
        for (rate in rateShorts) {
            val date = dateFormat.parse(rate.Date)
            val dateLong = date.time / 1000
            entries.add(Entry(dateLong.toFloat(), rate.Cur_OfficialRate.toFloat()))
        }
        val dateParsed = dateFormat.parse(rateShorts.last().Date)
        val date = dateFormat.format(dateParsed)
        val dateNow = dateFormat.format(Calendar.getInstance().time)
        if (date != dateNow)
            entries.remove(entries.last())
        when{
            entries.size > 100 -> duration = 2000
            entries.size in 11..99 -> duration = 1000
            entries.size < 11 -> duration = 500
        }

        val dataSet = LineDataSet(entries, "Label")
        dataSet.color = color
        dataSet.setDrawCircles(false)
        dataSet.setDrawFilled(true)
        dataSet.fillColor = color
        dataSet.highLightColor = color
        dataSet.enableDashedHighlightLine(15f, 5f, 5f)
        val lineData = LineData(dataSet)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.label = viewModel.currency.value!!.Cur_QuotName
        lineData.setDrawValues(false)
        binding.detailChart.data = lineData
        binding.detailChart.animateX(duration)
    }

    private fun prepareViews(){
        binding.toolbarDetail.setTitleTextColor(Color.WHITE)
        binding.toolbarDetail.setNavigationIcon(R.drawable.ic_tollbar_back)
        binding.toolbarDetail.setNavigationOnClickListener {
            activity!!.onBackPressed()
        }
        binding.bottomNavigationView.menu.getItem(1).isChecked = true
        binding.detailChart.highlightValue(null)
        binding.detailChart.setNoDataText("")
        binding.detailChart.visibility = View.VISIBLE
        binding.progressBarDetail.visibility = View.GONE
        binding.detailChart.xAxis.setDrawGridLines(false)
        binding.detailChart.xAxis.setDrawAxisLine(false)
        binding.detailChart.axisLeft.setDrawGridLines(false)
        binding.detailChart.axisRight.setDrawGridLines(false)
        binding.detailChart.setScaleEnabled(false)
        binding.detailChart.axisRight.textColor = Color.WHITE
        binding.detailChart.description.isEnabled = false
        binding.detailChart.axisLeft.textColor = Color.WHITE
        binding.detailChart.xAxis.textColor = Color.WHITE
        binding.detailChart.legend.textColor = Color.WHITE

        val markerView = CustomMarkerView(context!!, R.layout.chart_item)
        binding.detailChart.marker = markerView
        val x = binding.detailChart.xAxis
        x.setLabelCount(4, true)
        x.valueFormatter = MyXAxisValueFormatter()
        binding.detailChart.setPinchZoom(true)
        binding.detailChart.invalidate()
    }

    inner class MyXAxisValueFormatter// format values to 1 decimal digit
        : IAxisValueFormatter {

        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            // "value" represents the position of the label on the axis (x or y)
            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val valueD = value.toDouble() * 1000
            return simpleDateFormat.format(valueD)
        }

    }
}
