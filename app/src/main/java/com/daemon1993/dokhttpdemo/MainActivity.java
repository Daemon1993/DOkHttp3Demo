package com.daemon1993.dokhttpdemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.daemon1993.dokhttp.params.DFormParams;
import com.daemon1993.dokhttp.tools.DFileUtil;
import com.daemon1993.dokhttp.DOkHttp;
import com.daemon1993.dokhttp.params.DStreamParams;
import com.daemon1993.dokhttp.params.DStringParams;



import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.bt_get)
    Button btGet;
    @Bind(R.id.bt_post)
    Button btPost;
    @Bind(R.id.bt_upload)
    Button btUpload;
    @Bind(R.id.bt_download)
    Button btDownload;
    @Bind(R.id.tv_show)
    TextView tvShow;
    @Bind(R.id.bt_post_string)
    Button btPostString;

    @Bind(R.id.bt_post_form)
    Button btPostForm;
    @Bind(R.id.bt_post_text)
    Button btPostText;



    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog1;

    private File file;

    public static final String dir = Environment.getExternalStorageDirectory() + "/OkHttpDemo";
    private String TAG = "MAin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        btUpload.setOnClickListener(this);
        btDownload.setOnClickListener(this);
        btPost.setOnClickListener(this);
        btGet.setOnClickListener(this);

        btPostForm.setOnClickListener(this);
        btPostString.setOnClickListener(this);
        btPostText.setOnClickListener(this);


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        File file = new File(dir);
        if (file != null) {
            file.mkdirs();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_download:
                dowmload();
                break;
            case R.id.bt_get:
                getAction();
                break;

            case R.id.bt_post:
                postAction();
                break;

            case R.id.bt_upload:
                upload();
                break;
            case R.id.bt_post_string:
                postString();
                break;
            case R.id.bt_post_text:
                postStringTex();
                break;


        }
    }

    private void postStringTex() {
        DStreamParams dStreamParams = new DStreamParams();
        dStreamParams.setTextFile("lalalalaa");

        Request request = dStreamParams.setUrl("https://api.github.com/markdown/raw")
                .setTag(this)
                .build();

        DOkHttp.getInstance().postData2Server(request, new DOkHttp.MyCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(String json) {

            }
        });
    }


    /**
     * 上传String
     */
    private void postString() {

        DStringParams dStringParams = new DStringParams();

        Request request = dStringParams.getStringRequest(this, "hahhahaha", "https://api.github.com/markdown/raw");
        DOkHttp.getInstance().postData2Server(request, new DOkHttp.MyCallBack() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(String json) {

            }
        });

    }

    private void upload() {


        file = new File(DFileUtil.cachePath + "/down.jpg");

        if (file == null) {
            Toast.makeText(this, "file is null", Toast.LENGTH_SHORT).show();

            return;
        }

        progressDialog.setMessage("upload");
        progressDialog.show();
        progressDialog.setProgress(0);


        DFormParams dFormParams = new DFormParams();
        Request request = dFormParams.addImage("head", file.getName(), file)
                .addString("bindingCode", "358611111111111")
                .setUrl("http://112.74.199.133:8070/bmbb/sys/baby_updateHeadImg.bmb")
                .setTag(this)
                .addProgressListener(new DOkHttp.UIchangeListener() {
                    @Override
                    public void progressUpdate(long bytesWrite, long contentLength, boolean done) {
                        int progress = (int) (bytesWrite * 100 / contentLength);

                        progressDialog.setProgress(progress);

                        tvShow.append(progress + "\n");
                    }
                })
                .build();

        DOkHttp.getInstance().postData2Server(request, new DOkHttp.MyCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(String json) {

            }
        });


    }

    private void postAction() {

        progressDialog1.setMessage("postAction");
        progressDialog1.show();

        RequestBody requestBody = new FormBody.Builder()
                .add("key", "value")
                .build();

        DStringParams dStringParams = new DStringParams();

        Request request = dStringParams
                .post(requestBody)
                .build();


        DOkHttp.getInstance().getData4ServerAsync(request, new DOkHttp.MyCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(String json) {
                tvShow.setText(json);
                progressDialog1.dismiss();
            }
        });

    }

    private void getAction() {

        progressDialog1.setMessage("getAction");
        progressDialog1.show();

        Request request = new Request.Builder()
                .get()
                .tag(this)
                .url("http://publicobject.com/helloworld.txt")
                .build();


        DOkHttp.getInstance().getData4ServerAsync(request, new DOkHttp.MyCallBack() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(String json) {
                tvShow.setText(json);
                progressDialog1.dismiss();
            }
        });


    }

    private void dowmload() {

        progressDialog.setMessage("dowmload");
        progressDialog.setMax(100);
        progressDialog.show();


        Request request = new Request.Builder()
                .tag(this)
                .get()

                .url("http://7xnbj0.com1.z0.glb.clouddn.com/icon.jpg")

                .build();


        DOkHttp.getInstance().download4ServerListener(request, new DOkHttp.MyCallBack_Progress() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Response response) {
                DFileUtil.saveFile(response, "down.jpg");

                file = new File(DFileUtil.cachePath + "/down.jpg");

                progressDialog.dismiss();
            }
        }, new DOkHttp.UIchangeListener() {
            @Override
            public void progressUpdate(long bytesWrite, long contentLength, boolean done) {
                int progress = (int) (bytesWrite * 100 / contentLength);
                Log.e("Download", progress + "");

                progressDialog.setProgress(progress);

                tvShow.append(progress + "\n");
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DOkHttp.getInstance().cancelCallsWithTag(this);
    }
}
