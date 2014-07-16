
package com.example.mjmtools;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }

        startMainService(Keys.actionNoop);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            String versionName;
            try {
                versionName = getActivity().getPackageManager().getPackageInfo(
                        getActivity().getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                versionName = getString(R.string.unknownVersionName);
            }
            String str = getString(R.string.mainLayoutAboutText, versionName);
            TextView tv = (TextView) rootView.findViewById(R.id.mainLayoutAboutText);
            tv.setText(str);
            return rootView;
        }
    }

    private void startMainService(String action, int delay) {
        Context context = getApplicationContext();
        Intent mjmService = new Intent(context, MainService.class);
        mjmService.putExtra(Keys.action, action);
        mjmService.putExtra(Keys.delay, delay);
        context.startService(mjmService);
    }

    private void startMainService(String action) {
        startMainService(action, 0);
    }

    public void onMainLayoutSettingsButtonClicked(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
