package pl.agh.ki.io.alarm.alarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by P on 17.05.2016.
 */
public class FriendListAdapter extends ArrayAdapter<String> {
    private Context context;
    public List<String> friendList;

    public FriendListAdapter(Context context, int textViewResourceId, List<String> objects) {
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
        String friend = getItem(position);
        holder.nick.setText(friend);

        return convertView;
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public String getItem(int position) {
        return friendList.get(position);
    }

    public void setArrayList(List<String> messages) {
        this.friendList = messages;
        notifyDataSetChanged();
    }

    private class Holder {
        public TextView nick;
    }
}
