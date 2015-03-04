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
    private Handler handler = new Handler();

    public WorkerFragment() {
        super(R.string.no_task, R.layout.list_item_task);
    }

    @Override
    protected void setItemView(Wrapper wrapper, View view) {
        State state = (State) view.getTag();
        if (state == null) {
            String tagName = wrapper.getClass().getSimpleName();
            tagName = tagName.substring(0, tagName.length() - "Wrapper".length());
            state = State.valueOf(tagName.toUpperCase());
            view.setTag(state);
        } else if (wrapper.getClass().getName().toUpperCase().contains(state.name())) {
            return;
        }

        state.setVisibility(view);

        String methodName = "render" + state.name().charAt(0) + state.name().substring(1).toLowerCase() + "Wrapper";
        try {
            getClass().getDeclaredMethod(methodName, Wrapper.class, View.class).invoke(this, wrapper, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderWaitingWrapper(Wrapper wrapper, View view) {
    }

    private void renderCompleteWrapper(Wrapper wrapper, View view) {
        ((TextView) view.findViewById(R.id.task_state)).setText(R.string.task_complete);
    }

    private void renderRefuseWrapper(Wrapper wrapper, View view) {
        ((TextView) view.findViewById(R.id.task_state)).setText("You Are Rejected for " + ((RefuseWrapper) wrapper).reason);
    }

    private void renderRequestWrapper(Wrapper wrapper, final View view) {
        final RequestWrapper request = (RequestWrapper) wrapper;
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
                            view.setTag(State.WAITING);
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
                    if (iterator.next().taskId == request.taskId) {
                        iterator.remove();
                        break;
                    }
                }
                setData(data);
            }
        });
    }

    private void renderDelegateWrapper(Wrapper wrapper, final View view) {
        final DelegateWrapper delegate = (DelegateWrapper) wrapper;
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
                    addMessageWrapper(new RefuseWrapper(delegate.taskId, RefuseWrapper.Reason.OFFER_OUT_OF_DATE));
                }
            }
        });
        view.findViewById(R.id.task_do).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).onNavigationDrawerItemSelected(TASK_SUBMIT_TAG);
                ((TaskSubmitFragment) getFragmentManager().findFragmentByTag(TASK_SUBMIT_TAG)).setDelegateWrapper(delegate);
            }
        });
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

    private enum State {
        REQUEST(new boolean[]{true, false, true, false, true, true, false, false}),
        WAITING(new boolean[]{true, false, true, false, false, false, true, false}),
        DELEGATE(new boolean[]{false, true, true, false, false, false, false, true}),
        REFUSE, COMPLETE;
        private boolean[] visibility;

        State(boolean[] visibility) {
            this.visibility = visibility;
        }

        State() {
            this(new boolean[]{false, false, false, true, false, true, false, false});
        }

        public void setVisibility(View v) {
            v.findViewById(R.id.task_ddl).setVisibility(visibility[0] ? View.VISIBLE : View.GONE);
            v.findViewById(R.id.task_time_remain).setVisibility(visibility[1] ? View.VISIBLE : View.GONE);
            v.findViewById(R.id.task_reward).setVisibility(visibility[2] ? View.VISIBLE : View.GONE);
            v.findViewById(R.id.task_state).setVisibility(visibility[3] ? View.VISIBLE : View.GONE);
            v.findViewById(R.id.task_offer).setVisibility(visibility[4] ? View.VISIBLE : View.GONE);
            v.findViewById(R.id.task_remove).setVisibility(visibility[5] ? View.VISIBLE : View.GONE);
            v.findViewById(R.id.task_waiting).setVisibility(visibility[6] ? View.VISIBLE : View.GONE);
            v.findViewById(R.id.task_do).setVisibility(visibility[7] ? View.VISIBLE : View.GONE);
        }
    }

}
