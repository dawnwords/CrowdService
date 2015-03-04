package edu.fudan.se.crowdservice.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Dawnwords on 2015/3/4.
 */
public class CountDownView extends Button {

    private static final int COUNT_DOWN = 1329;
    private static final int STOP = 1330;

    private int timeRemain;
    private TimeUpListener listener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNT_DOWN:
                    setText(getText() + "(" + timeRemain + "s)");
                    if (timeRemain == 0) {
                        stop();
                        if (listener != null) {
                            listener.onTimeUp();
                        }
                    } else {
                        handler.sendEmptyMessageDelayed(COUNT_DOWN, 1000);
                    }
                    timeRemain--;
                    break;
                case STOP:
                    handler.removeMessages(COUNT_DOWN);
                    break;
            }
        }
    };

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownView setTimeUpListener(TimeUpListener listener) {
        this.listener = listener;
        return this;
    }

    public CountDownView setTimeRemain(int timeRemain) {
        this.timeRemain = timeRemain;
        stop();
        return this;
    }

    public CountDownView setClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        return this;
    }

    public void start() {
        handler.sendEmptyMessage(COUNT_DOWN);
    }

    public void stop() {
        handler.sendEmptyMessage(STOP);
    }

    public static interface TimeUpListener {
        void onTimeUp();
    }

}
