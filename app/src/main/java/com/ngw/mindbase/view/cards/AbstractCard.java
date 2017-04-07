package com.ngw.mindbase.view.cards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractCard implements Card {

    public View getNewInflatedView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayout(), parent, false);
        setUpView(view);
        return view;
    }

    protected abstract int getLayout();
    public void setUpView(View view) {}
}
