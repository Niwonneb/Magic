package com.ngw.seed.view.cards;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngw.seed.R;

public class InfoCard extends AbstractCard {
    private String message;

    public InfoCard(String message) {
        this.message = message;
    }

    @Override
    protected int getLayout() {
        return R.layout.card_view_thought;
    }

    @Override
    public void setUpView(View view) {
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(message);

        TextView backgroundText = (TextView) view.findViewById(R.id.backgroundText);
        backgroundText.setText("");

        ImageView imageView1 = (ImageView) view.findViewById(R.id.left_image);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.right_image);

        imageView1.setVisibility(View.INVISIBLE);
        imageView2.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getViewTypeAsInt() {
        return ViewType.TEXT.getIntValue();
    }

    @Override
    public Type getType() {
        return Type.OTHER;
    }
}
