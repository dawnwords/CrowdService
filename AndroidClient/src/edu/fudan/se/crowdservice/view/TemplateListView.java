package edu.fudan.se.crowdservice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import edu.fudan.se.crowdservice.R;
import org.osgi.service.obr.Resource;

/**
 * Created by Dawnwords on 2014/12/30.
 */
public class TemplateListView extends ListView {

    private Resource[] templates;
    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return templates.length;
        }

        @Override
        public Resource getItem(int i) {
            return templates[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = inflate(getContext(), R.layout.template_list_item, null);
            }
            TextView templateName = (TextView) convertView.findViewById(R.id.template_name);
            templateName.setText(getItem(i).getSymbolicName());
            return convertView;
        }
    };

    public TemplateListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        templates = new Resource[0];
        setAdapter(adapter);
    }

    public void setTemplateResource(Resource[] templates) {
        if (templates == null) {
            templates = new Resource[0];
        }
        this.templates = templates;
        this.adapter.notifyDataSetChanged();
    }
}
