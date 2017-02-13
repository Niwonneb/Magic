package com.ngw.seed.view.cards;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngw.seed.R;
import com.ngw.seed.model.Thought;

public class ThoughtCard extends AbstractCard {
    private Thought thought;
    public ThoughtCard(Thought thought) {
        this.thought = thought;
    }

    @Override
    protected int getLayout() {
        return R.layout.card_view_thought;
    }

    @Override
    public void setUpView(View view) {
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(thought.getText());

        ImageView imageView1 = (ImageView) view.findViewById(R.id.left_image);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.right_image);

        imageView1.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.VISIBLE);
    }

    @Override
    public int getViewTypeAsInt() {
        return ViewType.TEXT.getIntValue();
    }

    @Override
    public Type getType() {
        return Type.THOUGHT;
    }

    public String getThoughtId() {
        return thought.getId();
    }
}
