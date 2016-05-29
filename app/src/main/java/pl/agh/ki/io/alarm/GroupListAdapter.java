package pl.agh.ki.io.alarm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import pl.agh.ki.io.alarm.alarm.R;
import pl.agh.ki.io.alarm.sqlite.model.Friend;
import pl.agh.ki.io.alarm.sqlite.model.Group;

import static pl.agh.ki.io.alarm.sqlite.model.Friend.FriendComparator.*;
import static pl.agh.ki.io.alarm.sqlite.model.Group.GroupComparator.*;

/**
 * Created by P on 18.05.2016.
 */
public class GroupListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Group> groupList;

    public GroupListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupList.get(groupPosition).getFriends().size();
    }

    @Override
    public Group getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Friend getChild(int groupPosition, int childPosition) {
        return groupList.get(groupPosition).getFriends().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        HeaderHolder holder;

        if (convertView == null) {
            holder = new HeaderHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_list_item_header, null);
            holder.groupName = (TextView) convertView.findViewById(R.id.groupListItemHeader_groupName);

            convertView.setTag(holder);
        } else {
            holder = (HeaderHolder) convertView.getTag();
        }
        Group group = getGroup(groupPosition);
        holder.groupName.setText(group.getGroupName());

        ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.groupListItemHeader_imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SendMessageActivity.class);
                intent.putExtra(SendMessageActivity.EXTRA_ID, getGroupId(groupPosition));
                intent.putExtra(SendMessageActivity.EXTRA_ID_TYPE, SendMessageActivity.TYPE_GROUP);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        FriendHolder holder;

        if (convertView == null) {
            holder = new FriendHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_list_item_child, null);
            holder.nick = (TextView) convertView.findViewById(R.id.memberOfGroup);
            convertView.setTag(holder);
        } else {
            holder = (FriendHolder) convertView.getTag();
        }
        Friend friend = getChild(groupPosition, childPosition);
        holder.nick.setText(friend.getNick());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setArrayList(List<Group> groups) {
        this.groupList = groups;
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        Collections.sort(groupList, getGroupComparator(GROUP_NAME_SORT));
        for (Group group : groupList) {
            Collections.sort(group.getFriends(), getFriendComparator(NICK_SORT));
        }
        super.notifyDataSetChanged();
    }

    private class HeaderHolder {
        public TextView groupName;
    }

    private class FriendHolder {
        public TextView nick;
    }
}
