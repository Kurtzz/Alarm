package pl.agh.ki.io.alarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.agh.ki.io.alarm.alarm.R;
import pl.agh.ki.io.alarm.sqlite.model.Friend;

/**
 * Created by P on 21.05.2016.
 */
public class DefaultFriendListAdapter extends AbstractFriendListAdapter {

    public DefaultFriendListAdapter(Context context, int textViewResourceId, List<Friend> friends) {
        super(context, textViewResourceId, friends);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(getTextViewResourceId(), null);
            holder.nick = (TextView) convertView.findViewById(R.id.friendListItem_nickTextView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Friend friend = getItem(position);
        holder.nick.setText(friend.getNick());

        return convertView;
    }

    private class Holder {
        public TextView nick;
    }
}
