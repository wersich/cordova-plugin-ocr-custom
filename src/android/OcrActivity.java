package at.ventocom.liveocr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;

import edu.sfsu.cs.orange.ocr.CaptureActivity;
import edu.sfsu.cs.orange.ocr.PreferencesActivity;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OcrActivity extends CaptureActivity {

    public final static String EXTRA_SCANNED_VALUE = "scannedValue";
    private final static String TAG = OcrActivity.class.getSimpleName();
	private int countScannedValue = 0;
	private Set<String> scannedValues = new HashSet<String>();
    private String regex = "";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        CONTINUOUS_DISPLAY_METADATA = false;
        CONTINUOUS_DISPLAY_RECOGNIZED_TEXT = false;
        DEFAULT_DISABLE_CONTINUOUS_FOCUS = false;
        DISPLAY_SHUTTER_BUTTON = false;
        MUTE_ALL_DIALOGES = true;
        regex = getIntent().getStringExtra(LiveOcrPlugin.REGEX);
    }

    @Override
    protected boolean isEnforcedDefaultPreferences() {
        return true;
    }

    @Override
    protected void setDefaultPreferences() {
        super.setDefaultPreferences();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
        // prefs.edit().putString(OcrCharacterHelper.KEY_CHARACTER_WHITELIST_PORTUGUESE, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz*:/.-").apply();
        prefs.edit().putString(PreferencesActivity.KEY_SOURCE_LANGUAGE_PREFERENCE, "por").apply();
        prefs.edit().putBoolean(PreferencesActivity.KEY_CONTINUOUS_PREVIEW, true).apply();
    }

    @Override
    protected void detectedTextContinously(String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()){
            String match = matcher.group(0);
            String readValue = match;
            // TextView scannedTextView = cordova.getActivity().findViewById(R.id.help_view_bottom);
            // scannedTextView.setText(match);
            if(scannedValues.contains(readValue)){
                countScannedValue++;
            }else{
                scannedValues.add(readValue);
            }
            if(countScannedValue == 1){
                Intent intent = new Intent();
                intent.putExtra(EXTRA_SCANNED_VALUE, readValue);
                // return the digits to the calling activity, which will receive it and put it into the form field.
                setResult(1, intent);
                finish();
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected boolean checkFirstLaunch() {
        return true;
    }

    @SuppressWarnings("unused")
    public void abort(View view) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SCANNED_VALUE, "abort");
        setResult(1, intent);
        finish();
    }
}