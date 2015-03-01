package edu.fudan.se.crowdservice.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.*;
import android.widget.*;
import android.widget.RelativeLayout.LayoutParams;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.data.ConsumerSession;

/**
 * Created by Dawnwords on 2015/2/26.
 */
public class ConsumerSessionFragment extends BaseFragment<ConsumerSession.Message> {

    private View userInputView;
    private View userChooseView;
    private View userConfirmView;

    public ConsumerSessionFragment() {
        super(R.string.no_service_in_execution, R.layout.list_item_consumer_session);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_consumer_session, container, false);
        userInputView = view.findViewById(R.id.user_input_view);
        userChooseView = view.findViewById(R.id.user_choose_view);
        userConfirmView = view.findViewById(R.id.user_confirm_view);
        switchInputView(null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void addViewByLastMessage() {
        final ConsumerSession.Message lastMessage = getLastMessage();
        if (lastMessage == null) {
            return;
        }
        switch (lastMessage.type) {
            case CONSUMER_INPUT:
            case SERVICE_START:
            case SERVICE_STOP:
            case SERVICE_EXCEPTION:
            case SHOW_MESSAGE:
            case TEMPLATE_STOP:
                switchInputView(null);
                break;
            case REQUEST_INPUT:
                final EditText userInput = ((EditText) userInputView.findViewById(R.id.user_input));
                userInput.setText("");
                userInputView.findViewById(R.id.user_input_submit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Editable text = userInput.getText();
                        agent.setResultInput(lastMessage.sessionID, text == null ? "" : text.toString());
                    }
                });
                switchInputView(userInputView);
                break;
            case REQUEST_CONFIRM:
                userConfirmView.findViewById(R.id.user_confirm_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        agent.setResultInput(lastMessage.sessionID, String.valueOf(true));
                    }
                });
                userConfirmView.findViewById(R.id.user_confirm_no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        agent.setResultInput(lastMessage.sessionID, String.valueOf(false));
                    }
                });
                switchInputView(userConfirmView);
                break;
            case REQUEST_CHOOSE:
                int res = android.R.layout.simple_expandable_list_item_1;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), res, lastMessage.getChoices());
                final Spinner spinnerChoice = (Spinner) userChooseView.findViewById(R.id.user_choose);
                spinnerChoice.setAdapter(adapter);
                userChooseView.findViewById(R.id.user_choose_submit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int choice = spinnerChoice.getSelectedItemPosition();
                        agent.setResultInput(lastMessage.sessionID, String.valueOf(choice));
                    }
                });
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isVisible()) {
            inflater.inflate(R.menu.consumer_session, menu);
            ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_back) {
            ((NavigationFragment.NavigationDrawerCallbacks) getActivity()).onNavigationDrawerItemSelected("Consumer");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setEmptyText(CharSequence text) {
        TextView emptyView = (TextView) getListView().getEmptyView();
        emptyView.setVisibility(View.GONE);
        emptyView.setText(text);
    }

    private ConsumerSession.Message getLastMessage() {
        return data.size() > 0 ? data.get(data.size() - 1) : null;
    }

    private void switchInputView(View inputView) {
        userInputView.setVisibility(inputView == userInputView ? View.VISIBLE : View.GONE);
        userChooseView.setVisibility(inputView == userChooseView ? View.VISIBLE : View.GONE);
        userConfirmView.setVisibility(inputView == userConfirmView ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onItemSelected(ConsumerSession.Message item) {
    }

    @Override
    protected void setItemView(ConsumerSession.Message message, View convertView) {
        TextView content = (TextView) convertView.findViewById(R.id.message_content);
        content.setText(message.content);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (message.type == ConsumerSession.MessageType.CONSUMER_INPUT) {
            content.setBackgroundResource(R.drawable.bubble_consumer);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        } else {
            content.setBackgroundResource(R.drawable.buble_system);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        }
        content.setLayoutParams(params);

        TextView time = (TextView) convertView.findViewById(R.id.message_time);
        time.setText(message.createTime);
    }

    public void updateConsumerSessionMessage() {
        setData(data);
        addViewByLastMessage();
    }
}
