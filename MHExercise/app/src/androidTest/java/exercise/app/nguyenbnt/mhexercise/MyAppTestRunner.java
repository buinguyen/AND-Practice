package exercise.app.nguyenbnt.mhexercise;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;
import android.util.Log;

import io.appflate.restmock.RESTMockServerStarter;
import io.appflate.restmock.android.AndroidAssetsFileParser;
import io.appflate.restmock.android.AndroidLogger;

public class MyAppTestRunner extends AndroidJUnitRunner {
    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
        Log.i("MyAppTestRunner", "MyAppTestRunner onCreate()");
        RESTMockServerStarter.startSync(new AndroidAssetsFileParser(getContext()),
                new AndroidLogger());
    }
}