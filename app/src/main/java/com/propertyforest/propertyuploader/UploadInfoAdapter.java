package com.propertyforest.propertyuploader;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.propertyforest.propertyuploader.UploadInfo.DownloadState;

import java.util.List;

/**
 * Created by fash on 13/4/15.
 */
public class UploadInfoAdapter extends ArrayAdapter<UploadInfo> {
    // Simple class to make it so that we don't have to call findViewById frequently
    private static class ViewHolder {
        TextView textView;
        ProgressBar progressBar;
        Button button;
        UploadInfo info;
    }


    private static final String TAG = UploadInfoAdapter.class.getSimpleName();

    public UploadInfoAdapter(Context context, int textViewResourceId,
                             List<UploadInfo> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final UploadInfo info = getItem(position);
        // We need to set the convertView's progressBar to null.

        ViewHolder holder = null;

        if (null == row) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.file_upload_row, parent, false);

            holder = new ViewHolder();
            holder.textView = (TextView) row.findViewById(R.id.downloadFileName);
            holder.progressBar = (ProgressBar) row.findViewById(R.id.downloadProgressBar);
            holder.button = (Button) row.findViewById(R.id.downloadButton);
            holder.info = info;

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();

            holder.info.setProgressBar(null);
            holder.info = info;
            holder.info.setProgressBar(holder.progressBar);
        }

        holder.textView.setText(info.getFilename());
        holder.progressBar.setProgress(info.getProgress());
        holder.progressBar.setMax(100);
        info.setProgressBar(holder.progressBar);

        holder.button.setEnabled(info.getDownloadState() == DownloadState.NOT_STARTED);
        final Button button = holder.button;
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setDownloadState(DownloadState.QUEUED);
                button.setEnabled(false);
                button.invalidate();
                FileUploadTask task = new FileUploadTask(info);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });


        //TODO: When reusing a view, invalidate the current progressBar.

        return row;


    }
}
