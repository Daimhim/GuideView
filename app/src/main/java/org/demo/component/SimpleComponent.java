package org.demo.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.daimhim.guideview.view.Component;
import com.demo.guide.R;

/**
 * Created by binIoter on 16/6/17.
 */
public class SimpleComponent extends Component {

  public SimpleComponent(int anchorId) {
    super(anchorId, Component.ANCHOR_BOTTOM, Component.FIT_CENTER, 0, 0);
  }

  @Override
  public View getView(LayoutInflater inflater) {
    View ll = inflater.inflate(R.layout.layer_frends,null);
    ll.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Toast.makeText(view.getContext(), "引导层被点击了", Toast.LENGTH_SHORT).show();
      }
    });
    return ll;
  }
}
