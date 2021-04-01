package bersey.henry.screentimeouttile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AboutActivity extends AppCompatActivity {

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle(getString(R.string.about));

        TextView versionTextView = findViewById(R.id.versionTextView);
        versionTextView.setText(String.format("v%s (%d)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));

        TextView translatorsTextView = findViewById(R.id.translatorsTextView);
        String translatorsText = IntStream.range(0, getResources().getStringArray(R.array.language_codes).length).mapToObj(i -> {
            String code = getResources().getStringArray(R.array.language_codes)[i];
            String language = getString(getResources().getIdentifier("language_" + code, "string", getPackageName()));
            String translator = getResources().getStringArray(R.array.translators)[i];
            if (translator.equals(""))
                return "";
            return language + ": " + translator;
        }).filter(s -> !s.equals("")).collect(Collectors.joining("\n"));
        translatorsTextView.setText(translatorsText);
    }
}