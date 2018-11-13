package darkpoint.rubel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    SwipeRefreshLayout swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        tabLayout = (TabLayout) findViewById(R.id.tab_bar);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        swipe.setDistanceToTriggerSync(100);

        LinearLayout llm = (LinearLayout) tabLayout.getTabAt(0).view;
        TextView title = (TextView) llm.getChildAt(1);
        title.setTextSize(8);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llm.getLayoutParams();
        layoutParams.weight = 3.5f; // e.g. 0.5f
        llm.setGravity(Gravity.CENTER_VERTICAL);
        llm.setLayoutParams(layoutParams);
        tabLayout.getChildAt(0).setEnabled(false);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.colorSelectedTab),
                ContextCompat.getColor(this, R.color.colorSelectedTab));
        for(int i = 1; i < tabLayout.getTabCount(); i++)
        {
            LinearLayout ll = (LinearLayout) tabLayout.getTabAt(i).view;
            ll.getChildAt(1).setVisibility(View.GONE);
            ll.setGravity(Gravity.CENTER_VERTICAL);
        }
        checkAndUpdate();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkAndUpdate();
                swipe.setRefreshing(false);
                TabLayout.Tab tab = tabLayout.getTabAt(0);
                tab.select();
            }
        });
    }

    void updateData()
    {
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), this));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(4);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                LinearLayout ll = (LinearLayout) tab.view;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
                layoutParams.weight = 3.5f; // e.g. 0.5f
                layoutParams.gravity = Gravity.START;
                ll.setGravity(Gravity.CENTER_VERTICAL);
                ll.getChildAt(1).setVisibility(View.VISIBLE);
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorSelectedTab), PorterDuff.Mode.SRC_IN);
                ll.setLayoutParams(layoutParams);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LinearLayout ll = (LinearLayout) tab.view;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
                layoutParams.weight = 1; // e.g. 0.5f
                ll.getChildAt(1).setVisibility(View.GONE);
                tab.getIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorUnSelectedTab), PorterDuff.Mode.SRC_IN);
                ll.setLayoutParams(layoutParams);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.setVisibility(View.VISIBLE);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                enableDisableSwipeRefresh( state == ViewPager.SCROLL_STATE_IDLE );
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                viewPager.clearFocus();
                if(viewPager.getCurrentItem() == 1)
                {
                    swipe.setRefreshing(false);
                    swipe.setEnabled(false);
                }
            }
        });
    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    View.OnClickListener snackBarClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            checkAndUpdate();
        }
    };

    void checkAndUpdate()
    {
        if(hasConnection(getApplicationContext())) {
            updateData();
            tabLayout.getChildAt(0).setEnabled(true);
        }
        else {
            viewPager.setVisibility(View.GONE);
            tabLayout.getChildAt(0).setEnabled(false);
            Snackbar.make(tabLayout, "Нет подключения к интернету", Snackbar.LENGTH_LONG).setAction("Повторить", snackBarClick).show();
        }
    }

    private void enableDisableSwipeRefresh(boolean enable) {
        if (swipe != null) {
            swipe.setEnabled(enable);
        }
    }
}
