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
public class FriendListAdapter extends ArrayAdapter<String> {
    private Context context;
    public List<Friend> friendList;

    public FriendListAdapter(Context context, int textViewResourceId, List<Friend> objects) {
        super(context, textViewResourceId);
        this.context = context;
        this.friendList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.friend_list_item, null);
            holder.nick = (TextView) convertView.findViewById(R.id.nick);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        String nick = getItem(position);
        holder.nick.setText(nick);

        return convertView;
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public String getItem(int position) {
        return friendList.get(position).getNick();
    }

    public void setArrayList(List<Friend> friends) {
        this.friendList = friends;
        notifyDataSetChanged();
    }

    private class Holder {
        public TextView nick;
    }
}
