package pl.edu.agh.io.alarm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pl.edu.agh.io.alarm.ui.activities.LoginActivity;

/**
 * Created by Mateusz on 2016-04-21.
 */
public class UI extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
