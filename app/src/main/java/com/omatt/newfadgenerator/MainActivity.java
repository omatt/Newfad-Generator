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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.omatt.newfadgenerator.analytics.AnalyticsMedium;
import com.omatt.newfadgenerator.lib.GenLib;

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

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
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
    public static class PlaceholderFragment extends Fragment {
        private static final String TAG = "MainFragment";
        private String[] firstWord, secondWord, thirdWord;
        private TextView mTextViewNewFad, mBtnGenerateNewfad, mBtnTweet, mBtnAbout;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            startScreenTrack(getActivity().getApplication(), TAG, TrackerName.APP_TRACKER);

            firstWord = getResources().getStringArray(R.array.first_word);
            secondWord = getResources().getStringArray(R.array.second_word);
            thirdWord = getResources().getStringArray(R.array.third_word);

            mTextViewNewFad = (TextView) rootView.findViewById(R.id.tv_newfad);
            mBtnGenerateNewfad = (TextView) rootView.findViewById(R.id.btn_newfad_generate);
            mBtnGenerateNewfad.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTextViewNewFad.setText(generateNewFad());
                }
            });
            mBtnTweet = (TextView) rootView.findViewById(R.id.btn_newfad_tweet);
            mBtnTweet.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    tweetNewfad(mTextViewNewFad.getText().toString());
                }
            });
            mBtnAbout = (TextView) rootView.findViewById(R.id.btn_about);
            mBtnAbout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAbout();
                }
            });

            mTextViewNewFad.setText(generateNewFad());

            return rootView;
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

        private void tweetNewfad(String newfad) {
            StringBuilder mStringBuilderShare = new StringBuilder();
            mStringBuilderShare.append(getResources().getString(R.string.txt_tweet_phrase_1));
            mStringBuilderShare.append(" " + newfad + " ");
            mStringBuilderShare.append(getResources().getString(R.string.txt_tweet_phrase_2));
            // Create intent using ACTION_VIEW and a normal Twitter url:
            String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s", GenLib.urlEncode(mStringBuilderShare.toString()), GenLib.urlEncode(""));
            Intent intentTwitter = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

            // Narrow down to official Twitter app, if available:
            List<ResolveInfo> matchTwitter = getActivity().getPackageManager().queryIntentActivities(intentTwitter, 0);
            for (ResolveInfo info : matchTwitter) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                    intentTwitter.setPackage(info.activityInfo.packageName);
                }
            }
            startActivity(intentTwitter);
        }
    }
}
