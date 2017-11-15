package com.simple.app.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.simple.app.IMyAidlInterface;

/**
 * Created by hych on 2017/11/15.
 */

public class MyAidlService extends Service {

    public static final String TAG = MyAidlService.class.getSimpleName();


    public MyAidlService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class Interface extends IMyAidlInterface.Stub {


        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String pay(int pwd) throws RemoteException {
            if (pwd == 123) {
                return "支付成功";
            }
            return "支付失败";
        }
    }
}
