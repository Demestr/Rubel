package darkpoint.rubel.fragments


import android.content.Context
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import darkpoint.rubel.R
import darkpoint.rubel.activities.LoadingStatus
import darkpoint.rubel.activities.MainViewModel
import darkpoint.rubel.adapters.MyFragmentPagerAdapter
import darkpoint.rubel.databinding.FragmentContainerBinding


class ContainerFragment : Fragment() {

    private lateinit var binding: FragmentContainerBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_container, container, false)
        binding.lifecycleOwner = this

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
        }

        viewModel.status.observe(this, Observer {
            when(it){
                LoadingStatus.DONE -> {
                    binding.progressBarContainer.visibility = View.GONE
                    binding.viewPager.visibility = View.VISIBLE
                }
                LoadingStatus.ERROR -> {
                    binding.progressBarContainer.visibility = View.GONE
                    binding.viewPager.visibility = View.GONE
                }
                else -> {
                    binding.progressBarContainer.visibility = View.VISIBLE
                    binding.viewPager.visibility = View.GONE
                }
            }
        })

        val llm = binding.tabBar.getTabAt(0)!!.view as LinearLayout
        val title = llm.getChildAt(1) as TextView
        val icon = llm.getChildAt(0) as ImageView
        icon.setColorFilter(R.color.colorSelectedTab, PorterDuff.Mode.DST)
        title.textSize = 8f
        val layoutParams = llm.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 3.5f // e.g. 0.5f
        llm.gravity = Gravity.CENTER_VERTICAL
        llm.layoutParams = layoutParams
        binding.tabBar.getChildAt(0).isEnabled = false
        binding.tabBar.setTabTextColors(ContextCompat.getColor(context!!, R.color.colorSelectedTab),
                ContextCompat.getColor(context!!, R.color.colorSelectedTab))
        for (i in 1 until binding.tabBar.tabCount) {
            val ll = binding.tabBar.getTabAt(i)!!.view as LinearLayout
            ll.getChildAt(1).visibility = View.GONE
            ll.gravity = Gravity.CENTER_VERTICAL
        }

        binding.buttonRepeat.setOnClickListener {
            viewModel.initialize()
            checkAndUpdate()
        }
        checkAndUpdate()
        return binding.root
    }

    private fun updateData() {
        binding.viewPager.adapter = MyFragmentPagerAdapter(childFragmentManager)
        binding.tabBar.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager))
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabBar))
        binding.viewPager.offscreenPageLimit = 4
        binding.tabBar.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                val ll = tab.view as LinearLayout
                val layoutParams = ll.layoutParams as LinearLayout.LayoutParams
                layoutParams.weight = 3.5f // e.g. 0.5f
                layoutParams.gravity = Gravity.START
                ll.gravity = Gravity.CENTER_VERTICAL
                ll.getChildAt(1).visibility = View.VISIBLE
                tab.icon!!.setColorFilter(ContextCompat.getColor(context!!, R.color.colorSelectedTab), PorterDuff.Mode.SRC_IN)
                ll.layoutParams = layoutParams
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val ll = tab.view as LinearLayout
                val layoutParams = ll.layoutParams as LinearLayout.LayoutParams
                layoutParams.weight = 1f // e.g. 0.5f
                ll.getChildAt(1).visibility = View.GONE
                tab.icon!!.setColorFilter(ContextCompat.getColor(context!!, R.color.colorUnSelectedTab), PorterDuff.Mode.SRC_IN)
                ll.layoutParams = layoutParams
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        binding.viewPager.visibility = View.VISIBLE
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {
                val imm = getSystemService(context!!, InputMethodManager::class.java)
                imm!!.hideSoftInputFromWindow(binding.viewPager.windowToken, 0)
                binding.viewPager.clearFocus()
            }
        })
    }

    private fun checkAndUpdate() {
        if (hasConnection(context!!)) {
            binding.buttonRepeat.visibility = View.GONE
            updateData()
            binding.tabBar.getChildAt(0).isEnabled = true
        } else {
            binding.viewPager.visibility = View.GONE
            binding.progressBarContainer.visibility = View.GONE
            binding.buttonRepeat.visibility = View.VISIBLE
            binding.tabBar.getChildAt(0).isEnabled = false
            Toast.makeText(context, getString(R.string.network_error), Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {

        fun hasConnection(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifiInfo: NetworkInfo? = cm.activeNetworkInfo
            if (wifiInfo != null) {
                return true
            }
            return false
        }
    }

}
