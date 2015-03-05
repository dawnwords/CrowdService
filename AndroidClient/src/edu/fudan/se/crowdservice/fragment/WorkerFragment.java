package edu.fudan.se.crowdservice.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.util.LongSparseArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.activity.MainActivity;
import edu.fudan.se.crowdservice.view.CountDownButton;
import edu.fudan.se.crowdservice.wrapper.*;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by Jiahuan on 2015/1/21.
 */
public class WorkerFragment extends BaseFragment<Wrapper> {
    public static final String TASK_SUBMIT_TAG = "TaskSubmit";
    private final LongSparseArray<Saved> taskSavedMap;

    public WorkerFragment() {
        super(R.string.no_task, R.layout.list_item_task);
        taskSavedMap = new LongSparseArray<Saved>();
    }

    @Override
    protected void setItemView(Wrapper wrapper, View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
        }
        String methodName = "render" + wrapper.getClass().getSimpleName();
        try {
            getClass().getDeclaredMethod(methodName, Wrapper.class, ViewHolder.class, Saved.class)
                    .invoke(this, wrapper, holder, taskSavedMap.get(wrapper.taskId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderWaitingWrapper(Wrapper wrapper, ViewHolder holder, Saved saved) {
        i("renderWaitingWrapper");
        State.WAITING.setVisibility(holder);
        WaitingWrapper waiting = (WaitingWrapper) wrapper;
        holder.description.setText(saved.description);
        holder.reward.setText("Reward:" + waiting.cost);
    }

    private void renderCompleteWrapper(Wrapper wrapper, ViewHolder holder, Saved saved) {
        i("renderCompleteWrapper");
        renderStateWrapper(wrapper, holder, State.COMPLETE, getString(R.string.task_complete), saved);
    }

    private void renderRefuseWrapper(Wrapper wrapper, ViewHolder holder, Saved saved) {
        i("renderRefuseWrapper");
        renderStateWrapper(wrapper, holder, State.REFUSE, "You Are Rejected for " + ((RefuseWrapper) wrapper).reason, saved);
    }

    private void renderStateWrapper(Wrapper wrapper, ViewHolder holder, State state, String message, Saved saved) {
        state.setVisibility(holder);
        holder.description.setText(saved.description);
        holder.state.setText(message);
        holder.remove.setOnClickListener(new RemoveItemListener(wrapper));
    }

    private void renderDelegateWrapper(Wrapper wrapper, final ViewHolder holder, Saved saved) {
        i("renderDelegateWrapper");
        State.DELEGATE.setVisibility(holder);
        final DelegateWrapper delegate = (DelegateWrapper) wrapper;
        holder.description.setText(saved.description);
        holder.doo.setTimeUpListener(new OutDatedListener(delegate.taskId))
                .setTimeRemain(saved.ddl)
                .setClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.doo.stop();
                        ((MainActivity) getActivity()).onNavigationDrawerItemSelected(TASK_SUBMIT_TAG);
                        ((TaskSubmitFragment) getFragmentManager().findFragmentByTag(TASK_SUBMIT_TAG)).setDelegateWrapper(delegate);
                    }
                }).start();
        holder.reward.setText("Reward:" + delegate.cost);
    }

    private void renderRequestWrapper(Wrapper wrapper, final ViewHolder holder, Saved saved) {
        i("renderRequestWrapper");
        State.REQUEST.setVisibility(holder);
        final RequestWrapper request = (RequestWrapper) wrapper;
        saved.description(request.description);
        holder.description.setText(request.description);
        holder.offer.setTimeUpListener(new OutDatedListener(request.taskId))
                .setTimeRemain(saved.ddl)
                .setClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.offer.stop();
                        showOfferPriceDialog(request.taskId);
                    }
                }).start();
        holder.remove.setOnClickListener(new RemoveItemListener(request));
    }

    private void i(String msg) {
        Log.i(getClass().getSimpleName(), msg);
    }

    private void showOfferPriceDialog(final long taskId) {
        final EditText input = (EditText) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_task_offer, null);
        String title = getResources().getString(R.string.offer_price);
        new AlertDialog.Builder(getActivity()).setTitle(title).setCancelable(false)
                .setView(input).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                try {
                    int offer = Integer.parseInt(input.getText().toString());
                    addMessageWrapper(new WaitingWrapper(taskId, offer));
                    agent.sendOffer(new OfferWrapper(taskId, offer));
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage("Please Input correct price for this task!");
                }
            }
        }).create().show();
    }

    public synchronized void addMessageWrapper(Wrapper value) {
        i("Message:" + value.toString());
        savedDDL(value);
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).taskId == value.taskId) {
                data.set(i, value);
                refreshList();
                return;
            }
        }
        addData(value);
    }

    private void savedDDL(Wrapper value) {
        Saved saved = taskSavedMap.get(value.taskId);
        if (saved == null) {
            saved = new Saved().taskId(value.taskId);
        }
        int ddl = value instanceof DelegateWrapper ? ((DelegateWrapper) value).deadline :
                value instanceof RequestWrapper ? ((RequestWrapper) value).deadline : 0;
        taskSavedMap.put(value.taskId, saved.ddl(ddl * 1000 + new Date().getTime()));
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

        public void setVisibility(ViewHolder v) {
            v.offer.setVisibility(visibility[0] ? View.VISIBLE : View.GONE);
            v.reward.setVisibility(visibility[1] ? View.VISIBLE : View.GONE);
            v.waiting.setVisibility(visibility[2] ? View.VISIBLE : View.GONE);
            v.doo.setVisibility(visibility[3] ? View.VISIBLE : View.GONE);
            v.state.setVisibility(visibility[4] ? View.VISIBLE : View.GONE);
            v.remove.setVisibility(visibility[5] ? View.VISIBLE : View.GONE);
        }
    }

    private class ViewHolder {
        CountDownButton offer, doo;
        TextView reward, state, description;
        Button remove;
        View waiting;

        ViewHolder(View v) {
            description = (TextView) v.findViewById(R.id.task_description);
            offer = (CountDownButton) v.findViewById(R.id.task_offer);
            doo = (CountDownButton) v.findViewById(R.id.task_do);
            reward = (TextView) v.findViewById(R.id.task_reward);
            waiting = v.findViewById(R.id.task_waiting);
            state = (TextView) v.findViewById(R.id.task_state);
            remove = (Button) v.findViewById(R.id.task_remove);
            v.setTag(this);
        }
    }

    private class Saved {
        long taskId;
        String description;
        long ddl;

        Saved taskId(long taskId) {
            this.taskId = taskId;
            return this;
        }

        Saved description(String description) {
            this.description = description;
            return this;
        }

        Saved ddl(long ddl) {
            this.ddl = ddl;
            return this;
        }
    }

    private class OutDatedListener implements CountDownButton.TimeUpListener {
        private long taskId;

        public OutDatedListener(long taskId) {
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

        @Override
        public String toString() {
            return "WaitingWrapper{" +
                    "taskId=" + taskId +
                    ",cost=" + cost +
                    '}';
        }
    }

    private class RemoveItemListener implements View.OnClickListener {
        private Wrapper wrapper;

        public RemoveItemListener(Wrapper wrapper) {
            this.wrapper = wrapper;
        }

        @Override
        public void onClick(View view) {
            Iterator<Wrapper> iterator = data.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().taskId == wrapper.taskId) {
                    iterator.remove();
                    break;
                }
            }
            setData(data);
        }
    }
}
