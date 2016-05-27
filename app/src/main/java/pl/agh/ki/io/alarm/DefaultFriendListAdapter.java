package pl.agh.ki.io.alarm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import pl.agh.ki.io.alarm.alarm.R;
import pl.agh.ki.io.alarm.sqlite.model.Friend;

/**
 * Created by P on 21.05.2016.
 */
public class DefaultFriendListAdapter extends AbstractFriendListAdapter {

    public DefaultFriendListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(getTextViewResourceId(), null);
            holder.nick = (TextView) convertView.findViewById(R.id.friendListItem_nickTextView);
            holder.imageButton = (ImageButton) convertView.findViewById(R.id.friendListItem_imageButton);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final Friend friend = getItem(position);
        holder.nick.setText(friend.getNick());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SendMessageActivity.class);
                intent.putExtra(FriendsFragment.EXTRA_FRIEND_ID, friend.getId());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    private class Holder {
        public TextView nick;
        public ImageButton imageButton;
    }
}
