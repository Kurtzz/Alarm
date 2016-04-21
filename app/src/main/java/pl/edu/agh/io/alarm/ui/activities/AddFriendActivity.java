package pl.edu.agh.io.alarm.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import pl.edu.agh.io.alarm.R;

/**
 * Created by Mateusz on 2016-04-21.
 */
public class AddFriendActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        Button btn = (Button) findViewById(R.id.ADDFIREND_addbtn);
        btn.setOnClickListener(this);
        ImageButton b = (ImageButton) findViewById(R.id.ADDFRIEND_exitbtn);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ADDFIREND_addbtn:
                EditText editText = (EditText) findViewById(R.id.ADDFRIEND_friend_name);
                if(editText != null){
                    System.out.println("AddFriendActivity : Friend's Name: "+editText.getText().toString());
                }
                break;
            case R.id.ADDFRIEND_exitbtn:
                System.out.println("AddFriendActivity : ExitBtn Clicked");
                break;
        }
    }
}
