package com.propertyforest.propertyuploader;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.propertyforest.propertyuploader.UploadInfo.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by fash on 13/4/15.
 */
public class FileUploadTask extends AsyncTask<Void, Integer, Void> {
    private static final String    TAG = FileUploadTask.class.getSimpleName();
    final UploadInfo             mInfo;
    long totalSize = 0;

    public FileUploadTask(UploadInfo info) {
        mInfo = info;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        mInfo.setProgress(values[0]);
        ProgressBar bar = mInfo.getProgressBar();
        if(bar != null) {
            bar.setProgress(mInfo.getProgress());
            bar.invalidate();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d(TAG, "Starting download for " + mInfo.getFilename());
        mInfo.setDownloadState(DownloadState.UPLOADING);

        uploadFile(mInfo.getFilename(),mInfo.getRentId());

        mInfo.setDownloadState(DownloadState.COMPLETE);
        return null;
    }

    private String uploadFile(String filer,String rent_id) {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

        try {
            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });


            File sourceFile = new File(filer);
            FileBody fileBody = new FileBody(sourceFile,"image/jpg");
            StringBody r = new StringBody(rent_id);
            // Adding file data to http body
            entity.addPart("gallery[image]",fileBody );

            // Extra parameters if you want to pass to server
            entity.addPart("gallery[rent_id]", r);
            //entity.addPart("email", new StringBody("abc@gmail.com"));

            totalSize = entity.getContentLength();
            httppost.setEntity(entity);

            // Making server call
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: " + statusCode;
            }

            Log.d("response ", responseString);

        } catch (ClientProtocolException e) {
            responseString = e.toString();
        } catch (IOException e) {
            responseString = e.toString();
        }


        return responseString;

    }
    @Override
    protected void onPostExecute(Void result) {
        mInfo.setDownloadState(DownloadState.COMPLETE);
    }

    @Override
    protected void onPreExecute() {
        mInfo.setDownloadState(DownloadState.UPLOADING);
    }

}
