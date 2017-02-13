package com.ngw.seed.view.cards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface Card {
    View getNewInflatedView(LayoutInflater inflater, ViewGroup parent);
    int getViewTypeAsInt();
    void setUpView(View view);
    Type getType();

    enum Type {
        THOUGHT, CREATION, OTHER
    }

    enum ViewType {
        TEXT(1), EDIT_TEXT(2);

        private final int intValue;
        ViewType(int intValue) {
            this.intValue = intValue;
        }

        public int getIntValue() {
            return intValue;
        }
    }
}
