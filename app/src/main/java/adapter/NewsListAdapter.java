package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.trikarya.growth.NewsDetail;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import master.DatabaseHandler;
import master.NewsClass;
import network.ConnectionHandler;
import network.JsonCallback;
import network.NewsNetwork;
import utility.ImageHelper;
import android.trikarya.growth.R;

/**
 * Created by Hendry on 9/26/2017.
 */

public class NewsListAdapter extends ArrayAdapter<NewsClass> {
    Context context;
    List<NewsClass> newsClasses;
    LayoutInflater layoutInflater;
    ImageHelper imageHelper;
    NewsNetwork newsNetwork;
    DatabaseHandler databaseHandler;
    int width;
    List<Bitmap> image;
    List<Integer> positions;

    public NewsListAdapter(Context context, List<NewsClass> newsClasses, int width){
        super(context, R.layout.draft_item,newsClasses);
        this.context = context;
        this.newsClasses = newsClasses;
        imageHelper = new ImageHelper(context);
        layoutInflater = LayoutInflater.from(context);
        this.width = width;
        newsNetwork = new NewsNetwork(context);
        databaseHandler = new DatabaseHandler(context);
        image = new ArrayList<>();
        positions = new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.news_list_item,parent,false);
        final NewsClass currentNews = newsClasses.get(position);
        LinearLayout group = (LinearLayout) convertView.findViewById(R.id.group);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetail.class);
                intent.putExtra("data",currentNews);
                context.startActivity(intent);
            }
        });
        final RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.container_news);
        final ImageView imNews = (ImageView) convertView.findViewById(R.id.im_news);
        TextView headline = (TextView) convertView.findViewById(R.id.headline);
        headline.setText(currentNews.getJudul());
        TextView tgl = (TextView) convertView.findViewById(R.id.news_date);
        tgl.setText(currentNews.getDate_upload().split(" ")[0]);
        final TextView warning = (TextView) convertView.findViewById(R.id.message);
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        if(positions.size() < position + 1 && !positions.contains(position)) {
            if(currentNews.getImage().contains(".jpg") || currentNews.getImage().contains(".png")) {
                newsNetwork.getDetail(currentNews.getId(), new JsonCallback() {
                    @Override
                    public void Done(JSONObject jsonObject, String message) {
                        if (jsonObject != null && message.equals(ConnectionHandler.response_message_success)) {
                            try {
                                currentNews.setImage(jsonObject.getString(ConnectionHandler.response_data));
                                databaseHandler.updateNews(currentNews);
                                byte[] decodedString = Base64.decode(jsonObject.getString(ConnectionHandler.response_data), Base64.DEFAULT);
                                Bitmap bitmap = imageHelper.getFitScreenBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), width);
                                progressBar.setVisibility(View.GONE);
                                int height = width * bitmap.getHeight() / bitmap.getWidth();
                                float d = context.getResources().getDisplayMetrics().density;
                                int margin = (int) (16 * d); // margin in pixels
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT, height);
                                layoutParams.setMargins(0, 0, 0, margin);
                                container.setLayoutParams(layoutParams);
                                image.add(bitmap);
                                positions.add(position);
                                imNews.setImageBitmap(bitmap);
                                imNews.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
                                progressBar.setVisibility(View.GONE);
                                warning.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            warning.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
            else{
                byte[] decodedString = Base64.decode(currentNews.getImage(), Base64.DEFAULT);
                Bitmap bitmap = imageHelper.getFitScreenBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), width);
                progressBar.setVisibility(View.GONE);
                int height = width * bitmap.getHeight() / bitmap.getWidth();
                float d = context.getResources().getDisplayMetrics().density;
                int margin = (int) (16 * d); // margin in pixels
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, height);
                layoutParams.setMargins(0, 0, 0, margin);
                container.setLayoutParams(layoutParams);
                image.add(bitmap);
                positions.add(position);
                imNews.setImageBitmap(bitmap);
                imNews.setVisibility(View.VISIBLE);
            }
        }
        else {
            int ind = positions.indexOf(position);
            if(ind < image.size() && ind >= 0) {
                Bitmap bitmap = image.get(ind);
                float d = context.getResources().getDisplayMetrics().density;
                int margin = (int) (16 * d); // margin in pixels
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, bitmap.getHeight());
                layoutParams.setMargins(0, 0, 0, margin);
                container.setLayoutParams(layoutParams);
                imNews.setImageBitmap(bitmap);
                imNews.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
            else{
                progressBar.setVisibility(View.GONE);
                warning.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }
}
