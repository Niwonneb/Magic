package com.ngw.mindbase.view.cards;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ngw.mindbase.R;
import com.ngw.mindbase.model.Thought;

public class CreateThoughtCard extends AbstractCard {
    private Thought previousThought;
    private int color;

    public CreateThoughtCard(Thought previosThought, int color) {
        this.previousThought = previosThought;
        this.color = color;
    }

    public Thought getPreviousThought() {
        return previousThought;
    }

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

    private EditText editText;

    @Override
    public void setUpView(View view) {
        TextView prompt = (TextView) view.findViewById(R.id.prompt);

        if (previousThought.isInitialQuestion()) {
            prompt.setVisibility(View.INVISIBLE);
        } else {
            prompt.setVisibility(View.VISIBLE);
        }

        TextView previousTextView = (TextView) view.findViewById(R.id.previousThought);
        previousTextView.setText(previousThought.getText());
        editText = (EditText) view.findViewById(R.id.editText);

        setColor(color);
    }

    public void setColor(int color) {
        editText.setBackgroundColor(color);
        if (isDarkColor(color)) {
            editText.setTextColor(Color.WHITE);
        } else {
            editText.setTextColor(Color.BLACK);
        }
    }

    private static boolean isDarkColor(int color){
    double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
    if(darkness<0.5){
        return false;
    }else{
        return true;
    }
}

    public String getText() {
        return editText.getText().toString();
    }
}
