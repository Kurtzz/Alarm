package pl.agh.ki.io.alarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pl.agh.ki.io.alarm.alarm.R;
import pl.agh.ki.io.alarm.sqlite.model.Friend;

/**
 * Created by P on 17.05.2016.
 */
public class FriendListAdapter extends ArrayAdapter<Friend> {
    private Context context;
    public List<Friend> friendList;
    int textViewResourceId;

    public FriendListAdapter(Context context, int textViewResourceId, List<Friend> friends) {
        super(context, textViewResourceId);
        this.context = context;
        this.friendList = friends;
        this.textViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(textViewResourceId, null);
            if (textViewResourceId == R.layout.friend_list_item) {
                holder.nick = (TextView) convertView.findViewById(R.id.nick);
            } else {
                holder.nick = (TextView) convertView.findViewById(R.id.nick_multi);
            }
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Friend friend = getItem(position);
        holder.nick.setText(friend.getNick());

        return convertView;
    }

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

    private class Holder {
        public TextView nick;
    }
}
