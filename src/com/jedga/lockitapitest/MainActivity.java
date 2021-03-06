package com.jedga.lockitapitest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    public final static String ACCESS_GRANTED = "com.jedga.lockit.ACCESS_GRANTED";
    public final static String ACCESS_DENIED = "com.jedga.lockit.ACCESS_DENIED";
    public final static String RANDOM_KEYBOARD = "random_keyboard";
    public final static String TIMEOUT = "timeout";
    public final static String PASSWORD = "password";

    private LockItReceiver mLockItReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mLockItReceiver = new LockItReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACCESS_GRANTED);
        filter.addAction(ACCESS_DENIED);

        registerReceiver(mLockItReceiver, filter);

        final EditText password = (EditText) findViewById(R.id.password_text);
        final EditText timeout = (EditText) findViewById(R.id.timeout_text);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(ComponentName.unflattenFromString("com.jedga.lockit/.security.PasswordActivity"));
                String psw = password.getText().toString();
                String tout = timeout.getText().toString();
                intent.putExtra(PASSWORD, psw.isEmpty() ? "123456" : psw);
                intent.putExtra(TIMEOUT, tout.isEmpty() ? 15 : Integer.parseInt(tout));
                intent.putExtra(RANDOM_KEYBOARD, true);
                startActivity(intent);
            }
        });
    }

    private static class LockItReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ACCESS_GRANTED)) {
                Toast.makeText(context, R.string.pass, Toast.LENGTH_LONG).show();
            } else if(intent.getAction().equals(ACCESS_DENIED)) {
                Toast.makeText(context, R.string.fail, Toast.LENGTH_LONG).show();
            }
        }
    }
}
