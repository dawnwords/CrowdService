package edu.fudan.se.crowdservice.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import java.util.Date;

/**
 * Created by Dawnwords on 2015/3/4.
 */
public class CountDownButton extends Button {

    private static final int COUNT_DOWN = 1329;
    private static final int STOP = 1330;

    private int timeRemain;
    private boolean hasStarted;
    private String text;
    private TimeUpListener listener;
    private Handler handler;

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.text = getText().toString();
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case COUNT_DOWN:
                        setText(text + "(" + timeRemain + "s)");
                        if (timeRemain <= 0) {
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
    }

    public CountDownButton setTimeUpListener(TimeUpListener listener) {
        this.listener = listener;
        return this;
    }

    public CountDownButton setClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        return this;
    }

    public void start() {
        if (!hasStarted) {
            handler.sendEmptyMessage(COUNT_DOWN);
            hasStarted = true;
        }
    }

    public void stop() {
        handler.sendEmptyMessage(STOP);
        hasStarted = false;
    }

    public CountDownButton setTimeRemain(long ddl) {
        this.timeRemain = (int) ((ddl - new Date().getTime()) / 1000);
        return this;
    }

    public static interface TimeUpListener {
        void onTimeUp();
    }

}
