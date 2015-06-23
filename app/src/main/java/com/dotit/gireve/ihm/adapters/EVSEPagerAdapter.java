package com.dotit.gireve.ihm.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.dotit.gireve.entity.EVSEItem;
import com.dotit.gireve.ihm.StationDetailsFragment;

import java.util.ArrayList;

/**
 * @author H.L - admin
 *
 */
public class EVSEPagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<EVSEItem> items;

	public EVSEPagerAdapter(FragmentManager fm) {
		super(fm);

	}

	public EVSEPagerAdapter(FragmentManager fm, ArrayList<EVSEItem> items) {
		super(fm);
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Fragment getItem(int position) { 
		return StationDetailsFragment.newInstance(items.get(position));
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);

	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE; 
	}	


}
