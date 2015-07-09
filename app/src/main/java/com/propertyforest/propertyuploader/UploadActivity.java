package com.propertyforest.propertyuploader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends Activity {
    // LogCat tag
    private static final String TAG = UploadActivity.class.getSimpleName();

    private String rent_id = "1";
    public ArrayList<String> map = new ArrayList<String>();
    Bundle b;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        b = getIntent().getExtras();
        btn = (Button) findViewById(R.id.finishUploading);
        ListView listView = (ListView) findViewById(R.id.downloadListView);

        List<UploadInfo> downloadInfo = new ArrayList<UploadInfo>();

        if (b != null) {
            ArrayList<String> ImgData = b.getStringArrayList("IMAGE");
            rent_id = b.getString("rent_id");
            for (int i = 0; i < ImgData.size(); i++) {
                map.add(ImgData.get(i).toString());
                File f = new File(map.get(i));
                long size = f.getTotalSpace();
                downloadInfo.add(new UploadInfo(map.get(i), size,rent_id));

            }

        }
        listView.setAdapter(new UploadInfoAdapter(this, R.id.downloadListView, downloadInfo));

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(UploadActivity.this,MainActivity.class);
                startActivity(i);
            }
        }
        );

    }

    }

