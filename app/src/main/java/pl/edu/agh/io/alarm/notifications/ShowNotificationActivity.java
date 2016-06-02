package pl.edu.agh.io.alarm.notifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.middleware.Middleware;

public class ShowNotificationActivity extends Activity {
    public static final String TEXT = "text";
    public static final String NICKNAME = "nickname";

    private Button btnBack;
    private String text;
    private String nickname;
    private TextView nicknameTextView;
    private TextView textTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_notifications);

        btnBack = (Button) findViewById(R.id.NOTIFICATION_buttonBack);

        final Intent intent = getIntent();



        final Bundle bundle  = intent.getExtras();
        if(bundle != null) {
            text = bundle.getString(TEXT);
            nickname = bundle.getString(NICKNAME);

            nicknameTextView = (TextView) findViewById(R.id.NOTIFICATION_nickname);
            nicknameTextView.setText(nickname);

            textTextView = (TextView) findViewById(R.id.NOTIFICATION_text);
            textTextView.setText(text);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Middleware.getMediaPlayer() != null)
                        Middleware.getMediaPlayer().stop();
                    finish();
                }
            });
        }
    }
}
