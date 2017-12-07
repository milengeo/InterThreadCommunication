/*
 # Inter Thread Communication
 Android example of lock-free inter-thread communication.

 # The problem
 There are several ways for communicating between threads in Android platform.
 Most of them use synchronization locks, which introduce performance penalties.
 In many cases, the ANRs (Aplication Not Responding) causes are related to locking waits.

 # The solution
 This sample shows how to communicate between two threads without using syncronizations locks.
 That is done by utilizing the Androic specific classes Handler and Looper.
 Thye sending thread creates a POJO object with desired data and posts a Runnable with it to
 the target thread Handler queue. And the response is sent by the same way in the oppite direction.

 # The result
 We can achieve two-way inter-thread communication without the performance penalty of the synchronization locks.

 Author: Milen Georgiev  https://github.com/milengeo
 */


package org.mg;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mJobView;
    private ScrollView mJobScroll;
    private int mJobCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.app_name));

        mJobView = (TextView) findViewById(R.id.job_view);
        mJobScroll = (ScrollView) findViewById(R.id.job_scroll);

        Worker.create();
        Worker.getInstance().setCallback(new WorkerCallback());

        say("Ready... Please click the button to post a job.");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Worker.delete();
    }


    public void say(String aLine) {
        mJobView.append("\n" + aLine);
        mJobScroll.fullScroll(View.FOCUS_DOWN);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_post_job) {
            postJob();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void postJob() {
        mJobCount++;
        say("Posting job #: " + mJobCount);
        Job job = new Job();
        job.id = mJobCount;
        job.first = mJobCount;
        job.second = mJobCount+1;
        Worker.getInstance().requestJob(job);
    }


    private class WorkerCallback implements Worker.CallbackI {

        public void onJobDone(final Job aJob) {
            Log.d(TAG, "onJobDone-Out: " + aJob.id);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onJobDone-In: " + aJob.id);
                    say("Job #: " + aJob.id + " is done, result: " + aJob.result);
                }
            });
        }
    }

}