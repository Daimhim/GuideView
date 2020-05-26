package com.demo.component;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.binioter.guideview.view.Component;
import com.demo.guide.R;

/**
 * Created by binIoter on 16/6/17.
 */
public class LottieComponent extends Component {

  public LottieComponent(int anchorId) {
    super(anchorId, Component.ANCHOR_TOP, Component.FIT_CENTER, 0, -30);
  }

  @Override public View getView(LayoutInflater inflater) {

    LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.layer_lottie, null);
    ll.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Toast.makeText(view.getContext(), "引导层被点击了", Toast.LENGTH_SHORT).show();
      }
    });
    return ll;
  }
}
