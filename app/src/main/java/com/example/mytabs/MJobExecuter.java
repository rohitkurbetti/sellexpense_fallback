package com.example.mytabs;

import android.os.AsyncTask;

public class MJobExecuter extends AsyncTask<Void,Void,String> {
    @Override
    protected String doInBackground(Void... voids) {
        return "Bakcground long running taks fnished";
    }
}
