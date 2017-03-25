package phone.crc.com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends Activity {

    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.ll) LinearLayout ll;
    @BindView(R.id.rl) RelativeLayout rl;
    @BindView(R.id.v_point_red) View redPoint;

    int[] ids=null;
    List<ImageView> iList=null;
    private int pointMoveWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        // 绑定组件
        initView();
        // 初始化数据
        initData();
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    private void initData() {
        ImageView imageView=null;

        ids = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};//初始化引导页图片
        iList = new ArrayList<ImageView>();// 创建一个ListView<ImageView>来存储图片

        for (int i = 0; i < ids.length; i++) {
            imageView = new ImageView(WelcomeActivity.this);
            // 给当前ImageView设置背景图
            imageView.setBackgroundResource(ids[i]);
            iList.add(imageView);
        }

        // 给ViewPager适配Item一般都ImageView
        viewpager.setAdapter(new WelcomePage(ids,iList));

        // 动态给线性 布局添加三个小灰点
        for (int i = 0; i < iList.size(); i++) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.welcome_point_gray);
            int px= DisplayUtil.dip2px(this,10);
            // 代码中所有的数字的单位都是像素 px
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(px,px);
            if(i>0){
                param.leftMargin = DisplayUtil.px2dip(this,20); // 20px
            }
            view.setLayoutParams(param);
            ll.addView(view);
        }

        ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i("jxy", "onGlobalLayout........");
                Log.i("jxy", "0point:" + ll.getChildAt(0).getLeft() + ",1point:" + ll.getChildAt(1).getLeft());
                pointMoveWidth = ll.getChildAt(1).getLeft() - ll.getChildAt(0).getLeft();
                ll.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });



        // 注册一个监听页面切换事件
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * @param position 当前页面下标
             * @param positionOffset 移动距离的百分比
             * @param positionOffsetPixels 移动像素
             * @discription viewpager滑动的时候实时执行该方法
             */

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("jxy", "当前页面的索引:" + position + ",移动距离百分比:" + positionOffset + ",移动的像素:" + positionOffsetPixels);
                // 获取red_poinrt的组件,并且设置参数
//                View redPoint = findViewById(R.id.red_point);
//                RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams)redPoint.getLayoutParams();
//                param.leftMargin = (int)(pointMoveWidth * positionOffset) + position * pointMoveWidth;
//                redPoint.setLayoutParams(param);

            }

            /**
             *
             * @param position 当前页面下标
             * @discription 切换成功后执行该方法
             */
            @Override  // 切换成功才会执行
            public void onPageSelected(int position) {
                Log.i("jxy", "当前被选择的页面:" + position);
                RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams)redPoint.getLayoutParams();
                param.leftMargin = position * pointMoveWidth;
                redPoint.setLayoutParams(param);
            }

            /**
             *
             * @param state :页面的状态: 0 代表未移动   1: 正在移动   2： 正在切换
             * @diccription 移动的时候状态发生变化的时候执行该
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("jxy", "state:" + state);
            }
        });
    }

    /**
     * 创建viewpagerr适配器继承PagerAdapter
     *
     */
    private class WelcomePage extends PagerAdapter {

        private int[] ids = null;
        private List<ImageView> iList = null;

        public WelcomePage(int[] ids,List<ImageView> iList) {
            this.ids=ids;
            this.iList=iList;
        }

        /**
         * 获取集合的大小
         */
        @Override
        public int getCount() {
            return iList.size();
        }

        /**
         *
         * @discription 实例化每一个Item,其实就是View
         * @param container 当前的viewpager
         * @param position 当前下标
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = (ImageView) iList.get(position);

            Log.i("jxy", "当前的ViewPager对象:" + container + ",position:" + position + ",ImageView:" + view);
            // 返回之前必须把当前View对象添加到容器中
            container.addView(view); // lv.addView(view); 只能使用适配器

            return view;
        }

        /**
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i("jxy", "当前销毁的对象:" + object);
            container.removeView((ImageView) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
