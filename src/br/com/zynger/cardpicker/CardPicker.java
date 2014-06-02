package br.com.zynger.cardpicker;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import br.com.zynger.cardpicker.AnimationFactory.AnimationEndListener;
import br.com.zynger.cardpicker.AnimationFactory.AnimationStartListener;
import br.com.zynger.cardpicker.CardPagerAdapter.OnCardItemClickListener;

public class CardPicker extends RelativeLayout {

	private final long FADE_IN_DURATION = 300;
	private final long FADE_OUT_DURATION = 300;
	private final long SLIDE_CARD_DURATION = 500;
	private final int CARD_CLIP_MAX_VALUE = 10000;
	private final int CARD_CLIP_MIN_VALUE = 5700;
	private final float CARD_TRANSLATION_Y = 120f;

	private AnimationFactory mAnimationFactory;
	private ViewPager mPager;
	private ImageView mCard;
	private ImageView mWallet;

	private ClipDrawable mWalletClipDrawable;

	private boolean isAnimating = false;
	private boolean isCardShown = false;
	private CardPagerAdapter mCardAdapter;

	public CardPicker(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public CardPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.cardpicker, this,
				true);

		mAnimationFactory = new AnimationFactory();

		mWallet = (ImageView) findViewById(R.id.cardpicker_wallet);
		mCard = (ImageView) findViewById(R.id.cardpicker_card);
		mPager = (ViewPager) findViewById(R.id.cardpicker_pager);
		mCardAdapter = new CardPagerAdapter();
		mPager.setAdapter(mCardAdapter);

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mCard
				.getLayoutParams();
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);
		mCard.setLayoutParams(layoutParams);
		mWallet.setLayoutParams(layoutParams);

		mCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				show();
			}
		});
		
		mCardAdapter.setOnCardItemClickListener(new OnCardItemClickListener() {
			@Override
			public void onCardClick(View card, int position) {
				hide();
			}
		});
		
		setInitialState();
	}

	private void setInitialState() {
		setWalletClipDrawable(R.drawable.credit_card_blue);
		mWalletClipDrawable.setLevel(CARD_CLIP_MIN_VALUE);
		mCard.setTranslationY(CARD_TRANSLATION_Y);
	}
	
	private void setWalletClipDrawable(int resId) {
		Drawable cardDrawable = getContext().getResources().getDrawable(resId);
		mWalletClipDrawable = new ClipDrawable(cardDrawable, Gravity.TOP,
				ClipDrawable.VERTICAL);
		mCard.setImageDrawable(mWalletClipDrawable);
		mWalletClipDrawable.setLevel(CARD_CLIP_MAX_VALUE);
	}

	private void show() {
		if (isAnimating) {
			return;
		}
		
		isAnimating = true;
		slideUpCard(new AnimationEndListener() {
			@Override
			public void onAnimationEnd() {
				isCardShown = true;
				isAnimating = false;

				mAnimationFactory.fadeInView(mPager, FADE_IN_DURATION,
						new AnimationStartListener() {
							@Override
							public void onAnimationStart() {
								mPager.setVisibility(View.VISIBLE);
							}
						});
			}
		});
	}

	private void hide() {
		if (isAnimating) {
			return;
		}
		
		setWalletClipDrawable(mCardAdapter.getCardDrawableId(mPager.getCurrentItem()));
		
		isAnimating = true;
		mAnimationFactory.fadeOutView(mPager, FADE_OUT_DURATION,
				new AnimationEndListener() {
					@Override
					public void onAnimationEnd() {
						mPager.setVisibility(View.GONE);
						
						slideDownCard(new AnimationEndListener() {
							@Override
							public void onAnimationEnd() {
								isCardShown = false;
								isAnimating = false;
							}
						});
					}
				});
	}
	
	private void slideUpCard(final AnimationEndListener endListener) {
		mCard.animate().setDuration(SLIDE_CARD_DURATION).translationYBy(-CARD_TRANSLATION_Y)
				.setListener(new AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
						ValueAnimator valAnim = ValueAnimator.ofInt(
								CARD_CLIP_MIN_VALUE, CARD_CLIP_MAX_VALUE);
						valAnim.setDuration(SLIDE_CARD_DURATION);
						valAnim.addUpdateListener(new AnimatorUpdateListener() {
							@Override
							public void onAnimationUpdate(
									ValueAnimator animation) {
								Integer animatedValue = (Integer) animation
										.getAnimatedValue();
								mWalletClipDrawable.setLevel(animatedValue);
							}
						});
						valAnim.start();
					}

					@Override
					public void onAnimationRepeat(Animator animation) {
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						endListener.onAnimationEnd();
					}

					@Override
					public void onAnimationCancel(Animator animation) {
					}
				});
	}

	private void slideDownCard(final AnimationEndListener endListener) {
		mCard.animate().setDuration(SLIDE_CARD_DURATION).translationYBy(CARD_TRANSLATION_Y)
				.setListener(new AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
						ValueAnimator valAnim = ValueAnimator.ofInt(
								CARD_CLIP_MAX_VALUE, CARD_CLIP_MIN_VALUE);
						valAnim.setDuration(SLIDE_CARD_DURATION);
						valAnim.addUpdateListener(new AnimatorUpdateListener() {
							@Override
							public void onAnimationUpdate(
									ValueAnimator animation) {
								Integer animatedValue = (Integer) animation
										.getAnimatedValue();
								mWalletClipDrawable.setLevel(animatedValue);
							}
						});
						valAnim.start();
					}

					@Override
					public void onAnimationRepeat(Animator animation) {
					}

					@Override
					public void onAnimationEnd(Animator animation) {
						endListener.onAnimationEnd();
					}

					@Override
					public void onAnimationCancel(Animator animation) {
					}
				});
	}

	public void toggle() {
		if (isCardShown) {
			hide();
		} else {
			show();
		}
	}
}
