package pl.edu.agh.io.alarm.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.List;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.sqlite.helper.DatabaseHelper;
import pl.edu.agh.io.alarm.sqlite.model.Group;
import pl.edu.agh.io.alarm.ui.Constants;
import pl.edu.agh.io.alarm.ui.Constants.IdType;
import pl.edu.agh.io.alarm.ui.activities.CreateGroupActivity;
import pl.edu.agh.io.alarm.ui.activities.EditGroupActivity;
import pl.edu.agh.io.alarm.ui.activities.SendMessageActivity;
import pl.edu.agh.io.alarm.ui.adapters.GroupListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment extends Fragment {
    private ExpandableListView groupList;
    private GroupListAdapter groupListAdapter;

    private OnFragmentInteractionListener mListener;
    private FloatingActionButton floatingActionButton;

    private DatabaseHelper helper;

    public GroupsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GroupsFragment.
     */
    public static GroupsFragment newInstance() {
        GroupsFragment fragment = new GroupsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        helper = new DatabaseHelper(getContext());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_groups, container, false);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.groupFragment_createGroupFab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateGroupActivity.class);
                startActivity(intent);
            }
        });

        List<Group> groups = helper.getGroups();

        groupList = (ExpandableListView) rootView.findViewById(R.id.groupFragment_groupListView);
        groupListAdapter = new GroupListAdapter(getContext());

        groupListAdapter.setArrayList(groups);
        groupList.setAdapter(groupListAdapter);

        registerForContextMenu(groupList);

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = new MenuInflater(getContext());
        menuInflater.inflate(R.menu.group_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case R.id.groupMenu_alarm:
                    sendAlarm(info.packedPosition);
                    return true;
                case R.id.groupMenu_edit:
                    editGroup(info.packedPosition);
                    return true;
                case R.id.groupMenu_delete:
                    deleteGroup(info.packedPosition);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return false;
    }

    private void sendAlarm(long packagePosition) {
        Intent intent = new Intent(getContext(), SendMessageActivity.class);
        intent.putExtra(Constants.EXTRA_ID, groupListAdapter.getGroup(groupList.getFlatListPosition(packagePosition)).getNameId());
        intent.putExtra(Constants.EXTRA_ID_TYPE, IdType.GROUP);
        startActivity(intent);
    }

    private void editGroup(long packagePosition) {
        Intent intent = new Intent(getContext(), EditGroupActivity.class);
        intent.putExtra(Constants.EXTRA_ID, groupListAdapter.getGroup(groupList.getFlatListPosition(packagePosition)).getNameId());
        startActivity(intent);
    }

    private void deleteGroup(long packagePosition) {
        Group group = groupListAdapter.getGroup(groupList.getFlatListPosition(packagePosition));
        helper.deleteGroup(group.getNameId());
        List<Group> list = helper.getGroups();
        groupListAdapter.setArrayList(list);
        Toast.makeText(getContext(), "Group \"" + group.getNameId() + "\" deleted successfully", Toast.LENGTH_SHORT).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Group> groups = helper.getGroups();
        groupListAdapter.setArrayList(groups);
    }
}
