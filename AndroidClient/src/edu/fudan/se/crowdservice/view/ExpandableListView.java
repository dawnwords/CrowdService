package edu.fudan.se.crowdservice.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Dawnwords on 2015/2/23.
 */
public class ExpandableListView extends ListView {
    private int old_count = 0;

    public ExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getCount() != old_count) {
            old_count = getCount();
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getCount() * (old_count > 0 ? getChildAt(0).getHeight() : 0);
            setLayoutParams(params);
        }
        super.onDraw(canvas);
    }
}
