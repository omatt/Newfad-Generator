package com.omatt.newfadgenerator.lib;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Android Dev on 18/3/2015.
 */
public class Animate {
    public static void fadeInLayout(final LinearLayout layout, final boolean isFadeOut) {
        int animStart = 0, animEnd = 1;
        if(isFadeOut){
            animStart = 1;
            animEnd = 0;
        }
        Animation fadeIn = new AlphaAnimation(animStart, animEnd);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(200);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                if(isFadeOut)layout.setVisibility(View.GONE);
                else layout.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {

            }
        });
        layout.startAnimation(fadeIn);
    }
}
