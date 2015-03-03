package edu.fudan.se.crowdservice.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.activity.MainActivity;
import edu.fudan.se.crowdservice.wrapper.*;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by Jiahuan on 2015/1/21.
 */
public class WorkerFragment extends BaseFragment<Wrapper> {
    public static final String TASK_SUBMIT_TAG = "TaskSubmit";
    private static boolean[] REQUEST_VISIBILITY = {true, false, true, false, true, true, false, false};
    private static boolean[] WAIT_VISIBILITY = {true, false, true, false, false, false, true, false};
    private static boolean[] DELEGATE_VISIBILITY = {false, true, true, false, false, false, false, true};
    private static boolean[] STATE_VISIBILITY = {false, false, false, true, false, true, false, false};
    private Handler handler = new Handler();

    public WorkerFragment() {
        super(R.string.no_task, R.layout.list_item_task);
    }

    @Override
    protected void setItemView(Wrapper wrapper, View view) {
        if (wrapper instanceof DelegateWrapper) {
            renderDelegateWrapper((DelegateWrapper) wrapper, view);
        } else if (wrapper instanceof RequestWrapper) {
            renderRequestWrapper((RequestWrapper) wrapper, view);
        } else if (wrapper instanceof RefuseWrapper) {
            renderRefuseWrapper((RefuseWrapper) wrapper, view);
        } else if (wrapper instanceof CompleteWrapper) {
            renderCompleteWrapper(view);
        }
    }

    private void renderCompleteWrapper(View view) {
        setVisibility(view, STATE_VISIBILITY);
        ((TextView) view.findViewById(R.id.task_state)).setText(R.string.task_complete);
    }

    private void renderRefuseWrapper(RefuseWrapper wrapper, View view) {
        setVisibility(view, STATE_VISIBILITY);
        ((TextView) view.findViewById(R.id.task_state)).setText("You Are Rejected for " + wrapper.reason);
    }

    private void renderRequestWrapper(final RequestWrapper request, final View view) {
        setVisibility(view, REQUEST_VISIBILITY);
        ((TextView) view.findViewById(R.id.task_description)).setText(request.description);
        //TODO + ddl + reward in request wrapper
        ((TextView) view.findViewById(R.id.task_ddl)).setText("12:20");
        ((TextView) view.findViewById(R.id.task_reward)).setText("20");
        view.findViewById(R.id.task_offer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText input = (EditText) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_task_offer, null);
                String title = getResources().getString(R.string.offer_price);
                new AlertDialog.Builder(getActivity()).setTitle(title).setCancelable(false)
                        .setView(input).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        try {
                            int offer = Integer.parseInt(input.getText().toString());
                            agent.sendOffer(new OfferWrapper(request.taskId, offer));
                            dialog.dismiss();
                            setVisibility(view, WAIT_VISIBILITY);
                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage("Please Input correct price for this task!");
                        }
                    }
                }).create().show();
            }
        });
        view.findViewById(R.id.task_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iterator<Wrapper> iterator = data.iterator();
                while (iterator.hasNext()) {
                    Wrapper wrapper = iterator.next();
                    if (wrapper.taskId == request.taskId) {
                        iterator.remove();
                        return;
                    }
                }
            }
        });
    }

    private void renderDelegateWrapper(final DelegateWrapper delegateWrapper, final View view) {
        setVisibility(view, DELEGATE_VISIBILITY);
        //TODO + ddl in delegateWrapper
        final Date ddl = new Date();
        ddl.setTime(ddl.getTime() + 100 * 1000);
        final TextView timeRemain = (TextView) view.findViewById(R.id.task_time_remain);
        handler.post(new Runnable() {
            @Override
            public void run() {
                long time = (ddl.getTime() - new Date().getTime()) / 1000;
                if (time > 0) {
                    timeRemain.setText(time + "s");
                    handler.postDelayed(this, 1000);
                } else {
                    setVisibility(view, STATE_VISIBILITY);
                    ((TextView) view.findViewById(R.id.task_state)).setText(R.string.task_out_of_date);
                }
            }
        });
        view.findViewById(R.id.task_do).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).onNavigationDrawerItemSelected(TASK_SUBMIT_TAG);
                ((TaskSubmitFragment) getFragmentManager().findFragmentByTag(TASK_SUBMIT_TAG)).setDelegateWrapper(delegateWrapper);
            }
        });
    }

    private void setVisibility(View v, boolean[] visibility) {
        v.findViewById(R.id.task_ddl).setVisibility(visibility[0] ? View.VISIBLE : View.GONE);
        v.findViewById(R.id.task_time_remain).setVisibility(visibility[1] ? View.VISIBLE : View.GONE);
        v.findViewById(R.id.task_reward).setVisibility(visibility[2] ? View.VISIBLE : View.GONE);
        v.findViewById(R.id.task_state).setVisibility(visibility[3] ? View.VISIBLE : View.GONE);
        v.findViewById(R.id.task_offer).setVisibility(visibility[4] ? View.VISIBLE : View.GONE);
        v.findViewById(R.id.task_remove).setVisibility(visibility[5] ? View.VISIBLE : View.GONE);
        v.findViewById(R.id.task_waiting).setVisibility(visibility[6] ? View.VISIBLE : View.GONE);
        v.findViewById(R.id.task_do).setVisibility(visibility[7] ? View.VISIBLE : View.GONE);
    }

    public synchronized void addMessageWrapper(Wrapper value) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).taskId == value.taskId) {
                data.set(i, value);
                setData(data);
                return;
            }
        }
        addData(value);
    }
}
