package com.zapps.html.xml.viewer.file.reader.interfaces;

import androidx.cardview.widget.CardView;

public interface InterfaceAdapterMainCard {
    int MAX_ELEVATION_FACTOR = 6;
    float BASE_ELEVATION = 2;
    CardView getCardViewAt(int position);

    int getCount();
}