package com.example.mytabs;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.widget.Toast;

public class MJobScheduler extends JobService {
    private MJobExecuter mJobExecuter;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mJobExecuter = new MJobExecuter(){
            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(MJobScheduler.this, s, Toast.LENGTH_SHORT).show();
                jobFinished(jobParameters,false);
            }
        };

        mJobExecuter.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobExecuter.cancel(true);
        return true;
    }
}
