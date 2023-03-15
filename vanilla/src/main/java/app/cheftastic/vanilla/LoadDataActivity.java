package app.cheftastic.vanilla;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import app.cheftastic.R;

public class LoadDataActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_load_data);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }
}
