/**
 * animation
 */
package com.wtf.bate.startup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;


/**
 * @author wtf
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class LayoutAnimation {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public static void setupCustomAnimations(LayoutTransition mTransitioner,
			Context context) {
		// Changing while Adding
		PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
		PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
		PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0,
				1);
		PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom",
				0, 1);
		PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX",
				1f, 0f, 1f);
		PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY",
				1f, 0f, 1f);
		final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(
				context, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX,
				pvhScaleY).setDuration(
				mTransitioner.getDuration(LayoutTransition.CHANGE_APPEARING));
		

		mTransitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
		
		
		changeIn.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setScaleX(1f);
				view.setScaleY(1f);
			}
		});

		// Changing while Removing
		Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
		Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
		Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
		PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe(
				"rotation", kf0, kf1, kf2);
		final ObjectAnimator changeOut = ObjectAnimator
				.ofPropertyValuesHolder(context, pvhLeft, pvhTop, pvhRight,
						pvhBottom, pvhRotation)
				.setDuration(
						mTransitioner
								.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
		
		
		mTransitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,
				changeOut);
		
		
		changeOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotation(0f);
			}
		});

		// Adding
		ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f,
				0f).setDuration(
				mTransitioner.getDuration(LayoutTransition.APPEARING));
		
		mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);
		
		animIn.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationY(0f);
			}
		});

		// Removing
		ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f,
				90f).setDuration(
				mTransitioner.getDuration(LayoutTransition.DISAPPEARING));
		
		mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
		
		animOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationX(0f);
			}
		});

	}
}
