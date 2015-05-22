package kar34.washington.edu.awty;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.*;


public class MainActivity extends ActionBarActivity {

    private PendingIntent pIntent;
    private String num;
    private String message;
    private String interval;
    BroadcastReceiver br = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            SmsManager sms = SmsManager.getDefault();
            try {
                sms.sendTextMessage(num, null, message, null, null);
                Toast.makeText(context, "Message has been sent!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(context, "Message not sent", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button b = (Button) findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText numEdit = (EditText) findViewById(R.id.tele);
                num = numEdit.getText().toString();

                EditText messageEdit = (EditText) findViewById(R.id.message);
                message = messageEdit.getText().toString();

                EditText timeEdit = (EditText) findViewById(R.id.interval);
                interval = timeEdit.getText().toString();

                CharSequence buttonText = b.getText();

                registerReceiver(br, new IntentFilter("go"));
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                if (buttonText.equals("Stop")) {
                    b.setText("Start");
                    stop();
                } else if (num.isEmpty() || message.isEmpty() || interval.isEmpty())
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else if (num.length() != 10)
                    Toast.makeText(getApplicationContext(), "Invalid digit length", Toast.LENGTH_SHORT).show();
                else if (Integer.parseInt(interval) < 1)
                    Toast.makeText(getApplicationContext(), "Please pick a value higher than 0", Toast.LENGTH_SHORT).show();
                else if (buttonText.equals("Start")) {
                    b.setText("Stop");
                    start(am);
                }

            }
        });
    }

    public void start(AlarmManager am) {
        Intent i = new Intent();
        i.putExtra("number", num);
        i.putExtra("message", message);
        i.setAction("go");

        pIntent = PendingIntent.getBroadcast(MainActivity.this, 0, i, 0);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + Integer.parseInt(interval) * 1000, Integer.parseInt(interval) * 1000, pIntent);
    }

    public void stop() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
