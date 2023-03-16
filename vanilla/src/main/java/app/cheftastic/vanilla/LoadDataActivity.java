package app.cheftastic.vanilla;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import app.cheftastic.R;

public class LoadDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_load_data);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }
}
