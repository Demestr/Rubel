package darkpoint.rubel;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import darkpoint.rubel.Fragments.AllCurrenciesFragment;
import darkpoint.rubel.Fragments.ConverterFragment;
import darkpoint.rubel.Fragments.MainFragment;
import darkpoint.rubel.Fragments.SettingsFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MainFragment.newInstance(position + 1);
            case 1:
                return ConverterFragment.newInstance(position + 1);
            case 2:
                return AllCurrenciesFragment.newInstance(position + 1);
            case 3:
                return SettingsFragment.newInstance(position + 1);
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }
}
