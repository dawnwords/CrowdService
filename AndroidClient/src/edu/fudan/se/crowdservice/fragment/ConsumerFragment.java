package edu.fudan.se.crowdservice.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.fudan.se.crowdservice.R;

/**
 * Created by Jiahuan on 2015/1/21.
 */
public class ConsumerFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consumer, container, false);
    }
}
