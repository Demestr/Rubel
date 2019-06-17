package darkpoint.rubel.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import darkpoint.rubel.R
import darkpoint.rubel.activities.MainViewModel
import darkpoint.rubel.adapters.RateAdapter
import darkpoint.rubel.databinding.FragmentAllCurrenciesBinding

class AllCurrenciesFragment : Fragment() {

    private lateinit var binding: FragmentAllCurrenciesBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_currencies, container, false)

        binding.lifecycleOwner = this

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
            binding.rateViewModel = viewModel
            //val currencyAdapter = CurrencyAdapter(viewModel.listCurrencies.value)
            binding.currenciesList.adapter = RateAdapter(RateAdapter.OnClickListener { rate ->
                val bundle = bundleOf("rateForDetail" to rate, "colorRate" to R.color.colorSelected)
                findNavController().navigate(R.id.action_global_detailFragment, bundle)
            })
        }

        viewModel.countCurrencies.observe(this, androidx.lifecycle.Observer {
            if (it == 4){
                binding.warningMessageAll.visibility = View.GONE
                binding.currenciesList.visibility = View.VISIBLE
            }
            else{
                binding.warningMessageAll.visibility = View.VISIBLE
                binding.currenciesList.visibility = View.GONE
            }
        })
        
        return binding.root
    }
}
