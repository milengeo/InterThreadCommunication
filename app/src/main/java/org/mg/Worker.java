
package org.mg;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


public class Worker extends Thread {

    public interface CallbackI {
        void onJobDone(Job aJob);
    }

    private static final String TAG = "Worker";
    private static Worker self;
    private Handler mHandler;
    private CallbackI mCallback;


    public static void create() {
        if (null == self) {
            self = new Worker();
            self.start();
        }
    }

    public static void delete() {
        if (null == self) return;
        self.mHandler.getLooper().quit();
        self = null;
    }

    public static Worker getInstance() {
        return self;
    }


    private Worker() {
        super("Worker");
    }


    public void setCallback(CallbackI aCallback) {
        mCallback = aCallback;
    }



    @Override
    public void run() {
        try {
            Looper.prepare();
            mHandler = new Handler();
            Looper.loop();
        } catch (Exception ex) {
            Log.d(TAG, "ex: " + ex.getMessage());
        }
    }





    public void requestJob(final Job aJob) {
        Log.d(TAG, "requestJob-Out: " + aJob.id);
        if (null == mCallback) return;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "requestJob-In: " + aJob.id);
                aJob.result = aJob.first + aJob.second;
                mCallback.onJobDone(aJob);
            }
        });
    }




}
