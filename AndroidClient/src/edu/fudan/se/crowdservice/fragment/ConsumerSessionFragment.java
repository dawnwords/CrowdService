package edu.fudan.se.crowdservice.fragment;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.data.ConsumerSession;

/**
 * Created by Dawnwords on 2015/2/26.
 */
public class ConsumerSessionFragment extends BaseFragment<ConsumerSession.Message> {
    public ConsumerSessionFragment() {
        super(R.string.no_service_in_execution, R.layout.list_item_consumer_session);
        addViewByLastMessage();
    }

    private void addViewByLastMessage() {
        ConsumerSession.Message lastMessage = data.get(data.size() - 1);
        switch (lastMessage.type) {
            case CONSUMER_INPUT:
            case SERVICE_START:
            case SERVICE_STOP:
            case SERVICE_EXCEPTION:
            case SHOW_MESSAGE:
            case TEMPLATE_STOP:
                break;
            case REQUEST_INPUT:
                //TODO addBottomView and setResultInput
                String result = "";
                agent.setResultInput(lastMessage.sessionID, result);
                break;
            case REQUEST_CONFIRM:
                //TODO addBottomView and setResultInput
                break;
            case REQUEST_CHOOSE:
                //TODO addBottomView and setResultInput
                break;
        }
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
}
