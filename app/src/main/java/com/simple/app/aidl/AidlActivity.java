package com.simple.app.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.simple.app.IMyAidlInterface;

/**
 * Created by hych on 2017/11/15.
 */

public class AidlActivity extends AppCompatActivity {

    private IMyAidlInterface mRemoteInterface;
    private MyRemoteServiceConnection mRemoteConn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRemoteConn = new MyRemoteServiceConnection();
        Intent intent = new Intent(AidlActivity.this, MyAidlService.class);
        //绑定
        bindService(intent, mRemoteConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mRemoteConn);

    }

    public class MyRemoteServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteInterface = IMyAidlInterface.Stub.asInterface(service);
            if (mRemoteInterface != null) {
                try {
                    mRemoteInterface.pay(123);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
