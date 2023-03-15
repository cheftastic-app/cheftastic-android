package app.cheftastic.vanilla;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.LinearLayout;

import app.cheftastic.R;

public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_information);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        LinearLayout contactInfo = (LinearLayout) findViewById(R.id.activity_information_send_email);
        if (contactInfo != null) {
            contactInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getResources().getString(R.string.email), null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Comentarios y sugerencias sobre 'Cheftastic'");
                    startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public boolean onNavigateUp() {
        return onSupportNavigateUp();
    }
}
