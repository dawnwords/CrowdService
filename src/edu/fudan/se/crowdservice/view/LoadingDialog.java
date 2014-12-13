package edu.fudan.se.crowdservice.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import edu.fudan.se.crowdservice.R;

/**
 * Created by Dawnwords on 2014/12/13.
 */
public class LoadingDialog {
    public static ProgressDialog show(Context context, String msg) {
        ProgressDialog dialog = ProgressDialog.show(context, context.getString(R.string.loading), msg);
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        return dialog;
    }

    public static ProgressDialog show(Context context, String msg, Handler handler, Runnable task) {
        handler.post(task);
        return show(context, msg);
    }
}
