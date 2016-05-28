package pl.agh.ki.io.alarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import pl.agh.ki.io.alarm.alarm.R;
import pl.agh.ki.io.alarm.sqlite.helper.DatabaseHelper;
import pl.agh.ki.io.alarm.sqlite.model.Friend;

public class EditFriendActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spinner;
    private Friend friend;
    private Button button;
    private CheckBox checkBox;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        friend = databaseHelper.getFriend(getIntent().getIntExtra(SendMessageActivity.EXTRA_ID, 0));

        TextView textView = (TextView) findViewById(R.id.editFriend_nickTextView);
        textView.setText(friend.getNick());

        checkBox = (CheckBox) findViewById(R.id.editFriend_checkBox);
        checkBox.setChecked(friend.isBlocked());

        spinner = (Spinner) findViewById(R.id.editFriend_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarmLevelsArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(friend.getLevel() - 1);

        button = (Button) findViewById(R.id.editFriend_saveButton);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = spinner.getSelectedItemPosition();
        friend.setLevel(position + 1);
        friend.setBlocked(checkBox.isChecked());
        databaseHelper.updateFriend(friend);
        onBackPressed();
    }
}
