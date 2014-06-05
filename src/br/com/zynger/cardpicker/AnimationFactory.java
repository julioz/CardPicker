package br.com.zynger.cardpicker;

import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

public class AnimationFactory {

	private static final String ALPHA = "alpha";
	private static final float INVISIBLE = 0f;
	private static final float VISIBLE = 1f;

	public AnimationFactory() {
	}

	public void fadeInView(View target, long duration,
			final AnimationStartListener listener) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(target, ALPHA, INVISIBLE,
				VISIBLE);
		animator.setDuration(duration).addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
				listener.onAnimationStart();
			}

			@Override
			public void onAnimationEnd(Animator animator) {
			}

			@Override
			public void onAnimationCancel(Animator animator) {
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}
		});
		animator.start();
	}

	public void fadeOutView(final View target, long duration,
			final AnimationEndListener listener) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(target, ALPHA, INVISIBLE);
		animator.setDuration(duration).addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				if (isBelowHoneycomb()) {
					target.clearAnimation();
				}
				listener.onAnimationEnd();
			}

			@Override
			public void onAnimationCancel(Animator animator) {
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}
		});
		animator.start();
	}

	public static boolean isBelowHoneycomb() {
		return android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB;
	}

	public interface AnimationStartListener {
		void onAnimationStart();
	}

	public interface AnimationEndListener {
		void onAnimationEnd();
	}
}
