package ku00015.shoutapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Klaidas on 27/11/2017.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    int count;

    public TabPagerAdapter(FragmentManager fm, int count) {
        super(fm);
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                MyPosts myPosts = new MyPosts();
                return myPosts;
            case 1:
                MyComments myComments = new MyComments();
                return myComments;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
