package pl.edu.agh.io.alarm.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import pl.edu.agh.io.alarm.ui.activities.SendMessageActivity;

/**
 * Created by Mateusz on 2016-04-21.
 */
public class UI extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, SendMessageActivity.class);
        startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }
}
