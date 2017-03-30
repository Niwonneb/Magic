package com.ngw.mindtime.view.cards;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ngw.mindtime.R;
import com.ngw.mindtime.model.Thought;

public class CreateThoughtCard extends AbstractCard {
    private boolean isQuestion;
    private Thought previousThought;

    public CreateThoughtCard(Thought.ThoughtType type, Thought previosThought) {
        this.isQuestion = type == Thought.ThoughtType.Question;
        this.previousThought = previosThought;
    }

    public Thought getPreviousThought() {
        return previousThought;
    }

    public Thought.ThoughtType getCreatedThoughtType() {
        return (previousThought.getType() == Thought.ThoughtType.Answer ? Thought.ThoughtType.Question : Thought.ThoughtType.Answer);
    }

    public boolean isQuestion() {
        return isQuestion;
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
        TextView promptTextView = (TextView) view.findViewById(R.id.prompt);
        String text;
        if (isQuestion) {
            text = "Stelle dir eine Frage in Bezug auf:";
        } else {
            text = "Beantworte dir selbst die Frage:";
        }
        promptTextView.setText(text);


        TextView previousTextView = (TextView) view.findViewById(R.id.previousThought);
        previousTextView.setText(previousThought.getText());
        editText = (EditText) view.findViewById(R.id.editText);
    }

    public String getText() {
        return editText.getText().toString();
    }
}
