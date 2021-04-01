package bersey.henry.screentimeouttile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner languageSpinner = findViewById(R.id.languageSpinner);
        List<String> languages = Arrays.stream(
                getResources().getStringArray(R.array.language_codes))
                .map(code -> getString(getResources().getIdentifier("language_" + code, "string", getPackageName())))
                .collect(Collectors.toList());
        languages.add(0, "(" + getString(R.string.auto) + ")");
        ArrayAdapter<String> languageSpinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, languages);
        languageSpinner.setAdapter(languageSpinnerAdapter);

        RecyclerView timeoutsRecyclerView = findViewById(R.id.timeoutsRecyclerView);
        timeoutsRecyclerView.setAdapter(new TimeoutsRecyclerAdapter());
        timeoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Spinner newUnitSpinner = findViewById(R.id.newTimeoutUnitSpinner);
        List<String> units = Arrays.stream(Timeout.Unit.values()).map(unit -> unit.getShorthand(getResources())).collect(Collectors.toList());
        ArrayAdapter<String> newUnitAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, units);
        newUnitSpinner.setAdapter(newUnitAdapter);

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