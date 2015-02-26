package edu.fudan.se.crowdservice.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.wrapper.OfferWrapper;
import edu.fudan.se.crowdservice.wrapper.RequestWrapper;

/**
 * Created by Jiahuan on 2015/1/21.
 */
public class WorkerFragment extends BaseFragment<RequestWrapper> {

    public WorkerFragment() {
        super(R.string.no_task, R.layout.list_item_task);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void onItemSelected(final RequestWrapper task) {
        final EditText input = new EditText(getActivity());
        input.setKeyListener(new DigitsKeyListener());
        input.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        String title = getResources().getString(R.string.offer_price);
        new AlertDialog.Builder(getActivity()).setTitle(title).setCancelable(false)
                .setView(input).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Editable text = input.getText();
                if (text != null && !text.toString().isEmpty()) {
                    try {
                        int offer = Integer.parseInt(text.toString());
                        agent.sendOffer(new OfferWrapper(task.taskId, offer));
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showMessage("Please Input an integer for task price!");
                    }
                } else {
                    showMessage("Please offer a task price!");
                }
            }
        }).create().show();

    }

    @Override
    protected void setItemView(RequestWrapper task, View view) {
        TextView taskDescription = (TextView) view.findViewById(R.id.task_description);
        taskDescription.setText(task.description);
    }

    public void addRequest(RequestWrapper value) {
        addData(value);
    }
}
