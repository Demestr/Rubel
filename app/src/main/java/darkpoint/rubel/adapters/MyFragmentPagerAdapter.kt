package darkpoint.rubel.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import darkpoint.rubel.fragments.AllCurrenciesFragment
import darkpoint.rubel.fragments.ConverterFragment
import darkpoint.rubel.fragments.MainFragment
import darkpoint.rubel.fragments.SettingsFragment

class MyFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> MainFragment()
            1 -> ConverterFragment()
            2 -> AllCurrenciesFragment()
            3 -> SettingsFragment()
            else -> MainFragment()
        }

    }

    override fun getCount(): Int {
        return 4
    }
}
