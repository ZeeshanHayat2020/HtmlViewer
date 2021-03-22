package com.zapps.html.xml.viewer.file.reader.adapters;

import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.zapps.html.xml.viewer.file.reader.interfaces.InterfaceAdapterMainCard;
import com.zapps.html.xml.viewer.file.reader.models.ModelMainCards;
import com.zapps.html.xml.viewer.file.reader.views.FragmentMainCards;

import java.util.ArrayList;
import java.util.List;

public class AdapterMainCardMainCardsPager extends FragmentStatePagerAdapter implements InterfaceAdapterMainCard {
    private List<FragmentMainCards> fragments;
    private List<ModelMainCards> modelMainCardsList;

    public AdapterMainCardMainCardsPager(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        modelMainCardsList = new ArrayList<>();
    }


    @Override
    public CardView getCardViewAt(int position) {
        return fragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentMainCards.getInstance(modelMainCardsList.get(position));
    }

    public void addCardFragment(FragmentMainCards fragment, ModelMainCards modelMainCards, int position) {
        fragments.add(fragment);
        modelMainCardsList.add(modelMainCards);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (FragmentMainCards) fragment);
        return fragment;
    }


}