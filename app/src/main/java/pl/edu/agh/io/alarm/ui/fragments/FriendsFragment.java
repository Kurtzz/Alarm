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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.sqlite.helper.DatabaseHelper;
import pl.edu.agh.io.alarm.sqlite.model.Friend;
import pl.edu.agh.io.alarm.ui.Constants;
import pl.edu.agh.io.alarm.ui.Constants.IdType;
import pl.edu.agh.io.alarm.ui.activities.AddFriendActivity;
import pl.edu.agh.io.alarm.ui.activities.EditFriendActivity;
import pl.edu.agh.io.alarm.ui.activities.SendMessageActivity;
import pl.edu.agh.io.alarm.ui.adapters.DefaultFriendListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {
    private ListView friendList;
    private DefaultFriendListAdapter friendListAdapter;

    private OnFragmentInteractionListener mListener;
    private FloatingActionButton floatingActionButton;

    private DatabaseHelper helper;

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FriendsFragment.
     */

    public static FriendsFragment newInstance() {
        FriendsFragment fragment = new FriendsFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.friendFragment_addFriendFab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddFriendActivity.class);
                startActivity(intent);
            }
        });

        List<Friend> friends = helper.getFriends();

        friendList = (ListView) rootView.findViewById(R.id.friendFragment_friendListView);
        friendListAdapter = new DefaultFriendListAdapter(rootView.getContext(), R.layout.friend_list_item_image_button);

        friendListAdapter.setArrayList(friends);
        friendList.setAdapter(friendListAdapter);

        registerForContextMenu(friendList);

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = new MenuInflater(getContext());
        menuInflater.inflate(R.menu.friend_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (getUserVisibleHint()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()) {
                case R.id.friendMenu_alarm:
                    sendAlarm(info.position);
                    return true;
                case R.id.friendMenu_edit:
                    editFriend(info.position);
                    return true;
                case R.id.friendMenu_block:
                    blockFriend(info.position);
                    return true;
                case R.id.friendMenu_delete:
                    deleteFriend(info.position);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return false;
    }

    private void sendAlarm(int position) {
        Intent intent = new Intent(getContext(), SendMessageActivity.class);
        intent.putExtra(Constants.EXTRA_ID, friendListAdapter.getItem(position).getId());
        intent.putExtra(Constants.EXTRA_ID_TYPE, IdType.FRIEND);
        startActivity(intent);
    }

    private void editFriend(int position) {
        Intent intent = new Intent(getContext(), EditFriendActivity.class);
        intent.putExtra(Constants.EXTRA_ID, friendListAdapter.getItem(position).getId());
        startActivity(intent);
    }

    private void blockFriend(int position) {
        Friend friend = friendListAdapter.getItem(position);
        String action;
        if (friend.isBlocked()) {
            friend.setBlocked(false);
            action = "unblocked";
        } else {
            friend.setBlocked(true);
            action = "blocked";
        }
        helper.updateFriend(friend);
        friendListAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Friend \"" + friend.getNick() + "\" is " + action, Toast.LENGTH_SHORT).show();
    }

    private void deleteFriend(int position) {
        Friend friend = friendListAdapter.getItem(position);
        helper.deleteFriend(friend.getId());
        List<Friend> list = helper.getFriends();
        friendListAdapter.setArrayList(list);
        Toast.makeText(getContext(), "Friend \"" + friend.getNick() + "\" deleted successfully", Toast.LENGTH_SHORT).show();
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
        friendListAdapter.setArrayList(helper.getFriends());
    }
}
