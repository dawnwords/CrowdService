package edu.fudan.se.crowdservice.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.felix.TemplateManager;
import edu.fudan.se.crowdservice.felix.TemplateManager.OnTemplateResolvedListener;
import org.osgi.service.obr.Resource;


/**
 * Created by Dawnwords on 2015/2/22.
 */
public class ConsumerDrawerView extends FrameLayout {

    private TemplateManager tm;
    private TemplateAdapter adapter;
    private View loadingView, noTemplateView;
    private ListView templateList;

    public ConsumerDrawerView(LayoutInflater inflater) {
        super(inflater.getContext());
        inflater.inflate(R.layout.list_item_navi_consumer, this, true);

        loadingView = findViewById(R.id.loading_template);
        noTemplateView = findViewById(R.id.no_template);
        initTemplateList();
        initAddTemplateButton();
    }

    private void initTemplateList() {
        adapter = new TemplateAdapter();
        templateList = (ListView) findViewById(R.id.template_list);
        templateList.setAdapter(adapter);
    }

    private void initAddTemplateButton() {
        Button addTemplate = (Button) findViewById(R.id.add_template);
        addTemplate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchView(loadingView);
                post(new Runnable() {
                    @Override
                    public void run() {
                        tm.getAvailableTemplateResource(new TemplateManager.OnResourcesReceivedListener() {
                            @Override
                            public void onResourcesReceived(Resource[] resources) {
                                adapter.setResources(resources);
                                switchView(templateList);
                                templateList.getParent().requestLayout();
                            }

                            @Override
                            public void onFailure() {
                                switchView(noTemplateView);
                            }
                        });
                    }
                });
            }
        });
    }

    private void switchView(View view) {
        noTemplateView.setVisibility(view == noTemplateView ? VISIBLE : GONE);
        templateList.setVisibility(view == templateList ? VISIBLE : GONE);
        loadingView.setVisibility(view == loadingView ? VISIBLE : GONE);
    }

    public void setTemplateManager(TemplateManager tm) {
        this.tm = tm;
    }

    public void setTemplateResolveListener(final OnTemplateResolvedListener listener) {
        templateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tm.resolveTemplate(adapter.getItem(position), getContext(), listener);
            }
        });
    }

    private class TemplateAdapter extends BaseAdapter {
        private Resource[] resources;

        public void setResources(Resource[] resources) {
            this.resources = resources;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return resources == null ? 0 : resources.length;
        }

        @Override
        public Resource getItem(int position) {
            return resources == null ? null : resources[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_template, null);
            }
            TextView templateName = (TextView) convertView.findViewById(R.id.template_name);
            templateName.setText(getItem(position).getSymbolicName());
            return convertView;
        }
    }
}
