package com.example.louder2.Fragment;

import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.louder2.R;

import java.util.List;

public class FragSetting extends PreferenceFragmentCompat {
    private View view;
    private String TAG = "프래그먼트";

    SharedPreferences prefs;

    Preference soundPreference;
    Preference keywordSoundPreference;
    Preference message;
    Preference sound;
    Preference vibrate;

    @Nullable
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Log.i(TAG, "onCreateView");

        addPreferencesFromResource(R.xml.settings_preference);
        soundPreference =  findPreference("sound_list");
        keywordSoundPreference =  findPreference("keyword_sound_list");
        message =  findPreference("message");
        sound =  findPreference("sound");
        vibrate =  findPreference("vibrate");

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(!prefs.getString("sound_list", "").equals("")){
            soundPreference.setSummary(prefs.getString("sound_list", "기본음"));
        }


        //선택된 값으로 바꿔주기
        prefs.registerOnSharedPreferenceChangeListener(prefListener);


    }
    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals("sound_list")){
                soundPreference.setSummary(prefs.getString("sound_list", "기본음"));
            }

            if(!prefs.getBoolean("message", false)){
                message.setSummary("사용 안함");
            } else{
                message.setSummary("사용");
            }

            if(!prefs.getBoolean("sound", false)){
                sound.setSummary("사용 안함");
            } else{
                sound.setSummary("사용");
            }

            if(!prefs.getBoolean("vibrate", false)){
                vibrate.setSummary("사용 안함");
            } else{
                vibrate.setSummary("사용");
            }

        }
        //환경설정 적용
        //((BaseAdapter)getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
    };

}
