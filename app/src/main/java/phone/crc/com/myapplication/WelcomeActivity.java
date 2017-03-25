package phone.crc.com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends Activity {

    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.ll) LinearLayout ll;
    @BindView(R.id.rl) RelativeLayout rl;

    int[] ids=null;
    List<ImageView> iList=null;

    Handler handler= new Handler(){

        public void handleMessage(android.os.Message msg) {

            View redview= new View(WelcomeActivity.this);
            redview.setBackgroundResource(R.drawable.welcome_point_red);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(40,40);
            // 代码中所有的数字的单位都是像素 px
            switch(msg.what) {
                case 0:
                    //param.leftMargin = 20; // 20px

                    redview.setLayoutParams(param);
                    rl.addView(redview);
                    break;
                case 1:

                    param.leftMargin = 40; // 20px

                    redview.setLayoutParams(param);
                    rl.addView(redview);
                    break;
                case 3:

                    param.leftMargin = 90; // 20px

                    redview.setLayoutParams(param);
                    rl.addView(redview);
                    break;

                default: break;
            }

        }
    };

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
        ids = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
        iList = new ArrayList<ImageView>();

        // 创建一个ListView<ImageView>来存储图片,
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(WelcomeActivity.this);
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
            // 代码中所有的数字的单位都是像素 px
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(40,40);
            if(i>0){
                param.leftMargin = 20; // 20px
            }
            view.setLayoutParams(param);
            ll.addView(view);
        }





    }

    private class WelcomePage extends PagerAdapter {

        private int[] ids = null;
        private List<ImageView> iList = null;

        public WelcomePage(int[] ids,List<ImageView> iList) {
            this.ids=ids;
            this.iList=iList;
        }

        @Override  // 返回集合大小
        public int getCount() {
            return iList.size();
        }

        @Override // 实例化每一个Item,其实就是View
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = (ImageView) iList.get(position);


            Message msg=Message.obtain();
            msg.what=position;
            handler.sendMessage(msg);


            Log.i("jxy", "当前的ViewPager对象:" + container + ",position:" + position + ",ImageView:" + view);
            // 返回之前必须把当前View对象添加到容器中
            container.addView(view); // lv.addView(view); 只能使用适配器
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i("jxy", "当前销毁的对象:" + object);
            container.removeView((ImageView) object);
        }

        @Override  //
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
