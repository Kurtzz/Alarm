package pl.edu.agh.io.alarm.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pl.edu.agh.io.alarm.ui.UI;

public class Manager extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, UI.class);
        startActivity(intent);
    }
}
