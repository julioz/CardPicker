package br.com.zynger.cardpicker;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import br.com.zynger.cardpicker.AnimationFactory.AnimationEndListener;
import br.com.zynger.cardpicker.AnimationFactory.AnimationStartListener;
import br.com.zynger.cardpicker.CardAdapter.OnCardClickListener;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class CardPicker extends RelativeLayout {

	private final long FADE_IN_DURATION = 300;
	private final long FADE_OUT_DURATION = 300;
	private final long SLIDE_CARD_DURATION = 300;
	private final int CARD_CLIP_MAX_VALUE = 10000;
	private final int CARD_CLIP_MIN_VALUE = 5700;
	private final int CARD_TRANSLATION_Y_DEFAULT = 70;
	
	private float mDensity;
	private float mCardTranslationY;
	
	private AnimationFactory mAnimationFactory;
	private CardPagerContainer mCardPager;
	private ImageView mCard;
	private ImageView mWallet;

	private ClipDrawable mWalletClipDrawable;

	private boolean isAnimating = false;
	private boolean isCardShown = false;
	private CardAdapter mCardAdapter;

	public CardPicker(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public CardPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.cardpicker, this,
				true);

		mAnimationFactory = new AnimationFactory();
		mDensity = getContext().getResources().getDisplayMetrics().density;
		mCardTranslationY = CARD_TRANSLATION_Y_DEFAULT * mDensity;

		mWallet = (ImageView) findViewById(R.id.cardpicker_wallet);
		mCard = (ImageView) findViewById(R.id.cardpicker_card);
		mCardPager = new CardPagerContainer(getContext());
		mCardPager.setVisibility(View.GONE);
		((ViewGroup) ((Activity) getContext()).getWindow().getDecorView()).addView(mCardPager);

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mCard
				.getLayoutParams();
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT,
				RelativeLayout.TRUE);
		mCard.setLayoutParams(layoutParams);
		mWallet.setLayoutParams(layoutParams);

		mCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		
		setInitialState();
	}
	
	public void setCardTranslationY(float translationY) {
		mCardTranslationY = mDensity * translationY;
	}
	
	public void setCardAdapter(CardAdapter cardAdapter) {
		mCardAdapter = cardAdapter;
		mCardPager.setAdapter(mCardAdapter);
	}
	
	public void setOverlayColor(int color) {
		mCardPager.setOverlayColor(color);
	}

	private void setInitialState() {
		Drawable blueCard = getContext().getResources().getDrawable(R.drawable.credit_card_blue);
		setWalletClipDrawable(blueCard);
		mWalletClipDrawable.setLevel(CARD_CLIP_MIN_VALUE);
		ViewHelper.setTranslationY(mCard, mCardTranslationY);
	}
	
	private void setWalletClipDrawable(Drawable cardDrawable) {
		mWalletClipDrawable = new ClipDrawable(cardDrawable, Gravity.TOP,
				ClipDrawable.VERTICAL);
		mCard.setImageDrawable(mWalletClipDrawable);
		mWalletClipDrawable.setLevel(CARD_CLIP_MAX_VALUE);
	}

	public void show() {
		if (isAnimating) {
			return;
		}
		
		isAnimating = true;
		slideUpCard(new AnimationEndListener() {
			@Override
			public void onAnimationEnd() {
				isCardShown = true;
				isAnimating = false;

				mAnimationFactory.fadeInView(mCardPager, FADE_IN_DURATION,
						new AnimationStartListener() {
							@Override
							public void onAnimationStart() {
								mCardPager.setVisibility(View.VISIBLE);
							}
						});
			}
		});
	}

	public void hide() {
		if (isAnimating) {
			return;
		}
		
		setWalletClipDrawable(mCardAdapter.getCardAsDrawable(getContext(), mCardPager.getCurrentItem()));
		
		isAnimating = true;
		mAnimationFactory.fadeOutView(mCardPager, FADE_OUT_DURATION,
				new AnimationEndListener() {
					@Override
					public void onAnimationEnd() {
						mCardPager.setVisibility(View.GONE);
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
		ViewPropertyAnimator.animate(mCard).setDuration(SLIDE_CARD_DURATION).translationYBy(-mCardTranslationY)
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
		ViewPropertyAnimator.animate(mCard).setDuration(SLIDE_CARD_DURATION).translationYBy(mCardTranslationY)
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

	public void setOnCardClickListener(final OnCardClickListener onCardClickListener) {
		mCardAdapter.setOnCardClickListener(new OnCardClickListener() {
			@Override
			public boolean onCardClick(View card, int position) {
				if (isAnimating) return false;
				
				boolean userListenerReturn = onCardClickListener.onCardClick(card, position);
				if (userListenerReturn) {
					hide();
				}
				return userListenerReturn;
			}

			@Override
			public boolean onCardBackgroundClick(View card, int position) {
				if (isAnimating) return false;
				
				boolean userListenerReturn = onCardClickListener.onCardBackgroundClick(card, position);
				if (userListenerReturn) {
					hide();
				}
				return userListenerReturn;
			}
		});
	}
}
