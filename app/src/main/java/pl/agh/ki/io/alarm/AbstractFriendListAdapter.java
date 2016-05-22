package pl.agh.ki.io.alarm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import pl.agh.ki.io.alarm.sqlite.model.Friend;

/**
 * Created by P on 21.05.2016.
 */
public abstract class AbstractFriendListAdapter extends ArrayAdapter<Friend> {
    private Context context;
    private List<Friend> friendList;
    private int textViewResourceId;

    public AbstractFriendListAdapter(Context context, int textViewResourceId, List<Friend> friends) {
        super(context, textViewResourceId);
        this.context = context;
        this.friendList = friends;
        this.textViewResourceId = textViewResourceId;
    }

    @Override
    public abstract View getView(final int position, View convertView, ViewGroup parent);

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Friend getItem(int position) {
        return friendList.get(position);
    }

    public void setArrayList(List<Friend> friends) {
        this.friendList = friends;
        notifyDataSetChanged();
    }

    @Override
    public Context getContext() {
        return context;
    }

    public int getTextViewResourceId() {
        return textViewResourceId;
    }
}
