package edu.fudan.se.crowdservice.fragment;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.activity.MainActivity;
import edu.fudan.se.crowdservice.core.Template;
import edu.fudan.se.crowdservice.data.ConsumerSession;


/**
 * Created by Jiahuan on 2015/1/21.
 */
public class ConsumerFragment extends BaseFragment<ConsumerSession> {

    private static final String CONSUMER_SESSION_TAG = "ConsumerSession";

    public ConsumerFragment() {
        super(R.string.no_template, R.layout.list_item_template);
    }

    @Override
    protected void onItemSelected(ConsumerSession session) {
        ConsumerSessionFragment fragment = (ConsumerSessionFragment) getFragmentManager().findFragmentByTag(CONSUMER_SESSION_TAG);
        fragment.setData(session.messages);
        fragment.updateConsumerSessionMessage();
        ((MainActivity) getActivity()).onNavigationDrawerItemSelected(CONSUMER_SESSION_TAG);
    }

    @Override
    protected void setItemView(ConsumerSession session, View convertView) {
        TextView templateName = (TextView) convertView.findViewById(R.id.template_name);
        TextView templateCreateTime = (TextView) convertView.findViewById(R.id.template_create_time);
        TextView lastMessage = (TextView) convertView.findViewById(R.id.last_message);

        templateName.setText(session.templateName);
        templateCreateTime.setText(session.createTime);
        ConsumerSession.Message msg = session.getLastMessage();
        lastMessage.setText(msg == null ? "Template Started." : msg.content);
    }

    public void addTemplate(Template template) {
        String templateName = template.getClass().getSimpleName();
        int sessionId = agent.executeTemplate(template);
        addData(new ConsumerSession(sessionId, templateName));
    }

    public void addConsumerSessionMessage(ConsumerSession.Message message) {
        for (ConsumerSession session : data) {
            if (session.sessionID == message.sessionID) {
                session.messages.add(message);
                setData(data);
                break;
            }
        }
    }
}
