package com.zapps.html.xml.viewer.file.reader.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.zapps.html.xml.viewer.file.reader.R;
import com.zapps.html.xml.viewer.file.reader.interfaces.InterfaceAdapterMainCard;
import com.zapps.html.xml.viewer.file.reader.models.ModelMainCards;


public class FragmentMainCards extends Fragment {
    private CardView cardView;

    public static Fragment getInstance(ModelMainCards modelMainCards) {
        FragmentMainCards f = new FragmentMainCards();
        Bundle args = new Bundle();
        args.putParcelable("place", modelMainCards);
        f.setArguments(args);
        return f;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_view_card_view, container, false);
        cardView = (CardView) view.findViewById(R.id.cardView);
//        cardView.setMaxCardElevation(cardView.getCardElevation() * InterfaceAdapterMainCard.MAX_ELEVATION_FACTOR);
        TextView title = (TextView) view.findViewById(R.id.itemCards_tv);
        ModelMainCards modelMainCards = null;
        if (getArguments() != null) {
            modelMainCards = getArguments().getParcelable("place");
            title.setText(modelMainCards.getName());
        }
        return view;
    }

    public CardView getCardView() {
        return cardView;
    }
}