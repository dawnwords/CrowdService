package edu.fudan.se.crowdservice.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.data.ConsumerSession;

/**
 * Created by Dawnwords on 2015/2/26.
 */
public class ConsumerSessionFragment extends ChildFragment<ConsumerSession.Message> {

    private View userInputView;
    private View userChooseView;
    private View userConfirmView;

    private ConsumerSession session;

    public ConsumerSessionFragment() {
        super(R.string.no_service_in_execution, R.layout.list_item_consumer_session, "Consumer");
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

    private void addViewByLastMessage() {
        final ConsumerSession.Message lastMessage = session.getLastMessage();
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
                userInput.requestFocus();
                userInputView.findViewById(R.id.user_input_submit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Editable text = userInput.getText();
                        agent.setResultInput(session.sessionID, text == null ? "" : text.toString());
                    }
                });
                switchInputView(userInputView);
                break;
            case REQUEST_CONFIRM:
                userConfirmView.findViewById(R.id.user_confirm_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        agent.setResultInput(session.sessionID, String.valueOf(true));
                    }
                });
                userConfirmView.findViewById(R.id.user_confirm_no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        agent.setResultInput(session.sessionID, String.valueOf(false));
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
                        agent.setResultInput(session.sessionID, String.valueOf(choice));
                    }
                });
                switchInputView(userChooseView);
                break;
        }
    }

    @Override
    public void setEmptyText(CharSequence text) {
        TextView emptyView = (TextView) getListView().getEmptyView();
        emptyView.setVisibility(View.GONE);
        emptyView.setText(text);
    }

    private void switchInputView(View inputView) {
        userInputView.setVisibility(inputView == userInputView ? View.VISIBLE : View.GONE);
        userChooseView.setVisibility(inputView == userChooseView ? View.VISIBLE : View.GONE);
        userConfirmView.setVisibility(inputView == userConfirmView ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void setItemView(ConsumerSession.Message message, View convertView) {
        TextView content = (TextView) convertView.findViewById(R.id.message_content);
        content.setText(message.content);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (message.type == ConsumerSession.MessageType.CONSUMER_INPUT) {
            content.setBackgroundResource(R.drawable.bubble_consumer);
            params.gravity = Gravity.RIGHT;
        } else {
            content.setBackgroundResource(R.drawable.buble_system);
            params.gravity = Gravity.LEFT;
        }
        content.setLayoutParams(params);

        TextView time = (TextView) convertView.findViewById(R.id.message_time);
        time.setText(message.createTime);
    }

    public void setSession(ConsumerSession session) {
        this.session = session;
        setData(session.getMessageList());
        addViewByLastMessage();
    }

    public void updateConsumerSessionMessage() {
        setData(data);
        addViewByLastMessage();
    }
}
