package com.dotit.gireve.ihm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dotit.gireve.R;
import com.dotit.gireve.entity.EVSEItem;
import com.dotit.gireve.ihm.adapters.EVSEPagerAdapter;
import com.dotit.gireve.ihm.viewpager.CirclePageIndicator;
import com.dotit.gireve.ihm.viewpager.PageIndicator;
import com.dotit.gireve.utils.GireveFonts;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author H.L - admin
 *
 */
public class EVSEPagerFragment extends Fragment {

    private static final String EVSE_ITEMS_TAG = "evse_items";
    private static final String EVSE_FROM_LIST_TAG = "from_list";

    public EVSEPagerAdapter mAdapter;
	public ViewPager mPager;
	public PageIndicator mIndicator;
    private TextView txv_title;
    private ImageButton btn_back;

    private boolean isFromList = false;
    private LinkedList<EVSEItem> items;

    public static EVSEPagerFragment newInstance(LinkedList<EVSEItem> items, boolean isFromList){

        EVSEPagerFragment fragmentInstance = new EVSEPagerFragment();

        Bundle argument = new Bundle();
        argument.putSerializable(EVSE_ITEMS_TAG, items);
        argument.putBoolean(EVSE_FROM_LIST_TAG, isFromList);
        fragmentInstance.setArguments(argument);

        return fragmentInstance;
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.pager_layout, container, false);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txv_title       = (TextView) rootView.findViewById(R.id.txv_title);
        txv_title	    .setTypeface(GireveFonts.getClanProBold());

        btn_back        = (ImageButton) rootView.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

		mPager = (ViewPager)rootView.findViewById(R.id.view_pager);
		mIndicator = (CirclePageIndicator)rootView.findViewById(R.id.indicator);

		return rootView;
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isFromList = getArguments().getBoolean(EVSE_FROM_LIST_TAG);
        items = (LinkedList<EVSEItem>)getArguments().getSerializable(EVSE_ITEMS_TAG);


        if(isFromList)
            btn_back.setImageResource(R.drawable.back_list_icon);
        else
            btn_back.setImageResource(R.drawable.back_map_icon);


        mAdapter = new EVSEPagerAdapter(getActivity().getSupportFragmentManager(), new ArrayList<>(items));
        mPager.setAdapter(mAdapter);
        mPager.setSaveEnabled(false);
        mIndicator.setViewPager(mPager);

    }

    public void  cleanViewOnDestroy(){

		mPager.setAdapter(null);
		mPager = null;
		mIndicator.setOnPageChangeListener(null);
		mIndicator = null;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		cleanViewOnDestroy();

	}

}
