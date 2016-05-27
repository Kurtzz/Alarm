package pl.agh.ki.io.alarm;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import pl.agh.ki.io.alarm.alarm.R;
import pl.agh.ki.io.alarm.sqlite.model.Friend;

/**
 * Created by P on 21.05.2016.
 */
public class MultiChoiceFriendListAdapter extends AbstractFriendListAdapter {
    private SparseBooleanArray checkedItems;

    public MultiChoiceFriendListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
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
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkedItems.append(position, isChecked);
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Friend friend = getItem(position);
        holder.nick.setText(friend.getNick());

        return convertView;
    }

    public SparseBooleanArray getCheckedItems() {
        return checkedItems;
    }

    private class Holder {
        public TextView nick;
        public CheckBox checkBox;
    }
}
