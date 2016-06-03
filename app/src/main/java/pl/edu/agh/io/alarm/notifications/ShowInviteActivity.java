package pl.edu.agh.io.alarm.notifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.edu.agh.io.alarm.R;

public class ShowInviteActivity extends Activity {
    public static final String GROUP_NAME = "groupName";
    public static final String NICKNAME = "nickname";
    public static final String ANSWER = "answer";

    private String groupName;
    private String nickname;
    private TextView nicknameTextView;
    private TextView textTextView;
    private Button btnYes;
    private Button btnNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_invite);

        btnYes = (Button) findViewById(R.id.INVITE_TAK);
        btnNo = (Button) findViewById(R.id.INVITE_NIE);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(NICKNAME,nickname);
                intent.putExtra(GROUP_NAME,groupName);
                intent.putExtra(ANSWER,true);
                sendBroadcast(intent);
                finish();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(NICKNAME,nickname);
                intent.putExtra(GROUP_NAME,groupName);
                intent.putExtra(ANSWER,false);
                sendBroadcast(intent);
                finish();
            }
        });

        final Intent intent = getIntent();
        final Bundle bundle  = intent.getExtras();
        if(bundle != null) {
            groupName = bundle.getString(GROUP_NAME);
            nickname = bundle.getString(NICKNAME);

            nicknameTextView = (TextView) findViewById(R.id.NOTIFICATION_nickname);
            nicknameTextView.setText("Uzytkownik "+nickname+" zaprosil Cie do grupy: ");

            textTextView = (TextView) findViewById(R.id.NOTIFICATION_text);
            textTextView.setText(groupName);

        }
    }
}
