package com.plracticalcoding.asyncTaskClass;


import android.os.AsyncTask;

class SequantialTasks extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        // Simulate work
        //Log.d("AsyncTask", "First Task Started");
        return null;
    }
//
//    @Override
//    protected void onPostExecute(Void result) {
//        // Start second task after first is completed
//        new SecondTask().execute();
//    }
//}

//private class SecondTask extends AsyncTask<Void, Void, Void> {
//    @Override
//    protected Void doInBackground(Void... voids) {
//        // Simulate work
//        Log.d("AsyncTask", "Second Task Started");
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void result) {
//        // Start third task after second is completed
//        new ThirdTask().execute();
//    }
//}
//
//private class ThirdTask extends AsyncTask<Void, Void, Void> {
//    @Override
//    protected Void doInBackground(Void... voids) {
//        // Simulate work
//        Log.d("AsyncTask", "Third Task Started");
//        return null;
//    }
}

