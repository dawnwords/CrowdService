package service.shcomputer.app.itemselection.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;
import edu.fudan.se.crowdservice.core.ServiceActivity;
import service.shcomputer.app.itemselection.interfaces.SHComputerInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jiahuan on 2015/1/27.
 */
public class SHComputerInfoActivity extends ServiceActivity {

    private SHComputerInfo[] shComputerInfos = {
            new SHComputerInfo("Thinkpad", "X240", "Intel Core i5", "8G DDR3", "256G SSD", "90%", 4000, "seller1", "Room 314, Computer Building", new Date(), 121.597862,31.191006),
            new SHComputerInfo("Thinkpad", "X240", "Intel Core i5", "8G DDR3", "256G SSD", "90%", 5000, "seller2", "Room 104, No.2 School Building", new Date(), 121.598361, 31.191066),
            new SHComputerInfo("Thinkpad", "X240", "Intel Core i5", "8G DDR3", "256G SSD", "90%", 6000, "seller3", "Room 401, SoftWare Building", new Date(), 121.59953, 31.191707),
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView = new ListView(getContext());

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return shComputerInfos.length;
            }

            @Override
            public SHComputerInfo getItem(int i) {
                return shComputerInfos[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    LinearLayout wrapper = new LinearLayout(getContext());
                    wrapper.setOrientation(LinearLayout.VERTICAL);
                    SHComputerInfo info = getItem(i);
                    wrapper.addView(createHorizontalLinearLayout("Brand:", info.brand));
                    wrapper.addView(createHorizontalLinearLayout("Serial:", info.series));
                    wrapper.addView(createHorizontalLinearLayout("CPU:", info.cpu));
                    wrapper.addView(createHorizontalLinearLayout("Memory:", info.memory));
                    wrapper.addView(createHorizontalLinearLayout("Disk:", info.disk));
                    wrapper.addView(createHorizontalLinearLayout("Price:", "" + info.price));
                    wrapper.addView(createHorizontalLinearLayout("Seller:", info.seller));
                    wrapper.addView(createHorizontalLinearLayout("Address:", info.location));
                    wrapper.addView(createHorizontalLinearLayout("", "[" + info.longitude + "," + info.longitude + "]"));
                    wrapper.addView(createHorizontalLinearLayout("Sale Time:", new SimpleDateFormat("yyyy-MM-dd").format(info.saleTime)));
                    view = wrapper;
                }
                return view;
            }

            LinearLayout createHorizontalLinearLayout(String key, String value) {
                LinearLayout result = new LinearLayout(getContext());
                result.setOrientation(LinearLayout.HORIZONTAL);
                TextView keyView = new TextView(getContext());
                keyView.setText(key);
                TextView valueView = new TextView(getContext());
                valueView.setText(value);
                result.addView(keyView, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 3));
                result.addView(valueView, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 7));
                return result;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                finish(adapterView.getAdapter().getItem(i));
            }
        });

        setContentView(listView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }
}
