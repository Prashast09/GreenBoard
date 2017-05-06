package bean.red.greenboard.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivam Seth on 4/28/2016.
 */
public class IndexTabAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();
    public IndexTabAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
    public void addFragment(Fragment fragment,String title){
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }
}

