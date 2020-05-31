package org.demo.aty;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.daimhim.guideview.GuideBuilder;
import org.daimhim.guideview.guide.AbsGuide;
import org.daimhim.guideview.view.MaskView;
import org.demo.component.LottieComponent;
import org.demo.component.MutiComponent;
import org.demo.component.SimpleComponent;
import com.demo.guide.R;

public class SimpleGuideViewActivity extends AppCompatActivity {

  private Button header_imgbtn;
  private LinearLayout ll_nearby, ll_video;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_simple_guide_view);
    Window window = getWindow();
//    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    header_imgbtn = (Button) findViewById(R.id.header_imgbtn);
    header_imgbtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showGuideView();
        Toast.makeText(SimpleGuideViewActivity.this, "show", Toast.LENGTH_SHORT).show();
      }
    });
    ll_nearby = (LinearLayout) findViewById(R.id.ll_nearby);
    ll_video = (LinearLayout) findViewById(R.id.ll_video);
    ll_nearby.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(v.getContext(),"我被点了",Toast.LENGTH_SHORT).show();
      }
    });
//    header_imgbtn.post(new Runnable() {
//      @Override
//      public void run() {
//        showGuideView();
//      }
//    });
  }

  public void showGuideView() {
    GuideBuilder builder = new GuideBuilder();
    builder.addTargetView(header_imgbtn,20,0)
            .setOutsideTouchable(false)
            .focusClick(true)
            .setCancelable(false)
            .setFullingLayoutId(R.layout.test_guide_layout_activity)
            .setShowMode(MaskView.GUIDE_LAYOUT_SHOW)
            .setAlpha(150);
    builder.setOnCancelListener(new GuideBuilder.OnCancelListener() {
      @Override
      public void onCancel(AbsGuide guide) {
        Toast.makeText(SimpleGuideViewActivity.this,"onCancel",Toast.LENGTH_SHORT).show();
      }
    });
    builder.addComponent(new SimpleComponent(header_imgbtn.getId()));
    AbsGuide guide = builder.createGuide();
    guide.show(SimpleGuideViewActivity.this);
  }

  public void showGuideView2() {
    final GuideBuilder builder1 = new GuideBuilder();
    builder1.addTargetView(ll_nearby)
            .setShowMode(MaskView.DIALOG_SHOW)
            .setAlpha(150);
    builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
      @Override
      public void onShown() {
      }

      @Override
      public void onDismiss() {
        showGuideView3();
      }
    });

    builder1.addComponent(new MutiComponent(ll_nearby.getId()));
    AbsGuide guide = builder1.createGuide();
    guide.show(SimpleGuideViewActivity.this);
  }

  public void showGuideView3() {
    final GuideBuilder builder1 = new GuideBuilder();
    builder1.addTargetView(ll_video)
            .setAlpha(150)
            .setShowMode(MaskView.DIALOG_SHOW)
            .setHighTargetCorner(20)
            .setHighTargetPadding(10);
//            .setExitAnimationId(android.R.anim.fade_out);
    builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
      @Override
      public void onShown() {
      }

      @Override
      public void onDismiss() {
      }
    });

    builder1.addComponent(new LottieComponent(ll_video.getId()));
    AbsGuide guide = builder1.createGuide();
    guide.setShouldCheckLocInWindow(false);
    guide.show(SimpleGuideViewActivity.this);
  }
}
