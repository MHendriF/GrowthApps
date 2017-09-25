package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.trikarya.growth.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Hendry on 9/24/2017.
 */

public class BannerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<Bitmap> bitmaps;

    public BannerAdapter(Context context, List<Bitmap> bitmaps) {
        this.context = context;
        this.bitmaps = bitmaps;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View imageLayout = layoutInflater.inflate(R.layout.banner_layout, container, false);

        assert imageLayout != null;
        ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.banner);


        imageView.setImageBitmap(bitmaps.get(position));

        container.addView(imageLayout);

        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
