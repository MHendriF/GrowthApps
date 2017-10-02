package android.trikarya.growth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import master.ArticleClass;
import network.ConnectionHandler;
import network.JsonCallback;
import network.ArticleNetwork;
import utility.ImageHelper;

/**
 * Created by Hendry on 9/26/2017.
 */

public class ArticleDetail extends AppCompatActivity {
    TextView judul,headline,tanggal,isi,warning;
    ImageView imageView;
    ProgressBar progressBar;
    ArticleNetwork articleNetwork;
    ImageHelper imageHelper;
    RelativeLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);
        ((TextView)getSupportActionBar().getCustomView().findViewById(R.id.title)).setText("NEWS DETAIL");
        getSupportActionBar().getCustomView().findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        articleNetwork = new ArticleNetwork(this);
        imageHelper = new ImageHelper(this);
        container = (RelativeLayout) findViewById(R.id.top_panel);
        judul = (TextView) findViewById(R.id.judul);
        warning = (TextView) findViewById(R.id.message);
        headline = (TextView) findViewById(R.id.headline);
        tanggal = (TextView) findViewById(R.id.date);
        isi = (TextView) findViewById(R.id.content);
        imageView = (ImageView) findViewById(R.id.im_article);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final ArticleClass articleClass = (ArticleClass) getIntent().getSerializableExtra("data");
        if(articleClass.getImage().contains(".jpg") || articleClass.getImage().contains(".png")){
            articleNetwork.getDetail(articleClass.getId(), new JsonCallback() {
                @Override
                public void Done(JSONObject jsonObject, String message) {
                    if (jsonObject != null && message.equals(ConnectionHandler.response_message_success)) {
                        try {
                            Point size = new Point();
                            ArticleDetail.this.getWindowManager().getDefaultDisplay().getSize(size);
                            int width = size.x;
                            articleClass.setImage(jsonObject.getString(ConnectionHandler.response_data));
                            byte[] decodedString = Base64.decode(jsonObject.getString(ConnectionHandler.response_data), Base64.DEFAULT);
                            Bitmap bitmap = imageHelper.getFitScreenBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length), width);
                            progressBar.setVisibility(View.GONE);
                            int height = width * bitmap.getHeight() / bitmap.getWidth();
                            float d = ArticleDetail.this.getResources().getDisplayMetrics().density;
                            int margin = (int) (16 * d); // margin in pixels
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, height);
                            layoutParams.setMargins(0, 0, 0, margin);
                            container.setLayoutParams(layoutParams);
                            imageView.setImageBitmap(bitmap);
                            imageView.setVisibility(View.VISIBLE);
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
        else {
            byte[] decodedString = Base64.decode(articleClass.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        judul.setText(articleClass.getJudul());
        headline.setText(articleClass.getHeadline());
        tanggal.setText(articleClass.getDate_upload().split(" ")[0]);
        isi.setText(Html.fromHtml(articleClass.getContent()));
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

}
