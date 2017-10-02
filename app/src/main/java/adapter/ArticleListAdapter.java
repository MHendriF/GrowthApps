package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.trikarya.growth.ArticleDetail;
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
import master.ArticleClass;
import network.ConnectionHandler;
import network.JsonCallback;
import network.ArticleNetwork;
import utility.ImageHelper;
import android.trikarya.growth.R;

/**
 * Created by Hendry on 9/26/2017.
 */

public class ArticleListAdapter extends ArrayAdapter<ArticleClass> {
    Context context;
    List<ArticleClass> articleClasses;
    LayoutInflater layoutInflater;
    ImageHelper imageHelper;
    ArticleNetwork articleNetwork;
    DatabaseHandler databaseHandler;
    int width;
    List<Bitmap> image;
    List<Integer> positions;

    public ArticleListAdapter(Context context, List<ArticleClass> articleClasses, int width){
        super(context, R.layout.draft_item, articleClasses);
        this.context = context;
        this.articleClasses = articleClasses;
        imageHelper = new ImageHelper(context);
        layoutInflater = LayoutInflater.from(context);
        this.width = width;
        articleNetwork = new ArticleNetwork(context);
        databaseHandler = new DatabaseHandler(context);
        image = new ArrayList<>();
        positions = new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.article_list_item,parent,false);
        final ArticleClass currentArticle = articleClasses.get(position);
        LinearLayout group = (LinearLayout) convertView.findViewById(R.id.group);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ArticleDetail.class);
                intent.putExtra("data", currentArticle);
                context.startActivity(intent);
            }
        });
        final RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.container_article);
        final ImageView imArticle = (ImageView) convertView.findViewById(R.id.im_article);
        TextView headline = (TextView) convertView.findViewById(R.id.headline);
        headline.setText(currentArticle.getJudul());
        TextView tgl = (TextView) convertView.findViewById(R.id.news_date);
        tgl.setText(currentArticle.getDate_upload().split(" ")[0]);
        final TextView warning = (TextView) convertView.findViewById(R.id.message);
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        if(positions.size() < position + 1 && !positions.contains(position)) {
            if(currentArticle.getImage().contains(".jpg") || currentArticle.getImage().contains(".png")) {
                articleNetwork.getDetail(currentArticle.getId(), new JsonCallback() {
                    @Override
                    public void Done(JSONObject jsonObject, String message) {
                        if (jsonObject != null && message.equals(ConnectionHandler.response_message_success)) {
                            try {
                                currentArticle.setImage(jsonObject.getString(ConnectionHandler.response_data));
                                databaseHandler.updateArticle(currentArticle);
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
                                imArticle.setImageBitmap(bitmap);
                                imArticle.setVisibility(View.VISIBLE);
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
                byte[] decodedString = Base64.decode(currentArticle.getImage(), Base64.DEFAULT);
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
                imArticle.setImageBitmap(bitmap);
                imArticle.setVisibility(View.VISIBLE);
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
                imArticle.setImageBitmap(bitmap);
                imArticle.setVisibility(View.VISIBLE);
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
