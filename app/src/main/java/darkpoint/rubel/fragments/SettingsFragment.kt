package darkpoint.rubel.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import darkpoint.rubel.R
import darkpoint.rubel.Setting
import darkpoint.rubel.activities.CurrenciesIndex
import darkpoint.rubel.activities.MainViewModel
import darkpoint.rubel.customViews.CustomChip
import darkpoint.rubel.databinding.FragmentSettingsBinding
import darkpoint.rubel.model.Rate
import java.util.*

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var fourCurrency: IntArray
    private val stack = Stack<CurrenciesIndex>()
    private val stackColors = Stack<ColorStateList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fourCurrency = Setting.loadSetting(this.context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        activity?.let { activity ->
            viewModel = ViewModelProviders.of(activity).get(MainViewModel::class.java)
        }
        binding.rateViewModel = viewModel
        viewModel.listCurrencies.observe(this, Observer {
            for (rate in it) {
                binding.currenciesGroup.addView(createChip(rate))
            }
        })

        viewModel.countCurrencies.observe(this, Observer {
            if (it == 4) {
                val ids = intArrayOf(viewModel.currencyOne.value!!.Cur_ID,
                        viewModel.currencyTwo.value!!.Cur_ID,
                        viewModel.currencyThree.value!!.Cur_ID,
                        viewModel.currencyFour.value!!.Cur_ID)
                Setting.saveSetting(context!!, ids)
                fourCurrency = ids
            }
        })

        return binding.root
    }

    private fun createChip(rate: Rate): Chip {
        val chip = CustomChip(context!!)
        chip.text = rate.Cur_Abbreviation
        chip.setChipBackgroundColorResource(R.color.colorPrimaryDark)
        chip.isCheckable = true
        chip.selectedID = rate.Cur_ID
        chip.rate = rate

        val id = fourCurrency.find { it == rate.Cur_ID }
        id?.let {
            val index = fourCurrency.indexOf(it)
            when (index) {
                0 -> {
                    chip.setChipBackgroundColorResource(R.color.colorCurrencyOne); chip.curIndex = CurrenciesIndex.ONE; chip.isChecked = true
                }
                1 -> {
                    chip.setChipBackgroundColorResource(R.color.colorCurrencyTwo); chip.curIndex = CurrenciesIndex.TWO; chip.isChecked = true
                }
                2 -> {
                    chip.setChipBackgroundColorResource(R.color.colorCurrencyThree); chip.curIndex = CurrenciesIndex.THREE; chip.isChecked = true
                }
                3 -> {
                    chip.setChipBackgroundColorResource(R.color.colorCurrencyFour); chip.curIndex = CurrenciesIndex.FOUR; chip.isChecked = true
                }
                else -> chip.setChipBackgroundColorResource(R.color.colorPrimaryDark)
            }
        }
        chip.setTextColor(Color.WHITE)
        chip.setOnCheckedChangeListener(chipListener)
        return chip
    }

    private val chipListener = CompoundButton.OnCheckedChangeListener { thisChip, isChecked ->
        val chip = thisChip as CustomChip
        if (stack.empty()) {
            if (!isChecked) {
                stack.push(chip.curIndex)
                stackColors.push(chip.chipBackgroundColor)
                viewModel.setRateNull(chip.curIndex)
                chip.setChipBackgroundColorResource(R.color.colorPrimaryDark)
            } else {
                thisChip.isChecked = false
            }
        } else {
            if (!isChecked) {
                stack.push(chip.curIndex)
                stackColors.push(chip.chipBackgroundColor)
                viewModel.setRateNull(chip.curIndex)
                chip.setChipBackgroundColorResource(R.color.colorPrimaryDark)
            } else {
                chip.curIndex = stack.pop()
                chip.chipBackgroundColor = stackColors.pop()
                viewModel.setNewRate(chip.curIndex, chip.rate)
                viewModel.updateConverter()
            }
        }
    }
}
