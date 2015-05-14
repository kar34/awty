package kar34.washington.edu.awty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String message = intent.getStringExtra("message");
        String number = intent.getStringExtra("number");

        Toast.makeText(context, number + ": " + message , Toast.LENGTH_SHORT).show();
    }

}
