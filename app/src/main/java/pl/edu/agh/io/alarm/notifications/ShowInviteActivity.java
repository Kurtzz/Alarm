package pl.edu.agh.io.alarm.notifications;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.middleware.Middleware;
import pl.edu.agh.io.alarm.sqlite.model.Friend;

public class ShowInviteActivity extends Activity {
    public static final String GROUP_NAME = "groupName";
    public static final String NICKNAME = "nickname";
    public static final String ANSWER = "answer";

    private String groupName;
    private String nickname;
    private int invitationId;
    private String senderUid;
    private TextView nicknameTextView;
    private TextView textTextView;
    private Button btnYes;
    private Button btnNo;

    private Middleware middleware;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService(new Intent(this, Middleware.class), middlewareConnection, Context.BIND_AUTO_CREATE);

        setContentView(R.layout.activity_show_invite);

        btnYes = (Button) findViewById(R.id.INVITE_TAK);
        btnNo = (Button) findViewById(R.id.INVITE_NIE);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ShowInvite", "Accepting invitation with id " + invitationId);
                Friend friend = new Friend();
                friend.setNick(nickname);
                friend.setId(senderUid);
                middleware.acceptInvitation(invitationId);
                middleware.createFriend(friend);
                finish();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ShowInvite", "Rejecting invitation with id " + invitationId);
                middleware.declineInvitation(invitationId);
                finish();
            }
        });

        final Intent intent = getIntent();
        final Bundle bundle  = intent.getExtras();
        if(bundle != null) {
            groupName = bundle.getString(GROUP_NAME);
            nickname = bundle.getString(NICKNAME);
            invitationId = bundle.getInt("invitationId");
            nicknameTextView = (TextView) findViewById(R.id.NOTIFICATION_nickname);
            nicknameTextView.setText("Uzytkownik "+nickname+" zaprosil Cie do grupy: "); // TODO: String from resources
            senderUid = bundle.getString("senderUid");
            textTextView = (TextView) findViewById(R.id.NOTIFICATION_text);
            textTextView.setText(groupName);

        }
    }

    private ServiceConnection middlewareConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ShowInviteActivity.this.middleware = ((Middleware.LocalBinder)iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            ShowInviteActivity.this.middleware = null;
        }
    };
}
