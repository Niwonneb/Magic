package com.magic_app.magic.view.scrolling;

import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.lang.reflect.Method;

/**
 * Basic implementation of ScrollListener. Just scrolls all children when user scrolls parent.
 */
public class ScrollerTo implements ScrollListener{

    protected static Method trackMotionScroll, hTrackMotionScroll;

    public void onScrollChanged(ScrollableComponent scrollable, int l, int t, int oldl, int oldt){
        for(int i = 0; i < scrollable.getChildCount(); i++){
            View child = scrollable.getChildAt(i);

            if (child instanceof ListView){
                ListView list = (ListView) child;
                scrollBy(list, t - oldt);
            }else {
                child.scrollTo(l, t);
            }
        }
    }

    void scrollByCompat(AbsListView list, int y){
        try {
            if (trackMotionScroll == null){
                trackMotionScroll = AbsListView.class.getDeclaredMethod("trackMotionScroll", int.class, int.class);
                trackMotionScroll.setAccessible(true);
            }
            trackMotionScroll.invoke(list, -y, -y);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void scrollBy(ListView list, int y){
        try {
            if (trackMotionScroll == null){
                trackMotionScroll = ListView.class.getDeclaredMethod("trackMotionScroll", int.class, int.class);
                trackMotionScroll.setAccessible(true);
            }
            trackMotionScroll.invoke(list, -y, -y);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    void scrollBy(AbsListView list, int y){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            list.scrollListBy(y);
        } else{
            scrollByCompat(list, y);
        }
    }
}
