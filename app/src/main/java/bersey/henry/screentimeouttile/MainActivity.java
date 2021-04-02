package bersey.henry.screentimeouttile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import bersey.henry.screentimeouttile.utils.LocaleUtils;
import bersey.henry.screentimeouttile.utils.NotificationUtils;

import static bersey.henry.screentimeouttile.utils.LocaleUtils.setLanguage;
import static bersey.henry.screentimeouttile.utils.LocaleUtils.updateLanguage;

public class MainActivity extends AppCompatActivity {

    boolean languageSpinnerLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TimeoutManager timeoutManager = TimeoutManager.getInstance(preferences);

        updateLanguage(preferences, this);

        setContentView(R.layout.activity_main);

        Spinner languageSpinner = findViewById(R.id.languageSpinner);
        List<String> languages = Arrays.stream(
                getResources().getStringArray(R.array.language_codes))
                .map(code -> getString(getResources().getIdentifier("language_" + code, "string", getPackageName())))
                .collect(Collectors.toList());
        languages.add(0, "(" + getString(R.string.auto) + ")");
        ArrayAdapter<String> languageSpinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, languages);
        languageSpinner.setAdapter(languageSpinnerAdapter);
        String langCode = LocaleUtils.getLangCode(preferences);
        languageSpinner.setSelection(langCode == null ? 0 : Arrays.asList(getResources().getStringArray(R.array.language_codes)).indexOf(langCode) + 1);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!languageSpinnerLoaded) {
                    languageSpinnerLoaded = true;
                    return;
                }

                String langCode = position == 0 ? null : getResources().getStringArray(R.array.language_codes)[position - 1];
                setLanguage(preferences, langCode);
                updateLanguage(preferences, getApplicationContext());
                finish();
                startActivity(getIntent());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RecyclerView timeoutsRecyclerView = findViewById(R.id.timeoutsRecyclerView);
        TimeoutsRecyclerAdapter timeoutsRecyclerAdapter = new TimeoutsRecyclerAdapter(timeoutManager);
        timeoutsRecyclerView.setAdapter(timeoutsRecyclerAdapter);
        timeoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        EditText newTimeoutEditText = findViewById(R.id.newTimeoutAmount);

        Spinner newUnitSpinner = findViewById(R.id.newTimeoutUnitSpinner);
        List<String> units = Arrays.stream(Timeout.Unit.values()).map(unit -> unit.getShorthand(getResources())).collect(Collectors.toList());
        ArrayAdapter<String> newUnitAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, units);
        newUnitSpinner.setAdapter(newUnitAdapter);

        ImageButton newButton = findViewById(R.id.newTimeoutButton);
        newButton.setOnClickListener(v -> {
            int amount = Integer.parseInt(newTimeoutEditText.getText().toString());
            Timeout.Unit unit = Timeout.Unit.values()[newUnitSpinner.getSelectedItemPosition()];
            Timeout timeout = new Timeout(amount, unit);
            if (!timeoutManager.alreadyExists(timeout)) {
                int i = timeoutManager.add(timeout);
                timeoutsRecyclerAdapter.notifyItemInserted(i);
            }
        });

        Switch notificationSwitch = findViewById(R.id.notificationSwitch);
        notificationSwitch.setChecked(preferences.getBoolean(NotificationUtils.PREFS_KEY, true));
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                preferences.edit().putBoolean(NotificationUtils.PREFS_KEY, isChecked).apply());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id != R.id.aboutItem)
            return super.onOptionsItemSelected(item);

        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);

        return true;
    }
}