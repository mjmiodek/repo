
package com.example.mjmtools;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends ActionBarActivity {

    private TextView mPowerOptionsTextview;
    private LinearLayout mPowerOptionsLayout;
    private CheckBox mPowerConnectedWifiCheckbox;
    private CheckBox mPowerConnectedMobiledataCheckbox;
    private EditText mPowerDisconnectedPeriodicTimeEdittext;
    private CheckBox mPowerDisconnectedWifiCheckbox;
    private CheckBox mPowerDisconnectedMobiledataCheckbox;

    private TextView mPowerOffOptionsTextview;
    private LinearLayout mPowerOffOptionsLayout;
    private EditText mPowerOffPeriodicTimeEdittext;
    private CheckBox mPowerOffWifiCheckbox;
    private CheckBox mPowerOffMobiledataCheckbox;

    private TextView mOtherOptionsTextview;
    private LinearLayout mOtherOptionsLayout;
    private CheckBox mUseLogFileCheckbox;
    private LinearLayout mLogFileNameLayout;
    private EditText mLogFileNameEdittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mPowerOptionsTextview == null) {
            mPowerOptionsTextview = (TextView) findViewById(R.id.powerOptionsTextview);
            mPowerOptionsTextview.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return onPowerOptionsTextviewLongClick(view);
                }
            });
        }

        if (mPowerOptionsLayout == null)
            mPowerOptionsLayout = (LinearLayout) findViewById(R.id.powerOptionsLayout);
        mPowerOptionsLayout.setVisibility(View.GONE);

        if (mPowerOffOptionsTextview == null) {
            mPowerOffOptionsTextview = (TextView) findViewById(R.id.powerOffOptionsTextview);
            mPowerOffOptionsTextview.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return onPowerOffOptionsTextviewLongClick(view);
                }
            });
        }

        if (mOtherOptionsTextview == null) {
            mOtherOptionsTextview = (TextView) findViewById(R.id.otherOptionsTextview);
            mOtherOptionsTextview.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return onOtherOptionsTextviewLongClick(view);
                }
            });
        }

        if (mPowerConnectedWifiCheckbox == null)
            mPowerConnectedWifiCheckbox = (CheckBox) findViewById(R.id.powerConnectedWifiCheckbox);

        if (mPowerConnectedMobiledataCheckbox == null)
            mPowerConnectedMobiledataCheckbox = (CheckBox) findViewById(R.id.powerConnectedMobiledataCheckbox);

        if (mPowerDisconnectedPeriodicTimeEdittext == null)
            mPowerDisconnectedPeriodicTimeEdittext = (EditText) findViewById(R.id.powerDisconnectedPeriodicTimeEdittext);
        Tools.setEditTextEditionFinishedAction(mPowerDisconnectedPeriodicTimeEdittext,
                new RunnableWithString() {
                    @Override
                    public void run() {
                        onPowerDisconnectedPeriodicTimeEdittextValueChanged(mParam);
                    }
                });

        if (mPowerDisconnectedWifiCheckbox == null)
            mPowerDisconnectedWifiCheckbox = (CheckBox) findViewById(R.id.powerDisconnectedWifiCheckbox);

        if (mPowerDisconnectedMobiledataCheckbox == null)
            mPowerDisconnectedMobiledataCheckbox = (CheckBox) findViewById(R.id.powerDisconnectedMobiledataCheckbox);

        if (mPowerOffOptionsLayout == null)
            mPowerOffOptionsLayout = (LinearLayout) findViewById(R.id.powerOffOptionsLayout);
        mPowerOffOptionsLayout.setVisibility(View.GONE);

        if (mPowerOffPeriodicTimeEdittext == null)
            mPowerOffPeriodicTimeEdittext = (EditText) findViewById(R.id.powerOffPeriodicTimeEdittext);
        Tools.setEditTextEditionFinishedAction(mPowerOffPeriodicTimeEdittext,
                new RunnableWithString() {
                    @Override
                    public void run() {
                        onPowerOffPeriodicTimeEdittextValueChanged(mParam);
                    }
                });

        if (mPowerOffWifiCheckbox == null)
            mPowerOffWifiCheckbox = (CheckBox) findViewById(R.id.powerOffWifiCheckbox);

        if (mPowerOffMobiledataCheckbox == null)
            mPowerOffMobiledataCheckbox = (CheckBox) findViewById(R.id.powerOffMobiledataCheckbox);

        if (mOtherOptionsLayout == null)
            mOtherOptionsLayout = (LinearLayout) findViewById(R.id.otherOptionsLayout);
        mOtherOptionsLayout.setVisibility(View.GONE);

        if (mUseLogFileCheckbox == null)
            mUseLogFileCheckbox = (CheckBox) findViewById(R.id.useLogFileCheckbox);

        if (mLogFileNameLayout == null)
            mLogFileNameLayout = (LinearLayout) findViewById(R.id.logFileNameLayout);

        if (mLogFileNameEdittext == null) {
            mLogFileNameEdittext = (EditText) findViewById(R.id.logFileNameExitbox);
            Tools.setEditTextEditionFinishedAction(mLogFileNameEdittext, new RunnableWithString() {
                @Override
                public void run() {
                    onLogFileNameEdittextValueChanged(mParam);
                }
            });
        }

        // fill initial field state

        mPowerConnectedWifiCheckbox.setChecked(MJMToolsApplication.getPowerConnectedEnableWifi());
        mPowerConnectedMobiledataCheckbox.setChecked(MJMToolsApplication
                .getPowerConnectedEnableMobiledata());
        mPowerDisconnectedPeriodicTimeEdittext.setText(String.valueOf(MJMToolsApplication
                .getPowerDisconnectedPeriodicTime()));
        mPowerDisconnectedWifiCheckbox.setChecked(MJMToolsApplication
                .getPowerDisconnectedDisableWifi());
        mPowerDisconnectedMobiledataCheckbox.setChecked(MJMToolsApplication
                .getPowerDisconnectedDisableMobiledata());
        mPowerOffPeriodicTimeEdittext.setText(String.valueOf(MJMToolsApplication
                .getPowerOffPeriodicTime()));
        mPowerOffWifiCheckbox.setChecked(MJMToolsApplication.getPowerOffDisableWifi());
        mPowerOffMobiledataCheckbox.setChecked(MJMToolsApplication.getPowerOffDisableMobiledata());
        mUseLogFileCheckbox.setChecked(MJMToolsApplication.getUseLogFile());
        mLogFileNameLayout
                .setVisibility(mUseLogFileCheckbox.isChecked() ? View.VISIBLE : View.GONE);
        mLogFileNameEdittext.setText(MJMToolsApplication.getLogFileName());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
            return rootView;
        }
    }

    //////////////////////////////////
    // control actions

    public void onPowerOptionsTextviewClick(View view) {
        boolean visible = mPowerOptionsLayout.getVisibility() == View.VISIBLE;
        mPowerOptionsLayout.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    public void onPowerConnectedWifiCheckboxClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        MJMToolsApplication.setPowerConnectedEnableWifi(checked);
    }

    public boolean onPowerOptionsTextviewLongClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.powerOptionsCaption);
        builder.setMessage(R.string.powerOptionsExplanation);
        AlertDialog dialog = builder.create();
        dialog.show();
        //Toast.makeText(getApplicationContext(), "onPowerOptionsTextviewLongClick",
        //Toast.LENGTH_SHORT).show();
        return true;
    }

    public void onPowerConnectedMobiledataCheckboxClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        MJMToolsApplication.setPowerConnectedEnableMobiledata(checked);
    }

    public void onPowerDisconnectedPeriodicTimeEdittextValueChanged(String periodicTime) {
        MJMToolsApplication.setPowerDisconnectedPeriodicTime(Long.parseLong(periodicTime));
    }

    public void onPowerDisconnectedWifiCheckboxClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        MJMToolsApplication.setPowerDisconnectedEnableWifi(checked);
    }

    public void onPowerDisconnectedMobiledataCheckboxClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        MJMToolsApplication.setPowerDisconnectedDisableMobiledata(checked);
    }

    public void onPowerOffOptionsTextviewClick(View view) {
        boolean visible = mPowerOffOptionsLayout.getVisibility() == View.VISIBLE;
        mPowerOffOptionsLayout.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    public boolean onPowerOffOptionsTextviewLongClick(View view) {
        Toast.makeText(getApplicationContext(), "onPowerOffOptionsTextviewLongClick",
                Toast.LENGTH_SHORT).show();
        return true;
    }

    public void onPowerOffPeriodicTimeEdittextValueChanged(String periodicTime) {
        MJMToolsApplication.setPowerOffPeriodicTime(Long.parseLong(periodicTime));
    }

    public void onPowerOffWifiCheckboxClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        MJMToolsApplication.setPowerOffDisableWifi(checked);
    }

    public void onPowerOffMobiledataCheckboxClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        MJMToolsApplication.setPowerOffDisableMobiledata(checked);
    }

    public void onOtherOptionsTextviewClick(View view) {
        boolean visible = mOtherOptionsLayout.getVisibility() == View.VISIBLE;
        mOtherOptionsLayout.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    public boolean onOtherOptionsTextviewLongClick(View view) {
        Toast.makeText(getApplicationContext(), "onOtherOptionsTextviewLongClick",
                Toast.LENGTH_SHORT).show();
        return true;
    }

    public void onUseLogFileCheckboxClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        MJMToolsApplication.setUseLogFile(checked);
        mLogFileNameLayout.setVisibility(checked ? View.VISIBLE : View.GONE);
    }

    public void onLogFileNameEdittextValueChanged(String fileName) {
        MJMToolsApplication.setLogFileName(fileName);
    }

    public void onLogFileOpenButtonClick(View view) {
        MJMLog.openLogFile(this);
    }

    public void onLogFileSendButtonClick(View view) {
        Toast.makeText(getApplicationContext(), getString(R.string.unimplementedOption),
                Toast.LENGTH_LONG).show();
    }

    public void onLogFileClearButtonClick(View view) {
        MJMLog.clearLogFile();
    }
}
