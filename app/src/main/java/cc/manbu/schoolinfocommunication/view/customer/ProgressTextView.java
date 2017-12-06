package cc.manbu.schoolinfocommunication.view.customer;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

import cc.manbu.schoolinfocommunication.R;


/**
* 自定义带文字动画效果的TextView
* @author gongyong2014
*
*/
public class ProgressTextView extends TextView {
	private Activity context;
	private int count = 0;
	private String str;
	private String text;
	private boolean isEnnableWordAnimation = false;
	private Thread thread;
	private String endText;
	private boolean isHidden;
	private Handler mHandler;
	public ProgressTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = (Activity) context;
		init();
		setVisibility(View.VISIBLE);
	}

	public ProgressTextView(Context context) {
		super(context);
		this.context = (Activity) context;
		init();
	}

	private void init(){
		if(getBackground()==null){
			setBackgroundResource(R.drawable.roundcorner_shape);
		}
		setGravity(Gravity.CENTER);
		setVisibility(View.GONE);
		mHandler = new Handler(Looper.getMainLooper());
	}
	public boolean isVisible(){
		return getVisibility()== View.VISIBLE;
	}
	/**
	 * 开启文字动画：.->..->...
	 */
	public void startWordAinmation(boolean isUsePoint){
		startWordAinmation(isUsePoint,2000,0,null,0,null,false);
	}

	public void startWordAinmation(boolean isUsePoint, long enterAnimDuration, final long existDuration, final String endText, final long exitDelayDuration, final Runnable onStop, final boolean isWillHidden){
		text = getText().toString();
		isHidden = false;
		mHandler.removeCallbacksAndMessages(null);
		clearAnimation();
		final String point = ".";
		if(isUsePoint){
			str = text;
			isEnnableWordAnimation = true;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if(isEnnableWordAnimation){
						count++;
						if(count<=6){
							str = str + point;
						}else{
							count = 0;
							str = text;
						}
						setText(str);
						if(!isHidden){
							setVisibility(View.VISIBLE);
						}
						mHandler.postDelayed(this,500);
						Log.w("mTimerTask", "show["+isVisible()+"]:"+str);
					}else {
						mHandler.removeCallbacksAndMessages(null);
						show();
					}
				}
			},50);
//			mTimer = new Timer();
//			mTimerTask = new TimerTask(){
//
//				@Override
//				public void run() {
//					thread = Thread.currentThread();
//					Log.d("Timer Thread", thread.toString());
//					if(isEnnableWordAnimation){
//						context.runOnUiThread(new Runnable(){
//
//							@Override
//							public void run() {
//								count++;
//								if(count<=6){
//									str = str + point;
//								}else{
//									count = 0;
//									str = text;
//								}
//								setText(str);
//								if(!isHidden){
//									setVisibility(View.VISIBLE);
//								}
//								Log.w("mTimerTask", "show["+isVisible()+"]:"+str);
//							}});
//					}else{
//							context.runOnUiThread(new Runnable(){
//
//								@Override
//								public void run() {
//									show();
//								}});
//							if(mTimer!=null){
//								mTimer.cancel();
//								mTimer = null;
//							}
//							if(mTimerTask!=null){
//								mTimerTask.cancel();
//								mTimerTask = null;
//							}
//					}
//				}
//
//			};
//			mTimer.scheduleAtFixedRate(mTimerTask,10, 500);
		}
		if(!isVisible()){
			AlphaAnimation mAlphaAnimation = new AlphaAnimation(0f,1f);
			mAlphaAnimation.setDuration(enterAnimDuration);
			mAlphaAnimation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if(existDuration>0){
						postDelayed(new Runnable() {
							@Override
							public void run() {
								stopWordAinmation(endText,exitDelayDuration,onStop,isWillHidden);
							}
						},existDuration);
					}
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}
			});
			setVisibility(View.VISIBLE);
			startAnimation(mAlphaAnimation);
		}else{
			if(existDuration>0){
				postDelayed(new Runnable() {
					@Override
					public void run() {
						stopWordAinmation(endText,exitDelayDuration,onStop,isWillHidden);
					}
				},existDuration);
			}
		}
	}

	public void simpleShowText(int sid,boolean isWillHidden){
		stopWordAinmation(context.getResources().getString(sid), 0L, null,isWillHidden);
	}

	public void simpleShowText(String text, boolean isWillHidden){
		stopWordAinmation(text, 0L, null,isWillHidden);
	}
	public void stopWordAinmation(int endTextId, long delay, Runnable onStop, boolean isHidden){
		stopWordAinmation(context.getResources().getString(endTextId), delay, onStop,isHidden);
	}
	/**
	 * 停止文字动画
	 * @param endText
	 * @param delay 等待一段时间后在执行
	 * @param onStop
	 */
	public void stopWordAinmation(String endText, long delay, final Runnable onStop, boolean isHidden){
		this.endText = endText;
		this.isHidden = isHidden;
		if(delay>0){
			postDelayed(new Runnable(){
				@Override
				public void run() {
					isEnnableWordAnimation = false;
					mHandler.removeCallbacksAndMessages(null);
					show();
					if(onStop!=null){
						onStop.run();
					}
				}}, delay);
		}else{
			isEnnableWordAnimation = false;
			show();
			if(onStop!=null){
				onStop.run();
			}
		}
	}

	private void show() {
		clearAnimation();
		setText(endText);
		if(!isVisible()){
			AlphaAnimation mAlphaAnimation = new AlphaAnimation(0f,1f);
			mAlphaAnimation.setDuration(2000);
			setVisibility(View.VISIBLE);
			startAnimation(mAlphaAnimation);
		}
		if(isHidden){
			AlphaAnimation mAlphaAnimation = new AlphaAnimation(1f,0f);
			mAlphaAnimation.setDuration(3000);
			mAlphaAnimation.setAnimationListener(new AnimationListener(){

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if(isHidden){
						setText("");
						setVisibility(View.GONE);
					}
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}});
			startAnimation(mAlphaAnimation);
		}
	}
}