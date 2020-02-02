package test.app.alan.mytest.localize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import test.app.alan.mytest.R;
import test.app.alan.mytest.localize.utils.LocaleUtils;


public class LocalizeActivity extends Activity {

    Button mChangeLocaleButton;
    Button mLoadActivityButton;

    private int mLanguageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localize);

        mChangeLocaleButton = findViewById(R.id.button_change_locale);
        mLoadActivityButton = findViewById(R.id.button_load_activity);

        LocaleUtils.initialize(this, LocaleUtils.ENGLISH);

        setupUi();
    }

    private void setupUi() {
        mChangeLocaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (++mLanguageIndex >= LocaleUtils.LocaleDef.SUPPORTED_LOCALES.length) {
                    mLanguageIndex = 0;
                }

                LocaleUtils.setLocale(getApplicationContext(), mLanguageIndex);
                updateUi();
            }
        });

        mLoadActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LocalizeActivity.this, AnotherActivity.class));
            }
        });
    }

    private void updateUi() {
        mChangeLocaleButton.setText(R.string.current_language);
        mLoadActivityButton.setText(R.string.open);
    }
}
