package edu.fudan.se.crowdservice.jade.agent.behaviour;

import android.content.Context;
import android.os.Handler;
import edu.fudan.se.crowdservice.core.IOUtil;
import edu.fudan.se.crowdservice.wrapper.ConversationType;
import edu.fudan.se.crowdservice.jade.agent.uimessage.DelegateMessage;
import edu.fudan.se.crowdservice.jade.agent.uimessage.UIMessage;
import edu.fudan.se.crowdservice.kv.ImageDisplay;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;
import edu.fudan.se.crowdservice.wrapper.DelegateWrapper;

/**
 * Created by Jiahuan on 2015/1/22.
 */
public class ReceiveDelegateBehaviour extends MessageReceivingBehaviour<DelegateWrapper> {
    private Context context;

    public ReceiveDelegateBehaviour(Handler handler, Context context) {
        super(ConversationType.DELEGATE, handler);
        this.context = context;
    }

    @Override
    protected UIMessage prepareMessage(DelegateWrapper content) {
        for (int i = 0; i < content.keyValueHolders.size(); i++) {
            KeyValueHolder holder = content.keyValueHolders.get(i);
            if (holder instanceof ImageDisplay) {
                ImageDisplay imageDisplay = (ImageDisplay) holder;
                String key = imageDisplay.getKey();
                String imagePath = IOUtil.saveByteArray(((ImageDisplay) holder).getValue(), context);
                content.keyValueHolders.set(i, new ImageDisplay(key, imagePath));
            }
        }
        return new DelegateMessage(content);
    }
}
