package de.odinoxin.galaxylockwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioButton startTop = (RadioButton) this.findViewById(R.id.startTop);
        startTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setGravity(R.layout.widgetlayout_top_start);
                }
            }
        });

        RadioButton centerTop = (RadioButton) this.findViewById(R.id.centerTop);
        centerTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setGravity(R.layout.widgetlayout_top_center);
                }
            }
        });

        RadioButton endTop = (RadioButton) this.findViewById(R.id.endTop);
        endTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setGravity(R.layout.widgetlayout_top_end);
                }
            }
        });

        RadioButton startCenter = (RadioButton) this.findViewById(R.id.startCenter);
        startCenter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setGravity(R.layout.widgetlayout_center_start);
                }
            }
        });

        RadioButton centerCenter = (RadioButton) this.findViewById(R.id.centerCenter);
        centerCenter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setGravity(R.layout.widgetlayout_center_center);
                }
            }
        });

        RadioButton endCenter = (RadioButton) this.findViewById(R.id.endCenter);
        endCenter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setGravity(R.layout.widgetlayout_center_end);
                }
            }
        });

        RadioButton startBottom = (RadioButton) this.findViewById(R.id.startBottom);
        startBottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setGravity(R.layout.widgetlayout_bottom_start);
                }
            }
        });

        RadioButton centerBottom = (RadioButton) this.findViewById(R.id.centerBottom);
        centerBottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setGravity(R.layout.widgetlayout_bottom_center);
                }
            }
        });

        RadioButton endBottom = (RadioButton) this.findViewById(R.id.endBottom);
        endBottom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setGravity(R.layout.widgetlayout_bottom_end);
                }
            }
        });
    }

    private void setGravity(int gravity) {
        SharedPreferences prefs = this.getSharedPreferences(GalaxyLockWidgetProvider.PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(GalaxyLockWidgetProvider.KEY_GRAVITY, gravity).apply();

        //Close Activity, if it was launched for configuration.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            int galaxyLockWidgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (galaxyLockWidgetID != AppWidgetManager.INVALID_APPWIDGET_ID) {
                Intent result = new Intent();
                result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, galaxyLockWidgetID);
                setResult(RESULT_OK, result);
                finish();
            }
        }
    }
}
