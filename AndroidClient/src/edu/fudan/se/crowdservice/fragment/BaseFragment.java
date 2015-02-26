package edu.fudan.se.crowdservice.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.fudan.se.crowdservice.jade.agent.AgentInterface;

import java.util.ArrayList;

/**
 * Created by Jiahuan on 2015/1/24.
 */
public abstract class BaseFragment<T> extends ListFragment {
    protected AgentInterface agent;
    protected ArrayList<T> data;
    private int emptyStringResId, itemLayoutId;
    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public T getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(itemLayoutId, null);
            }
            setItemView(getItem(i), view);
            return view;
        }
    };

    public BaseFragment(int emptyStringResId, int itemLayoutId) {
        super();
        this.emptyStringResId = emptyStringResId;
        this.itemLayoutId = itemLayoutId;
    }

    public void setAgent(AgentInterface agent) {
        this.agent = agent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new ArrayList<T>();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyText(getText(emptyStringResId));
        setListAdapter(adapter);
    }

    @Override
    public final void onListItemClick(ListView l, View v, int position, long id) {
        onItemSelected((T) adapter.getItem(position));
    }

    protected final void setData(ArrayList<T> data) {
        this.data = data;
        adapter.notifyDataSetChanged();
    }

    protected final void addData(T data) {
        this.data.add(data);
        adapter.notifyDataSetChanged();
    }

    protected abstract void onItemSelected(T item);

    protected abstract void setItemView(T item, View convertView);

    protected void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

}