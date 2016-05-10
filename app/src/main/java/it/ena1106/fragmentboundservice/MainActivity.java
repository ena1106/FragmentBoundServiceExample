package it.ena1106.fragmentboundservice;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button swapBtn = (Button) findViewById(R.id.btnSwap);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.top_right, new BoundFragment());
        ft.add(R.id.top_left, new BoundFragment());
        ft.add(R.id.bottom_right, new BoundFragment());
        ft.add(R.id.bottom_left, new BoundFragment());
        ft.commit();

        doBindService();

        /**
         * Replace the first fragment of the grid with a new one.
         */
        swapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.top_left, new BoundFragment());
                ft.commit();
            }
        });
    }

    private MyService boundService;
    private boolean isBound;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            boundService = ((MyService.MyBinder) service).getService();


        }

        public void onServiceDisconnected(ComponentName className) {
            boundService = null;
        }
    };

    void doBindService() {
        bindService(new Intent(MainActivity.this,
                MyService.class), mConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    void doUnbindService() {
        if (isBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            isBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }


}
