package pl.edu.agh.io.alarm.notifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pl.edu.agh.io.alarm.R;

public class MyClass extends Activity {
    private Button btnBack;
    private String text;
    private String nickname;
    private TextView nicknameTextView;
    private TextView textTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_notifications);

        System.out.println("MYCLASS CONTEXT:   "+getApplicationContext().toString());
        btnBack = (Button) findViewById(R.id.NOTIFICATION_buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();

        Bundle bundle  = intent.getExtras();
        if(bundle != null) {
            text = bundle.getString("text");
            nickname = bundle.getString("nickname");

            nicknameTextView = (TextView) findViewById(R.id.NOTIFICATION_nickname);
            nicknameTextView.setText(nickname);

            textTextView = (TextView) findViewById(R.id.NOTIFICATION_text);
            textTextView.setText(text);
        }
    }
}
