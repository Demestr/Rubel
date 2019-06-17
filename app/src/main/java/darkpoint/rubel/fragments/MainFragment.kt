package darkpoint.rubel.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import darkpoint.rubel.R
import darkpoint.rubel.Setting
import darkpoint.rubel.activities.LoadingStatus
import darkpoint.rubel.activities.MainViewModel
import darkpoint.rubel.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private var fourCurrency: IntArray? = null
    private lateinit var viewModel: MainViewModel
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val curDate = dateFormat.format(calendar.time)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        fourCurrency = Setting.loadSetting(this.activity!!)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        MobileAds.initialize(context, "ca-app-pub-5942497315583191~8880324859")
        val mAdRequest = AdRequest.Builder().build()

        binding.lifecycleOwner = this

        binding.bottomAdBanner.loadAd(mAdRequest)

        binding.bottomAdBanner.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                binding.bottomAdBanner.visibility = View.VISIBLE
            }
        }

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
            binding.mainViewModel = viewModel
        }

        viewModel.status.observe(this, androidx.lifecycle.Observer {
            when(it){
                LoadingStatus.DONE -> {
                    binding.mainLayout.visibility = View.VISIBLE
                }
                LoadingStatus.ERROR -> {
                    binding.mainLayout.visibility = View.GONE
                }
                else -> {
                    binding.mainLayout.visibility = View.GONE
                }
            }
        })

        binding.currencyOne.setOnClickListener {
            val bundle = bundleOf("rateForDetail" to viewModel.currencyOne.value, "colorRate" to R.color.colorCurrencyOne)
            findNavController().navigate(R.id.action_global_detailFragment, bundle)
        }

        binding.currencyTwo.setOnClickListener {
            val bundle = bundleOf("rateForDetail" to viewModel.currencyTwo.value, "colorRate" to R.color.colorCurrencyTwo)
            findNavController().navigate(R.id.action_global_detailFragment, bundle)
        }

        binding.currencyThree.setOnClickListener {
            val bundle = bundleOf("rateForDetail" to viewModel.currencyThree.value, "colorRate" to R.color.colorCurrencyThree)
            findNavController().navigate(R.id.action_global_detailFragment, bundle)
        }

        binding.currencyFour.setOnClickListener {
            val bundle = bundleOf("rateForDetail" to viewModel.currencyFour.value, "colorRate" to R.color.colorCurrencyFour)
            findNavController().navigate(R.id.action_global_detailFragment, bundle)
        }

        viewModel.dateRequest.observe(this, androidx.lifecycle.Observer {
            binding.datePicker.text = DateFormat.getDateInstance(DateFormat.LONG).format(it)
            if (curDate == dateFormat.format(it))
                fab_forwardDate.hide()
            else
                fab_forwardDate.show()
        })

        viewModel.countCurrencies.observe(this, androidx.lifecycle.Observer {
            if (it == 4){
                binding.warningMessage.visibility = View.GONE
                binding.mainLayout.visibility = View.VISIBLE
            }
            else{
                binding.warningMessage.visibility = View.VISIBLE
                binding.mainLayout.visibility = View.GONE
            }
        })

        val d = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.changeDate(calendar.time)
        }

        binding.datePicker.setOnClickListener {
            val pickerDialog = DatePickerDialog(context!!, d,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
            pickerDialog.datePicker.maxDate = Calendar.getInstance().time.time
            pickerDialog.show()
        }

        binding.fabBackDate.setOnClickListener {
            calendar.add(Calendar.DATE, -1)
            viewModel.changeDate(calendar.time)
        }

        binding.fabForwardDate.setOnClickListener {
            calendar.add(Calendar.DATE, 1)
            viewModel.changeDate(calendar.time)
        }

        return binding.root
    }


}
