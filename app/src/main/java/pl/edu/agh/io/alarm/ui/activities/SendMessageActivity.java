package pl.edu.agh.io.alarm.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import pl.edu.agh.io.alarm.R;
import pl.edu.agh.io.alarm.ui.UI;

/**
 * Created by Mateusz on 2016-04-21.
 */
public class SendMessageActivity extends Activity implements View.OnClickListener {
    private UI ui;


    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_sendmessage);
        Button send = (Button) findViewById(R.id.SENDMESSAGE_sendbtn);
        send.setOnClickListener(this);
        ImageButton imageButton = (ImageButton) findViewById(R.id.SENDMESSAGE_exitbtn);
        imageButton.setOnClickListener(this);

        final ListView listView = (ListView) findViewById(R.id.SENDMESSAGE_FriendListView);
        String string[] = { "1", "2" , "3", "4"};
        ArrayList<String> list = new ArrayList();
        list.addAll(Arrays.asList(string));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_view_content, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("------------------------");
                System.out.println(parent.toString());
                System.out.println(view.toString());
                System.out.println(position);
                System.out.println(id);

                System.out.println(listView.getItemAtPosition(position).toString());
                System.out.println("-------------------------");
            }
        });
        System.out.println("Created");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.SENDMESSAGE_sendbtn:
                EditText editText = (EditText) findViewById(R.id.SENDMESSAGE_msgtext);
                if(editText.getText() != null){
                    //TODO: Send to Communicate Module Friend and Text, then change to MainPageActivity
                    System.out.println(editText.getText().toString());
                    Intent intent = new Intent(this, AddFriendActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.SENDMESSAGE_exitbtn:
                System.out.println("ExitBtn Clicked");
                break;
        }
    }
}
