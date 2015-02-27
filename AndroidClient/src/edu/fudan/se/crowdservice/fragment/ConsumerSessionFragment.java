package edu.fudan.se.crowdservice.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
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
        addViewByLastMessage();
    }

    private void addViewByLastMessage() {
        final ConsumerSession.Message lastMessage = data.get(data.size() - 1);
        switch (lastMessage.type) {
            case CONSUMER_INPUT:
            case SERVICE_START:
            case SERVICE_STOP:
            case SERVICE_EXCEPTION:
            case SHOW_MESSAGE:
            case TEMPLATE_STOP:
                break;
            case REQUEST_INPUT:
                userInputView.findViewById(R.id.user_input_submit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Editable text = ((EditText) userInputView.findViewById(R.id.user_input)).getText();
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getListView().addFooterView(initFooterView(savedInstanceState));
        super.onViewCreated(view, savedInstanceState);
    }

    private View initFooterView(Bundle savedInstanceState) {
        View footer = getLayoutInflater(savedInstanceState).inflate(R.layout.list_footer_user_input, null);
        userInputView = footer.findViewById(R.id.user_input_view);
        userChooseView = footer.findViewById(R.id.user_choose_view);
        userConfirmView = footer.findViewById(R.id.user_confirm_view);

        switchInputView(null);
        return footer;
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
