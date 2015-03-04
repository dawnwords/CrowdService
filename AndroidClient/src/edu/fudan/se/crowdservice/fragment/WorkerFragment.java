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
import edu.fudan.se.crowdservice.view.CountDownButton;
import edu.fudan.se.crowdservice.wrapper.*;

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
        String methodName = "render" + wrapper.getClass().getSimpleName();
        try {
            getClass().getDeclaredMethod(methodName, Wrapper.class, View.class).invoke(this, wrapper, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderWaitingWrapper(Wrapper wrapper, View view) {
        State.WAITING.setVisibility(view);
        WaitingWrapper waiting = (WaitingWrapper) wrapper;
        ((TextView) view.findViewById(R.id.task_reward)).setText("Reward:" + waiting.cost);
    }

    private void renderCompleteWrapper(Wrapper wrapper, View view) {
        State.COMPLETE.setVisibility(view);
        ((TextView) view.findViewById(R.id.task_state)).setText(R.string.task_complete);
    }

    private void renderRefuseWrapper(Wrapper wrapper, View view) {
        State.REFUSE.setVisibility(view);
        ((TextView) view.findViewById(R.id.task_state)).setText("You Are Rejected for " + ((RefuseWrapper) wrapper).reason);
    }

    private void renderRequestWrapper(Wrapper wrapper, final View view) {
        State.REQUEST.setVisibility(view);
        final RequestWrapper request = (RequestWrapper) wrapper;
        ((TextView) view.findViewById(R.id.task_description)).setText(request.description);
        ((CountDownButton) view.findViewById(R.id.task_offer))
                .setTimeUpListener(new OfferOutDatedListener(request.taskId))
                .setTimeRemain(request.deadline)
                .setClickListener(new View.OnClickListener() {
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
                                    addMessageWrapper(new WaitingWrapper(request.taskId, offer));
                                    agent.sendOffer(new OfferWrapper(request.taskId, offer));
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    showMessage("Please Input correct price for this task!");
                                }
                            }
                        }).create().show();
                    }
                }).start();
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
                refreshList();
            }
        });
    }

    private void renderDelegateWrapper(Wrapper wrapper, final View view) {
        State.DELEGATE.setVisibility(view);
        final DelegateWrapper delegate = (DelegateWrapper) wrapper;
        ((CountDownButton) view.findViewById(R.id.task_do))
                .setTimeUpListener(new OfferOutDatedListener(delegate.taskId))
                .setTimeRemain(delegate.deadline)
                .setClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((MainActivity) getActivity()).onNavigationDrawerItemSelected(TASK_SUBMIT_TAG);
                        ((TaskSubmitFragment) getFragmentManager().findFragmentByTag(TASK_SUBMIT_TAG)).setDelegateWrapper(delegate);
                    }
                }).start();
        ((TextView) view.findViewById(R.id.task_reward)).setText("Reward:" + delegate.cost);
    }

    public synchronized void addMessageWrapper(Wrapper value) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).taskId == value.taskId) {
                data.set(i, value);
                refreshList();
                return;
            }
        }
        addData(value);
    }

    private enum State {
        REQUEST(new boolean[]{true, false, false, false, false, true}),
        WAITING(new boolean[]{false, true, true, false, false, false}),
        DELEGATE(new boolean[]{false, true, false, true, false, false}),
        REFUSE, COMPLETE;
        private boolean[] visibility;

        State(boolean[] visibility) {
            this.visibility = visibility;
        }

        State() {
            this(new boolean[]{false, false, false, false, true, true});
        }

        public void setVisibility(View v) {
            v.findViewById(R.id.task_offer).setVisibility(visibility[0] ? View.VISIBLE : View.GONE);
            v.findViewById(R.id.task_reward).setVisibility(visibility[1] ? View.VISIBLE : View.GONE);
            v.findViewById(R.id.task_waiting).setVisibility(visibility[2] ? View.VISIBLE : View.GONE);
            v.findViewById(R.id.task_do).setVisibility(visibility[3] ? View.VISIBLE : View.GONE);
            v.findViewById(R.id.task_state).setVisibility(visibility[4] ? View.VISIBLE : View.GONE);
            v.findViewById(R.id.task_remove).setVisibility(visibility[5] ? View.VISIBLE : View.GONE);
        }
    }

    private class OfferOutDatedListener implements CountDownButton.TimeUpListener {
        private long taskId;

        public OfferOutDatedListener(long taskId) {
            this.taskId = taskId;
        }

        @Override
        public void onTimeUp() {
            addMessageWrapper(new RefuseWrapper(taskId, RefuseWrapper.Reason.OFFER_OUT_OF_DATE));
        }
    }

    private class WaitingWrapper extends Wrapper {
        private static final long serialVersionUID = 207989568150920761L;
        public final int cost;

        public WaitingWrapper(long taskId, int cost) {
            super(taskId);
            this.cost = cost;
        }
    }

}
