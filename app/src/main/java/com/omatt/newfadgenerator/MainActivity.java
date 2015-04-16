package com.omatt.newfadgenerator;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.omatt.newfadgenerator.analytics.AnalyticsMedium;
import com.omatt.newfadgenerator.lib.Animate;
import com.omatt.newfadgenerator.lib.GenLib;
import com.omatt.newfadgenerator.utils.GlobalValues;
import com.omatt.newfadgenerator.utils.ShareManager;

import java.util.List;
import java.util.Random;

import static android.view.View.OnClickListener;
import static com.omatt.newfadgenerator.analytics.AnalyticsMedium.TrackerName;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getSupportActionBar().hide();

        GenLib.generateHashKey(this);
        // Initialize Facebook SDK
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public void onStart() {
        // Get an Analytics tracker to report app starts &amp; uncaught
        // exceptions etc.
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        // Stop the analytics tracking
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void startScreenTrack(Application application, String TAG, TrackerName mTrackerName) {
        // Get tracker.
        Tracker track = ((AnalyticsMedium) application).getTracker(mTrackerName);
        track.enableAdvertisingIdCollection(true);
        // Set screen name.
        track.setScreenName(TAG);
        // Send a screen view.
        track.send(new HitBuilders.AppViewBuilder().build());
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements OnClickListener {
        private static final String TAG = "MainFragment";
        private static final String KEY_NEWFAD = "NEWFAD";
        private String[] firstWord, secondWord, thirdWord;
        private TextView mTextViewNewFad, mBtnGenerateNewfad, mBtnAbout;
        private ImageButton mBtnShare, mBtnShareFB, mBtnShareTwitter, mBtnShareSMS, mBtnShareEmail;
        private LinearLayout layoutShare;
        private ShareDialog mShareDialog;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            mShareDialog = new ShareDialog(this);

            startScreenTrack(getActivity().getApplication(), TAG, TrackerName.APP_TRACKER);

            firstWord = getResources().getStringArray(R.array.first_word);
            secondWord = getResources().getStringArray(R.array.second_word);
            thirdWord = getResources().getStringArray(R.array.third_word);

            mTextViewNewFad = (TextView) rootView.findViewById(R.id.tv_newfad);
            mBtnGenerateNewfad = (TextView) rootView.findViewById(R.id.btn_newfad_generate);
            mBtnGenerateNewfad.setOnClickListener(this);

            layoutShare = (LinearLayout) rootView.findViewById(R.id.layout_share);
            mBtnShare = (ImageButton) rootView.findViewById(R.id.btn_share);
            mBtnShareFB = (ImageButton) rootView.findViewById(R.id.btn_share_fb);
            mBtnShareTwitter = (ImageButton) rootView.findViewById(R.id.btn_share_twitter);
            mBtnShareSMS = (ImageButton) rootView.findViewById(R.id.btn_share_sms);
            mBtnShareEmail = (ImageButton) rootView.findViewById(R.id.btn_share_email);
            mBtnShare.setOnClickListener(this);
            mBtnShareFB.setOnClickListener(this);
            mBtnShareTwitter.setOnClickListener(this);
            mBtnShareSMS.setOnClickListener(this);
            mBtnShareEmail.setOnClickListener(this);

            mBtnAbout = (TextView) rootView.findViewById(R.id.btn_about);
            mBtnAbout.setOnClickListener(this);

            if (savedInstanceState != null)
                mTextViewNewFad.setText(savedInstanceState.getString(KEY_NEWFAD));
            else mTextViewNewFad.setText(generateNewFad());

            return rootView;
        }

        @Override
        public void onSaveInstanceState(Bundle savedInstanceState) {
            super.onSaveInstanceState(savedInstanceState);
            savedInstanceState.putString(KEY_NEWFAD, mTextViewNewFad.getText().toString());
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_newfad_generate:
                    mTextViewNewFad.setText(generateNewFad());
                    break;
                case R.id.btn_about:
                    showAbout();
                    break;
                case R.id.btn_share:
                    Animate.fadeInLayout(layoutShare, layoutShare.getVisibility() == View.VISIBLE);
                    break;
                case R.id.btn_share_fb:
                    ShareManager.shareFacebookNewfad(getActivity(), mShareDialog, generateShareMessage(mTextViewNewFad.getText().toString()));
                    break;
                case R.id.btn_share_twitter:
                    ShareManager.shareTweetNewfad(getActivity(), generateShareMessage(mTextViewNewFad.getText().toString()));
                    break;
                case R.id.btn_share_sms:
                    ShareManager.shareSMSNewfad(getActivity(), generateShareMessage(mTextViewNewFad.getText().toString()));
                    break;
                case R.id.btn_share_email:
                    ShareManager.shareEmailNewfad(getActivity(), generateShareMessage(mTextViewNewFad.getText().toString()));
                    break;
            }
        }

        private String generateNewFad() {
            String newFad;
            Random generator = new Random();
            int randGen1 = generator.nextInt(firstWord.length);
            int randGen2 = generator.nextInt(secondWord.length);
            int randGen3 = generator.nextInt(thirdWord.length);

            newFad = firstWord[randGen1] + " " + secondWord[randGen2] + " " + thirdWord[randGen3];

            return newFad;
        }

        private String generateShareMessage(String newfad) {
            return getResources().getString(R.string.txt_share_phrase_1) + " " + newfad + " "
                    + getResources().getString(R.string.txt_share_phrase_2);
        }

        private void showAbout() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getActivity().getResources().getString(R.string.app_name))
                    .setMessage(getActivity().getResources().getString(R.string.txt_about))
                    .setCancelable(false)
                    .setNegativeButton(getActivity().getResources().getString(R.string.txt_about_close), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
