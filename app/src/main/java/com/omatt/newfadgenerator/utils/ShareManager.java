package com.omatt.newfadgenerator.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.omatt.newfadgenerator.R;
import com.omatt.newfadgenerator.lib.GenLib;

import java.util.List;

/**
 * Created by Omatt on 16/4/2015.
 */
public class ShareManager {
    public static void shareFacebookNewfad(Activity activity, ShareDialog mShareDialog, String shareFacebook) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(activity.getString(R.string.app_name))
                    .setContentDescription(shareFacebook)
                    .setContentUrl(Uri.parse(GlobalValues.APP_URL_FB))
                    .build();
            mShareDialog.show(linkContent);
        }
    }

    public static void shareTweetNewfad(Activity activity, String shareTweet) {
        // Create intent using ACTION_VIEW and a normal Twitter url:
        String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s", GenLib.urlEncode(shareTweet), GenLib.urlEncode(GlobalValues.APP_URL));
        Intent intentTwitter = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

        // Narrow down to official Twitter app, if available:
        List<ResolveInfo> matchTwitter = activity.getPackageManager().queryIntentActivities(intentTwitter, 0);
        for (ResolveInfo info : matchTwitter) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intentTwitter.setPackage(info.activityInfo.packageName);
            }
        }
        activity.startActivity(intentTwitter);
    }

    public static void shareEmailNewfad(Activity activity, String shareEmail) {
        Intent intentEmail = new Intent(Intent.ACTION_SEND);
        intentEmail.setType("message/rfc822");
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.txt_share_email_subject));
        intentEmail.putExtra(Intent.EXTRA_TEXT, shareEmail);

        try {
            activity.startActivity(Intent.createChooser(intentEmail, "Send Email"));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void shareSMSNewfad(Activity activity, String shareSMS) {
        Intent intentMessage = new Intent(Intent.ACTION_VIEW);
        intentMessage.setType("vnd.android-dir/mms-sms");
        intentMessage.setData(Uri.parse("sms:"));
        intentMessage.putExtra("sms_body", shareSMS);
        try {
            activity.startActivity(intentMessage);
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
