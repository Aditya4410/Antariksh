package com.antariksh.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aditya Singh on 24-04-2017.
 */

public class ImageDownloader extends AsyncTask<String,Void,Bitmap> {
    ImageDownloaded image;
    private final WeakReference<ImageView> imageViewReference;
    private Context context;
    private String url;




    public ImageDownloader(ImageView imageView, String url, Context context) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.url = url;
        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        image=new ImageDownloaded();
        super.onPreExecute();
        Toast.makeText(context,"Downloading Image",Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (isCancelled())
            bitmap=null;
        ImageView imageView = imageViewReference.get();
        if (imageView != null) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }

        image.setImage(bitmap);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
