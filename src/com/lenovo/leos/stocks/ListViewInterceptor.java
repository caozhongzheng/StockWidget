package com.lenovo.leos.stocks;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ListViewInterceptor extends ListView {
	private static final String TAG = "ListViewInterceptor";

	private DropListener mDropListener;

	private ImageView mDragView;
	private Bitmap mDragBitmap;
	private ViewGroup mParent;
	private ImageView mPlaceHolderView;
	private LinearLayout mContainer;
	private int mContainerIndex;
	private int mContainerHeight;
	//
	private ArrayList<Integer> mTriggerPoints;
	private int mFirstDragPos; 
	private int mFirstVisiblePos;
	private int mChildIndex;
	
	public ListViewInterceptor(Context context) {
		super(context);
	}

	public ListViewInterceptor(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ListViewInterceptor(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.v(TAG, ">>>>>>>>>>onTouchEvent");
		if ((mDropListener != null) && mDragView != null) {
			int action = ev.getAction();
			switch (action) {
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					int toPos = mChildIndex+mFirstVisiblePos;
					if (mDropListener != null && mFirstDragPos!=toPos && mFirstDragPos >= 0 && toPos < getCount()) {
						mDropListener.drop(mFirstDragPos, toPos);
					}
					stopDragging();
					break;
				case MotionEvent.ACTION_DOWN:
					prevY = (int) ev.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					int y = (int) ev.getY();
					dragView(y);

					int index = getTriggerIndex(y);
					Log.v(TAG, ">>>ACTION_MOVE>>>>>>>>>>" + "position=" + index + ",prevPos=" + mChildIndex + ",marginTop" + mMarginTop);
					if (index != INVALID_POSITION) {
						filler(index);
						mChildIndex = index;
					}
					break;
			}
			return true;
		}
		return super.onTouchEvent(ev);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mDropListener != null) {
			switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					int x = (int) ev.getX();
					int y = (int) ev.getY();
					final int itemnum = pointToPosition(x, y);
					Log.v(TAG, "onIntercept>>ACTION_DOWN>>itemnum>>>>>>>>>>>" + itemnum + ",y" + y);
					if (itemnum == AdapterView.INVALID_POSITION) {
						break;
					}
					//
					int first = getFirstVisiblePosition();
					mChildIndex = itemnum - first;
					View item = getChildAt(mChildIndex);
					View dragger = item.findViewById(mDraggerId);
					Log.v(TAG, "onIntercept >>>>>>>>>>>firstVisiblePosition=" + getFirstVisiblePosition() + //
							"-----dragger=" + dragger + //
							"---RawY=" + ev.getRawY() + //
							"----Y=" + ev.getY() + //
							"-----Top=" + item.getTop() + //
							"------itemParent=" + item.getParent());//
					if (dragger == null) {
						break;
					}
					//
					Rect r = new Rect();
					r.left = dragger.getLeft();
					r.right = dragger.getRight();
					r.top = dragger.getTop();
					r.bottom = dragger.getBottom();
					Log.d(TAG, "l=" + r.left + ",t=" + r.top + ",r=" + r.right + ",b=" + r.bottom + 
							",x=" + x + ",y=" + y + ",listHeight="+ getHeight());
					if (r.contains(x - item.getLeft(), y - item.getTop()) && getChildCount()>1) {
						mFirstDragPos = itemnum;
						mFirstVisiblePos = first;
						startDrag(item, mChildIndex);
					}
					break;
			}
		}
		return super.onInterceptTouchEvent(ev); 
	}

	private void startDrag(View item, int index) {
		stopDragging();
		//
		item.setDrawingCacheEnabled(true);
		mDragBitmap = Bitmap.createBitmap(item.getDrawingCache());
		preparDrag();
		//
		mContainer.removeViewAt(index);
		//
		filler(index);
		//
		addFloatView(mDragBitmap, index);
	}
	
	private void addFloatView(Bitmap bm, int childIndex){
		ImageView v = new ImageView(getContext());
		v.setImageBitmap(bm);
		v.setScaleType(ScaleType.FIT_XY);
		v.setAlpha(150);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(bm.getWidth(), bm.getHeight());
		mContainer.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		if(childIndex-1<0){
			mMarginTop = mContainer.getChildAt(childIndex+1).getTop()-mDragBitmap.getHeight();
		}else{
			mMarginTop = mContainer.getChildAt(childIndex-1).getBottom();
		}
		mContainerHeight = mContainer.getMeasuredHeight();
		params.setMargins(0, -mContainerHeight + mMarginTop, 0, 0);
		mContainer.addView(v, params);
		mParent.addView(mContainer, mContainerIndex);
		//
		mDragView = v;
		Log.i(TAG, ">>>addFloatView>>>>>>>>>>> view=" + mDragView + //
				", width=" + mDragBitmap.getWidth() + //
				", height=" + mDragBitmap.getHeight() + //
				", containerHeight=" + mContainer.getHeight() + //
				", MeasuredHeight=" + mContainer.getMeasuredHeight() + //
				", marginTop=" + mMarginTop+
				", top="+mDragView.getTop());
	}

	private void preparDrag() {
		ViewParent parent = this.getParent();
		if (parent instanceof ViewGroup) {
			mParent = (ViewGroup) parent;
		} else {
			throw new RuntimeException(this.getClass().getName() + "must have a ViewGroup parent.");
		}
		//
		mContainerIndex = mParent.indexOfChild(this);
		mParent.removeView(this);
		mContainer = new LinearLayout(getContext());
		mContainer.setOrientation(LinearLayout.VERTICAL);
		mContainer.setLayoutParams(new ViewGroup.LayoutParams(this.getWidth(), this.getHeight()));
		//
		int count = getChildCount();
		View[] items = new View[count];
		for (int i = 0; i < count; i++) {
			items[i] = getChildAt(i);
		}
		mTop = items[0].getTop();
		detachAllViewsFromParent();
		mTriggerPoints = new ArrayList<Integer>();
		for (int i = 0; i < count; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(items[i].getWidth(), items[i].getHeight());
			if (i == 0) {
				params.topMargin = mTop;
				mContainer.addView(items[i], params);
			} else {
				mContainer.addView(items[i], params);
			}
			mTriggerPoints.add(items[i].getTop() + items[i].getHeight() / 2);
		}
		Log.i(TAG, "trigger points=" + Arrays.toString(mTriggerPoints.toArray()));
	}

	private void filler(int index) {
		Log.d(TAG, ">>>filler view>>>>>> index=" + index);
		// if(mNeedBorder ){
		// }
		if (mPlaceHolderView == null) {
			Bitmap bm = Bitmap.createBitmap(mDragBitmap.getWidth(), mDragBitmap.getHeight(), Config.ARGB_8888);
			mPlaceHolderView = new ImageView(getContext());
			mPlaceHolderView.setImageBitmap(bm);
		}
		//
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDragBitmap.getWidth(),mDragBitmap.getHeight());
		params.topMargin = mTop;
		mContainer.removeView(mPlaceHolderView);
		mContainer.addView(mPlaceHolderView,index,params);
	}

	private void dragView(int y) {
		//int marginTop = y - mDragPoint;
		mMarginTop = mMarginTop+y-prevY;
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDragView.getLayoutParams();
		params.height = mDragBitmap.getHeight();
		params.width = mDragBitmap.getWidth();
		params.setMargins(0, -mContainerHeight+mMarginTop, 0, 0);
		Log.i(TAG, ">>>dragView>>>>>> marginTop=" + mMarginTop + //
				", listHeight=" + getHeight() + //
				", containerHeight=" + mContainer.getHeight()+
				", dragViewHeight="+mDragView.getHeight()+
				", y="+y+",prevY="+prevY);
		
		mContainer.updateViewLayout(mDragView, params);
	}

	private int prevY;

	private int getTriggerIndex(int y) {
		int index = INVALID_POSITION;
		if (y < prevY) {
			int top = mDragView.getTop();
			int i = mChildIndex - 1 < 0 ? -1 : mChildIndex - 1;
			if (i != -1 && top < mTriggerPoints.get(i)) {
				index = i;
			}
		}
		if (y > prevY) {
			int bottom = mDragView.getBottom();
			int i = mChildIndex + 1 > mTriggerPoints.size() - 1 ? -1 : mChildIndex + 1;
			if (i != -1 && bottom > mTriggerPoints.get(i)) {
				index = i;
			}
		}
		prevY = y;
		return index;
	}

	private void stopDragging() {
		if (mContainer != null) {
			mContainer.removeAllViews();
			mParent.removeView(mContainer);
			mParent.addView(this, mContainerIndex);
			mContainer = null;
		}
		if (mPlaceHolderView != null) {
			mPlaceHolderView.setImageDrawable(null);
			mPlaceHolderView = null;
		}
		if (mDragView != null) {
			Log.i(TAG, "itemnum>>>  mContainer.removeView(mDragView),mDragView=" + mDragView.toString());
			mDragView.setImageDrawable(null);
			mDragView = null;
		}
		if (mDragBitmap != null) {
			// mDragBitmap.recycle();
			mDragBitmap = null;
		}
		this.setSelectionFromTop(mFirstVisiblePos,mTop);
	}

	private int mDraggerId;

	private int mTop;

	private int mMarginTop;

	public void setDropListener(int dragger, DropListener onDrop) {
		mDraggerId = dragger;
		mDropListener = onDrop;
	}

	public interface DropListener {
		void drop(int from, int to);
	}

}
