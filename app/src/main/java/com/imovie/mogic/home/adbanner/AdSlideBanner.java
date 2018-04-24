package com.imovie.mogic.home.adbanner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.imovie.mogic.R;
import com.imovie.mogic.config.AppConfig;
import com.imovie.mogic.home.model.DBModel_SlideBanner;
import com.imovie.mogic.utills.DensityUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 广告滑动控件
 */
public class AdSlideBanner extends FrameLayout {


	private final static int IMAGE_COUNT = 5;
	private final static int TIME_INTERVAL = 5;
	private final static boolean isAutoPlay = true;
	private List<DBModel_SlideBanner> dbModel_slideBanners;
	private List<ImageView> imageViewsList;
	private List<View> dotViewsList;
	private ViewPager viewPager;
	private int currentItem  = 0;
	private ScheduledExecutorService scheduledExecutorService;
	private Context context;
	private boolean isStartPlay = false;
	private View view = null;
	private ScaleType scaleType = ScaleType.FIT_CENTER;
	private int default_image = R.drawable.home_ad_default;

	private Handler handler;

	public interface OnPageClickListener {

		void OnPageClick(View view, DBModel_SlideBanner dbModel_slideBanner);
		
	}

	private OnPageClickListener listener;


	//Handler
	public static class MyHandler extends Handler {
		WeakReference<AdSlideBanner> mActivityReference;

		MyHandler(AdSlideBanner adBanner) {
			mActivityReference= new WeakReference<AdSlideBanner>(adBanner);
		}

		@Override
		public void handleMessage(Message msg) {
			final AdSlideBanner adBanner = mActivityReference.get();
			if (adBanner != null) {
				super.handleMessage(msg);
				adBanner.viewPager.setCurrentItem(adBanner.currentItem);

			}
		}
	}

	public AdSlideBanner(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub\
		this.context = context;
	}
	public AdSlideBanner(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public AdSlideBanner(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub

		this.context = context;
	}

	public void setData(List<DBModel_SlideBanner> dbModel_slideBanners) {

		if (dbModel_slideBanners == null || dbModel_slideBanners.size() == 0 )
			return;

		this.dbModel_slideBanners = dbModel_slideBanners;

		initData();
		initUI(context);
		if(!isStartPlay)
		startPlay();
	}

	public void setOnPageClickListener(OnPageClickListener l) {

		this.listener = l;

	}

	@Override
	public boolean callOnClick() {
		return true;
	}

	public void startPlay(){
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
		isStartPlay = true;
	}

	public void stopPlay(){
		if(scheduledExecutorService != null)
		scheduledExecutorService.shutdown();
		isStartPlay = false;
	}

	private void initData(){

		if(imageViewsList == null){
			imageViewsList = new ArrayList<ImageView>();
		}
		else{
			imageViewsList.clear();
		}
		if(dotViewsList == null){
			dotViewsList = new ArrayList<View>();
		}
		else{
			dotViewsList.clear();
		}
	}

	private void initUI(Context context){
		try {
			handler = new MyHandler(AdSlideBanner.this);

		if(view == null)
			view = LayoutInflater.from(context).inflate(R.layout.home_banner_window, this, true);

		LinearLayout ll_dotTask = (LinearLayout)view.findViewById(R.id.homeSlideBanner_ll_dotTask);
		ll_dotTask.removeAllViews();
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);

		for(final DBModel_SlideBanner dbModel_slideBanner : dbModel_slideBanners) {

			ImageView view =  new ImageView(context);
			view.setScaleType(ScaleType.FIT_XY);     //图片拉伸
			/**android:scaleType="centerInside"centerInside当原图宽高或等于View的宽高时，按原图大小居中显示；反之将原图缩放至View的宽高居中显示*/
//			view.setScaleType(ScaleType.CENTER_INSIDE);//2433 药采购-药品详情页-药品图片不能变形。之前版本已有的需求。
//			view.setScaleType(scaleType);//2433 药采购-药品详情页-药品图片不能变形。之前版本已有的需求。
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {

					if (listener != null) {
						listener.OnPageClick(view,dbModel_slideBanner);
					}
				}
			});
			imageViewsList.add(view);

			View dotView = new View(context);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(context,4), DensityUtil.dip2px(context,4));
			layoutParams.setMargins(DensityUtil.dip2px(context,1), 0, DensityUtil.dip2px(context,1), 0);
			dotView.setLayoutParams(layoutParams);
			dotView.setBackgroundResource(R.drawable.banner_scroll_normal);
			dotViewsList.add(dotView);
			ll_dotTask.addView(dotView);

		}
		viewPager.setFocusable(true);
		//if(viewPager.getAdapter() == null)
			viewPager.setAdapter(new MyPagerAdapter());
		//viewPager.getAdapter().notifyDataSetChanged();

		viewPager.setOnPageChangeListener(new MyPageChangeListener());

		}catch (Exception e){e.printStackTrace();}
	}

	// 设置图片的显示方式
	public void setImageScaleType(ScaleType type){
			 scaleType = type;
	}

	// 设置默认图片
	public void setDefaultImage(int image){
		default_image = image;
	}

	private class MyPagerAdapter  extends PagerAdapter{

		private DisplayImageOptions mOptions = null;

		MyPagerAdapter() {

			mOptions = new DisplayImageOptions.Builder().showImageOnLoading(default_image)
					.showImageForEmptyUri(default_image)
					.showImageOnFail(default_image)
					.cacheInMemory(true).cacheOnDisc(true).delayBeforeLoading(200)
					.build();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			//((ViewPag.er)container).removeView((View)object);
			try {
				container.removeView(imageViewsList.get(position));
			}catch (Exception e){}
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			// TODO Auto-generated method stub
			if(dbModel_slideBanners == null || dbModel_slideBanners.size() == 0 || imageViewsList == null || imageViewsList.size() ==0){
				return 0;
			}
			String path = dbModel_slideBanners.get(position).imageUrl;
			ImageView iv = imageViewsList.get(position);
			ImageLoader.getInstance().displayImage(path, iv, mOptions);
			container.addView(imageViewsList.get(position), AppConfig.getScreenWidth(), AppConfig.getScreenWidth() * 244 / 640);
			return imageViewsList != null? imageViewsList.get(position):0;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageViewsList != null? imageViewsList.size():0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}

	private class MyPageChangeListener implements OnPageChangeListener{

		boolean isAutoPlay = false;
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
				case 1:
					isAutoPlay = false;
					break;
				case 2:
					isAutoPlay = true;
					break;
				case 0:
					if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
						viewPager.setCurrentItem(0);
					}
					else
					if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
						viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
					}
					break;
				}
			}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onPageSelected(int pos) {
			// TODO Auto-generated method stub
			currentItem = pos;
			for(int i=0;i < dotViewsList.size();i++){
				if(i == pos){
					((View)dotViewsList.get(pos)).setBackgroundResource(R.drawable.banner_scroll_orange3x);
				}
				else
				{
					((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.banner_scroll_normal3x);
				}
			}
		}
	}

	private class SlideShowTask implements Runnable{
		@Override
		public void run() {
		// TODO Auto-generated method stub
			synchronized (viewPager) {
				currentItem = (currentItem+1)%imageViewsList.size();
				handler.obtainMessage().sendToTarget();
			}
		}
	}

}
