package it.ena1106.fragmentboundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class MyService extends Service {

    private MyBinder binder = new MyBinder();

    private int counter;

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void increment(){
        counter++;
    }

    public int getCounter() {
        return counter;
    }
}
