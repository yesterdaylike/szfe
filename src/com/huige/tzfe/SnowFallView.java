package com.huige.tzfe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

public class SnowFallView extends View {
	public SnowFallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);
		setFocusableInTouchMode(true);
		snowFlakes = new Drawable[6];

		int snowFlakeIds[] = {
				R.drawable.snowflake1,
				R.drawable.snowflake2,
				R.drawable.snowflake3,
				R.drawable.snowflake4,
				R.drawable.snowflake5,
				R.drawable.snowflake6,
		};

		for (int i = 0; i < snowFlakeIds.length; i++) {
			Drawable snowFlake = context.getResources().getDrawable(snowFlakeIds[i++]);
			snowFlake.setBounds(0, 0, snowFlake.getIntrinsicWidth(), snowFlake
					.getIntrinsicHeight());
			snowFlakes[i] = snowFlake;
		}
	}

	private int snow_flake_count = 10;
	private final List<Drawable> drawables = new ArrayList<Drawable>();
	private int[][] coords;
	private final Drawable []snowFlakes;

	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
		super.onSizeChanged(width, height, oldw, oldh);
		Random random = new Random();
		Interpolator interpolator = new LinearInterpolator();

		snow_flake_count = Math.max(width, height) / 20;
		coords = new int[snow_flake_count][];
		drawables.clear();
		for (int i = 0; i < snow_flake_count; i++) {
			//AnimationSet animationSet = new AnimationSet(true);
			Animation animation = new TranslateAnimation(0, height / 10
					- random.nextInt(height / 5), 0, height + 30);
			animation.setDuration(10 * height + random.nextInt(5 * height));
			animation.setRepeatCount(-1);
			animation.initialize(10, 10, 10, 10);
			animation.setInterpolator(interpolator);
			/*animationSet.addAnimation(animation);

			animation = new ScaleAnimation(1, 0.5f, 1, 2f);
			animation.setRepeatCount(Animation.INFINITE);
			animation.setDuration(1000);
			animationSet.addAnimation(animation);

			animation = new RotateAnimation(0, 359);
			animation.setDuration(1000);
			animation.setRepeatCount(Animation.INFINITE);
			animationSet.addAnimation(animation);*/

			coords[i] = new int[] { random.nextInt(width - 30), -30 };
			drawables.add(new AnimateDrawable(snowFlakes[i%6], animation));

			animation.setStartOffset(random.nextInt(20 * height));
			animation.startNow();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (int i = 0; i < snow_flake_count; i++) {
			Drawable drawable = drawables.get(i);
			canvas.save();
			canvas.translate(coords[i][0], coords[i][1]);
			drawable.draw(canvas);
			canvas.restore();
		}
		invalidate();
	}
	class AnimateDrawable extends ProxyDrawable {

		private Animation mAnimation;
		private Transformation mTransformation = new Transformation();

		public AnimateDrawable(Drawable target) {
			super(target);
		}

		public AnimateDrawable(Drawable target, Animation animation) {
			super(target);
			mAnimation = animation;
		}

		public Animation getAnimation() {
			return mAnimation;
		}

		public void setAnimation(Animation anim) {
			mAnimation = anim;
		}

		public boolean hasStarted() {
			return mAnimation != null && mAnimation.hasStarted();
		}

		public boolean hasEnded() {
			return mAnimation == null || mAnimation.hasEnded();
		}

		@Override
		public void draw(Canvas canvas) {
			Drawable dr = getProxy();
			if (dr != null) {
				int sc = canvas.save();
				Animation anim = mAnimation;
				if (anim != null) {
					anim.getTransformation(
							AnimationUtils.currentAnimationTimeMillis(),
							mTransformation);
					canvas.concat(mTransformation.getMatrix());
				}
				dr.draw(canvas);
				canvas.restoreToCount(sc);
			}
		}
	}

	class ProxyDrawable extends Drawable {

		private Drawable mProxy;

		public ProxyDrawable(Drawable target) {
			mProxy = target;
		}

		public Drawable getProxy() {
			return mProxy;
		}

		public void setProxy(Drawable proxy) {
			if (proxy != this) {
				mProxy = proxy;
			}
		}

		@Override
		public void draw(Canvas canvas) {
			if (mProxy != null) {
				mProxy.draw(canvas);
			}
		}

		@Override
		public int getIntrinsicWidth() {
			return mProxy != null ? mProxy.getIntrinsicWidth() : -1;
		}

		@Override
		public int getIntrinsicHeight() {
			return mProxy != null ? mProxy.getIntrinsicHeight() : -1;
		}

		@Override
		public int getOpacity() {
			return mProxy != null ? mProxy.getOpacity() : PixelFormat.TRANSPARENT;
		}

		@Override
		public void setFilterBitmap(boolean filter) {
			if (mProxy != null) {
				mProxy.setFilterBitmap(filter);
			}
		}

		@Override
		public void setDither(boolean dither) {
			if (mProxy != null) {
				mProxy.setDither(dither);
			}
		}

		@Override
		public void setColorFilter(ColorFilter colorFilter) {
			if (mProxy != null) {
				mProxy.setColorFilter(colorFilter);
			}
		}

		@Override
		public void setAlpha(int alpha) {
			if (mProxy != null) {
				mProxy.setAlpha(alpha);
			}
		}
	}
} 
