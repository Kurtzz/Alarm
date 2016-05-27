package pl.agh.ki.io.alarm;

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
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import java.util.List;

import pl.agh.ki.io.alarm.alarm.R;
import pl.agh.ki.io.alarm.sqlite.helper.DatabaseHelper;
import pl.agh.ki.io.alarm.sqlite.model.Group;


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

    private DatabaseHelper databaseHelper;

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
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());
        if (getArguments() != null) {

        }
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

        List<Group> groups = databaseHelper.getGroups();

        groupList = (ExpandableListView) rootView.findViewById(R.id.groupFragment_groupListView);
        groupListAdapter = new GroupListAdapter(getContext(), groups);

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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.groupMenu_alarm:
                sendAlarm(info.position);
                return true;
            case R.id.groupdMenu_edit:
                editGroup(info.position);
                return true;
            case R.id.groupMenu_delete:
                deleteGroup(info.position);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendAlarm(int position) {
        Intent intent = new Intent(getContext(), SendMessageActivity.class);
        intent.putExtra(SendMessageActivity.EXTRA_ID, groupListAdapter.getGroup(position).getId());
        intent.putExtra(SendMessageActivity.EXTRA_ID_TYPE, SendMessageActivity.TYPE_GROUP);
        startActivity(intent);
    }

    private void editGroup(int position) {

    }

    private void deleteGroup(int position) {

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
        List<Group> groups = databaseHelper.getGroups();
        groupListAdapter.setArrayList(groups);

        System.out.println(groups);
    }
}
