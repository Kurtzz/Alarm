package pl.agh.ki.io.alarm.ui.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import pl.agh.ki.io.alarm.alarm.R;
import pl.agh.ki.io.alarm.sqlite.model.Friend;

/**
 * Created by P on 21.05.2016.
 */
public class MultiChoiceFriendListAdapter extends AbstractFriendListAdapter {
    private SparseBooleanArray checkedItems;

    public MultiChoiceFriendListAdapter(Context context) {
        super(context, R.layout.friend_list_item_multi);
        checkedItems = new SparseBooleanArray();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(getTextViewResourceId(), null);

            holder.nick = (TextView) convertView.findViewById(R.id.friendListItemMulti_nickTextView);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.friendListItemMulti_checkBox);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkedItems.get(position)) {
                        checkedItems.append(position, false);
                    } else {
                        checkedItems.append(position, true);
                    }
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Friend friend = getItem(position);
        holder.nick.setText(friend.getNick());
        holder.checkBox.setChecked(checkedItems.get(position));

        return convertView;
    }

    public SparseBooleanArray getCheckedItems() {
        return checkedItems;
    }

    public void setItemChecked(Friend friend) {
        int position = getPosition(friend);
        checkedItems.append(position, true);
        notifyDataSetChanged();
    }

    private class Holder {
        public TextView nick;
        public CheckBox checkBox;
    }
}
