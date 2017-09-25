package utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by Hendry on 9/20/2017.
 */

public class ImageHelper {
    private Context context;
    private String encodedImage;
    public ImageHelper(Context context){
        this.context = context;
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        /*if(bm != null && !bm.isRecycled()) {
            bm.recycle();
        }*/
        return resizedBitmap;
    }
    public Bitmap getFitScreenBitmap(Bitmap bm, int width){
        if(bm != null){
            int height = width*bm.getHeight()/bm.getWidth();
            bm = getResizedBitmap(bm,width,height);
        }
        return bm;
    }
    public String getStringImage(Bitmap tmp){
        if(tmp!=null) {
            int height = 600*tmp.getHeight()/tmp.getWidth(), width = 600;
            tmp = getResizedBitmap(tmp, width, height);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            tmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        else{
            Toast.makeText(context,"file not found",Toast.LENGTH_LONG);
            encodedImage = "";
        }
        return encodedImage;
    }
    public String getStringImageFix(Bitmap tmp){
        if(tmp!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            tmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        else{
            Toast.makeText(context,"file not found",Toast.LENGTH_LONG);
            encodedImage = "";
        }
        return encodedImage;
    }
}
