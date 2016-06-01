package pl.agh.ki.io.alarm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Collections;
import java.util.List;

import pl.agh.ki.io.alarm.sqlite.model.Friend;

import static pl.agh.ki.io.alarm.sqlite.model.Friend.FriendComparator.*;

/**
 * Created by P on 21.05.2016.
 */
public abstract class AbstractFriendListAdapter extends ArrayAdapter<Friend> {
    private final Context context;
    private final int textViewResourceId;
    private List<Friend> friendList;

    public AbstractFriendListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
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

    @Override
    public int getPosition(Friend item) {
        for (int i = 0; i < friendList.size(); i++) {
            if (friendList.get(i).equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void notifyDataSetChanged() {
        Collections.sort(friendList, getFriendComparator(BLOCK_SORT, NICK_SORT));
        super.notifyDataSetChanged();
    }

    public int getTextViewResourceId() {
        return textViewResourceId;
    }
}
