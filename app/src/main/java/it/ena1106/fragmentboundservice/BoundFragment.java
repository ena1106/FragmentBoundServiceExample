package it.ena1106.fragmentboundservice;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class BoundFragment extends Fragment {

    private MyService boundService;
    private boolean isBound;

    private TextView tvConnected;
    private TextView tvCounter;
    private Button btnConnect;
    private Button btnRefresh;
    private Button btnIncrement;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        tvConnected = (TextView) view.findViewById(R.id.txtConnected);
        tvCounter = (TextView) view.findViewById(R.id.txtCounter);
        btnConnect = (Button) view.findViewById(R.id.btnConnect);
        btnRefresh = (Button) view.findViewById(R.id.btnRefresh);
        btnIncrement = (Button) view.findViewById(R.id.btnIncrement);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound) {
                    doUnbindService();
                    refresh();
                } else {
                    doBindService();
                }
            }
        });

        btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boundService.increment();
                refresh();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        if (isBound) {
            tvConnected.setText("CONNECTED");
            tvCounter.setText("" + boundService.getCounter());
            btnConnect.setText("DISCONNECT");
            btnIncrement.setVisibility(View.VISIBLE);
        } else {
            tvConnected.setText("NOT CONNECTED");
            btnConnect.setText("CONNECT");
            tvCounter.setText("N/A");
            btnIncrement.setVisibility(View.INVISIBLE);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            boundService = ((MyService.MyBinder) service).getService();
            refresh();
        }

        public void onServiceDisconnected(ComponentName className) {
            boundService = null;
            isBound = false;
            refresh();
        }
    };

    void doBindService() {
        getActivity().bindService(new Intent(getActivity(),
                MyService.class), mConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    void doUnbindService() {
        if (isBound) {
            // Detach our existing connection.
            getActivity().unbindService(mConnection);
            isBound = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
}
