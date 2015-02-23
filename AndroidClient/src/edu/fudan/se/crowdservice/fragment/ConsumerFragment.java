package edu.fudan.se.crowdservice.fragment;

import android.view.View;
import edu.fudan.se.crowdservice.R;
import edu.fudan.se.crowdservice.felix.TemplateManager;
import org.osgi.service.obr.Resource;


/**
 * Created by Jiahuan on 2015/1/21.
 */
public class ConsumerFragment extends BaseFragment<Resource> {

    private TemplateManager tm;
//    private Handler handler;
//    private ProgressDialog dialog;

    public ConsumerFragment() {
        super(R.string.no_template, R.layout.list_item_template);
//        this.handler = new Handler();
    }

    public void setTemplateManager(TemplateManager tm) {
        this.tm = tm;
    }

//    public void loadAvailableTemplates() {
//        dialog = LoadingDialog.show(getActivity(), getString(R.string.loading_template_list), handler, new Runnable() {
//            @Override
//            public void run() {
//                tm.getAvailableTemplateResource(new TemplateManager.OnResourcesReceivedListener() {
//                    @Override
//                    public void onResourcesReceived(Resource[] resources) {
//                        ArrayList<Resource> data = new ArrayList<Resource>();
//                        data.addAll(Arrays.asList(resources));
//                        setData(data);
//                        dialog.dismiss();
//                    }
//
//                    @Override
//                    public void onFailure() {
//                        dialog.dismiss();
//                        showMessage("Unable to fetch template list!");
//                    }
//                });
//            }
//        });
//    }

    @Override
    protected void onItemSelected(Resource item) {
//        tm.resolveTemplate(item, getActivity(), new TemplateManager.OnTemplateResolvedListener() {
//            @Override
//            public void onTemplateResolved(Template template) {
//                agent.executeTemplate(template);
//            }
//
//            @Override
//            public void onFailure(Requirement[] unsatisfiedRequirements) {
//                String output = "";
//                for (Requirement requirement : unsatisfiedRequirements) {
//                    output += String.format("%s:%s\n", requirement.getName(), requirement.getComment());
//                }
//                showMessage(output);
//            }
//        });
    }

    @Override
    protected void setItemView(Resource resource, View convertView) {
//        TextView templateName = (TextView) convertView.findViewById(R.id.template_name);
//        templateName.setText(resource.getSymbolicName());
    }

}
