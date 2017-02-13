package com.ngw.seed.view.cards;

import android.view.View;
import android.widget.EditText;

import com.ngw.seed.R;

public class CreateThoughtCard extends AbstractCard {
    @Override
    protected int getLayout() {
        return R.layout.card_view_create_thought;
    }

    @Override
    public int getViewTypeAsInt() {
        return ViewType.EDIT_TEXT.getIntValue();
    }

    @Override
    public Type getType() {
        return Type.CREATION;
    }

    EditText editText;

    @Override
    public void setUpView(View view) {
        editText = (EditText) view.findViewById(R.id.editText);
    }

    public String getText() {
        return editText.getText().toString();
    }
}
