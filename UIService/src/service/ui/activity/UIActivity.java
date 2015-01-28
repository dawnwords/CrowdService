package service.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import edu.fudan.se.crowdservice.core.ServiceActivity;
import edu.fudan.se.crowdservice.core.dui.KeyValueView;
import edu.fudan.se.crowdservice.kv.KeyValueHolder;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/26.
 */
public class UIActivity extends ServiceActivity {

    public static final String UI_MODEL = "ui-model";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<KeyValueHolder> holders = (ArrayList<KeyValueHolder>) getInputParameterBundle().getSerializable(UI_MODEL);

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        for (KeyValueHolder holder : holders) {
            String viewClassName = "edu.fudan.se.crowdservice.core.dui." + holder.getClass().getSimpleName() + "View";
            try {
                Class clazz = Class.forName(viewClassName);
                KeyValueView view = (KeyValueView) clazz.getConstructor(Context.class, KeyValueHolder.class).newInstance(getContext(), holder);
                linearLayout.addView(view, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Button back = new Button(getContext());
        back.setText("Back");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        linearLayout.addView(back, params);

        ScrollView scrollView = new ScrollView(getContext());
        scrollView.addView(linearLayout, params);
        scrollView.setSmoothScrollingEnabled(true);

        setContentView(scrollView, params);
    }
}
