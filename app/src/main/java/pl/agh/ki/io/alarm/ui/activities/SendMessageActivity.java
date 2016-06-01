package pl.agh.ki.io.alarm.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import pl.agh.ki.io.alarm.alarm.R;

public class SendMessageActivity extends AppCompatActivity implements View.OnClickListener {
    private int receiversId;
    private String idType;

    public static final String EXTRA_ID = "pl.agh.ki.io.alarm.SendMessage.Activity.ID";
    public static final String EXTRA_ID_TYPE = "pl.agh.ki.io.alarm.SendMessage.Activity.ID_TYPE";
    public static final String TYPE_FRIEND = "TYPE_FRIEND";
    public static final String TYPE_GROUP = "TYPE_GROUP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Spinner spinner = (Spinner) findViewById(R.id.sendMessage_levelSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarmLevelsArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button sendButton = (Button) findViewById(R.id.sendMessage_sendButton);
        sendButton.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        receiversId = intent.getIntExtra(EXTRA_ID, 0);
        idType = intent.getStringExtra(EXTRA_ID_TYPE);
    }

    @Override
    public void onClick(View v) {
        Spinner spinner = (Spinner) findViewById(R.id.sendMessage_levelSpinner);
        EditText editText = (EditText) findViewById(R.id.sendMessage_messageEditText);

        String msgContent = editText.getText().toString();
        int level = Integer.valueOf(spinner.getSelectedItem().toString().substring(6));

        // TODO: Send message

        spinner.setSelection(0);
        editText.setText("");
    }
}
