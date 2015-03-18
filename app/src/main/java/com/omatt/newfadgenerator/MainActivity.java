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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.omatt.newfadgenerator.analytics.AnalyticsMedium;
import com.omatt.newfadgenerator.lib.Animate;
import com.omatt.newfadgenerator.lib.GenLib;

import java.util.List;
import java.util.Random;

import static android.view.View.OnClickListener;
import static com.omatt.newfadgenerator.analytics.AnalyticsMedium.TrackerName;


public class MainActivity extends ActionBarActivity {
    private static UiLifecycleHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getSupportActionBar().hide();

        GenLib.generateHashKey(this);
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
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
        private String[] firstWord, secondWord, thirdWord;
        private TextView mTextViewNewFad, mBtnGenerateNewfad, mBtnTweet, mBtnAbout;
        private ImageButton mBtnShare, mBtnShareFB, mBtnShareTwitter, mBtnShareSMS, mBtnShareEmail;
        private LinearLayout layoutShare;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

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

            mTextViewNewFad.setText(generateNewFad());

            return rootView;
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
                    shareFacebookNewfad(generateShareMessage(mTextViewNewFad.getText().toString()));
                    break;
                case R.id.btn_share_twitter:
                    shareTweetNewfad(generateShareMessage(mTextViewNewFad.getText().toString()));
                    break;
                case R.id.btn_share_sms:
                    shareSMSNewfad(generateShareMessage(mTextViewNewFad.getText().toString()));
                    break;
                case R.id.btn_share_email:
                    shareEmailNewfad(generateShareMessage(mTextViewNewFad.getText().toString()));
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
            StringBuilder mStringBuilderShare = new StringBuilder();
            mStringBuilderShare.append(getResources().getString(R.string.txt_share_phrase_1));
            mStringBuilderShare.append(" " + newfad + " ");
            mStringBuilderShare.append(getResources().getString(R.string.txt_share_phrase_2));

            return mStringBuilderShare.toString();
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

        private void shareEmailNewfad(String shareEmail) {

            Intent intentEmail = new Intent(Intent.ACTION_SEND);
            intentEmail.setType("message/rfc822");
            intentEmail.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.txt_share_email_subject));
            intentEmail.putExtra(Intent.EXTRA_TEXT, shareEmail);

            try {
                startActivity(Intent.createChooser(intentEmail, "Send Email"));
            } catch (android.content.ActivityNotFoundException ex) {
                Log.e(TAG, "No email clients installed");
            }
        }

        private void shareFacebookNewfad(String shareFacebook){
            if (FacebookDialog.canPresentShareDialog(getActivity(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
                // Publish the post using the Share Dialog
                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(getActivity()).setDescription(shareFacebook).setLink("http://keikun17.github.io/newfad-generator/").build();
                uiHelper.trackPendingDialogCall(shareDialog.present());
            } else {
                Intent intentFacebook = new Intent(Intent.ACTION_VIEW);
                intentFacebook.setType("text/plain");
                intentFacebook.putExtra(android.content.Intent.EXTRA_TEXT, "Share via Facebook");

                List<ResolveInfo> matchFacebook = getActivity().getPackageManager().queryIntentActivities(intentFacebook, 0);
                for (ResolveInfo info : matchFacebook) {
                    if ((info.activityInfo.name).contains("facebook")) {
                        intentFacebook.setPackage(info.activityInfo.packageName);
                    }
                }
                startActivity(intentFacebook);
            }
        }

        private void shareSMSNewfad(String shareSMS) {

            Intent intentMessage = new Intent(Intent.ACTION_VIEW);
            intentMessage.setType("vnd.android-dir/mms-sms");
            intentMessage.setData(Uri.parse("sms:"));
            intentMessage.putExtra("sms_body", shareSMS);
            try {
                startActivity(intentMessage);
            } catch (android.content.ActivityNotFoundException ex) {
                Log.e(TAG, "No sms clients installed");
            }
        }

        private void shareTweetNewfad(String shareTweet) {

            // Create intent using ACTION_VIEW and a normal Twitter url:
            String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s", GenLib.urlEncode(shareTweet), GenLib.urlEncode(""));
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
