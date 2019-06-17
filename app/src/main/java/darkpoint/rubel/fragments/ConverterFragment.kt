package darkpoint.rubel.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import darkpoint.rubel.R
import darkpoint.rubel.activities.CurrenciesIndex
import darkpoint.rubel.activities.MainViewModel
import darkpoint.rubel.databinding.FragmentConverterBinding
import java.text.DateFormat

class ConverterFragment : Fragment() {

    private lateinit var binding: FragmentConverterBinding
    private lateinit var viewModel: MainViewModel
    private var watcherByn: ConverterTextWatcher? = null
    private var watcher1: ConverterTextWatcher? = null
    private var watcher2: ConverterTextWatcher? = null
    private var watcher3: ConverterTextWatcher? = null
    private var watcher4: ConverterTextWatcher? = null
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_converter, container, false)
        binding.lifecycleOwner = this
        activity?.let { activity ->
            viewModel = ViewModelProviders.of(activity).get(MainViewModel::class.java)
        }
        binding.rateViewModel = viewModel

        watcherByn = ConverterTextWatcher(CurrenciesIndex.BYN, binding.editTextByn)
        watcher1 = ConverterTextWatcher(CurrenciesIndex.ONE, binding.editText)
        watcher2 = ConverterTextWatcher(CurrenciesIndex.TWO, binding.editTextTwo)
        watcher3 = ConverterTextWatcher(CurrenciesIndex.THREE, binding.editTextThree)
        watcher4 = ConverterTextWatcher(CurrenciesIndex.FOUR, binding.editTextFour)

        binding.editTextByn.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus)
                binding.editTextByn.addTextChangedListener(watcherByn)
            else
                binding.editTextByn.removeTextChangedListener(watcherByn)
        }

        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus)
                binding.editText.addTextChangedListener(watcher1)
            else
                binding.editText.removeTextChangedListener(watcher1)
        }

        binding.editTextTwo.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus)
                binding.editTextTwo.addTextChangedListener(watcher2)
            else
                binding.editTextTwo.removeTextChangedListener(watcher2)
        }

        binding.editTextThree.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus)
                binding.editTextThree.addTextChangedListener(watcher3)
            else
                binding.editTextThree.removeTextChangedListener(watcher3)
        }

        binding.editTextFour.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus)
                binding.editTextFour.addTextChangedListener(watcher4)
            else
                binding.editTextFour.removeTextChangedListener(watcher4)
        }

        viewModel.dateRequest.observe(this, androidx.lifecycle.Observer {
            binding.conversionDate.text = getString(R.string.converterDate, DateFormat.getDateInstance(DateFormat.LONG).format(it))
        })

        viewModel.countCurrencies.observe(this, androidx.lifecycle.Observer {
            if (it == 4){
                binding.warningMessageConv.visibility = View.GONE
                binding.convView.visibility = View.VISIBLE
            }
            else{
                binding.warningMessageConv.visibility = View.VISIBLE
                binding.convView.visibility = View.GONE
            }
        })

        binding.editTextByn.isLongClickable = false
        binding.editText.isLongClickable = false
        binding.editTextTwo.isLongClickable = false
        binding.editTextThree.isLongClickable = false
        binding.editTextFour.isLongClickable = false

        return binding.root
    }

    private inner class ConverterTextWatcher(val currenciesIndex: CurrenciesIndex, val tiet: TextInputEditText) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString().matches("^[.0]".toRegex())) {
                tiet.text = Editable.Factory.getInstance().newEditable("")
                return
            }
            val input: Double
            if (s.toString().isNotEmpty()) {
                input = s.toString().toDouble()
                viewModel.setConvertValue(currenciesIndex, input, false)
            }
            else
                viewModel.setConvertValue(currenciesIndex, 0.0, false)
        }
    }
}
