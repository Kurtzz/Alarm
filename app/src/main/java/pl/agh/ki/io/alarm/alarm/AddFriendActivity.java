package pl.agh.ki.io.alarm.alarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        Button addFriendButton = (Button) findViewById(R.id.add_friend_button);
        addFriendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
