package dome.etnet.com.checkmqapidome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn1, btn2, btn3;
    TextView respon_statu, respon_time, respon_data, api;
    LinearLayout loading, ll1, ll2;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        loading = findViewById(R.id.loading);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        api = findViewById(R.id.api);
        respon_statu = findViewById(R.id.respon_statu);
        respon_time = findViewById(R.id.respon_time);
        respon_data = findViewById(R.id.respon_data);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.VISIBLE);
        clearUI();
        switch (view.getId()) {
            case R.id.btn1:
                sendRequest(1);
                break;
            case R.id.btn2:
                sendRequest(2);
                break;
            case R.id.btn3:
                sendRequest(3);
                break;
        }
    }

    private OkHttpClient okHttpClient;

    private void sendRequest(int pos) {
        updataUI(false);
        String url = "";
        switch (pos) {
            case 1:
                url = "http://202.62.215.188/selector/mq3/startup_full.php";
                break;
            case 2:
                url = "http://202.62.215.188/content/mq3/commonList.php?code=17,65,75";
                break;
            case 3:
                url = "http://202.62.215.188/content/mq3/quoteStk.php?code=1";
                break;
        }
        api.setText(url);
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updataUI(true);
                        respon_statu.setText("连接失败");
                        respon_data.setText(e.toString());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String data = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updataUI(true);
                        respon_time.setText((response.receivedResponseAtMillis() - response.sentRequestAtMillis()) + "");
                        respon_statu.setText("连接成功.." + response.code());
                        respon_data.setText(Html.fromHtml(data));
                    }
                });
            }
        });

    }

    private void updataUI(boolean type) {
        loading.setVisibility(type ? View.GONE : View.VISIBLE);
        btn1.setEnabled(type);
        btn2.setEnabled(type);
        btn3.setEnabled(type);
    }

    private void clearUI() {
        respon_time.setText("");
        respon_statu.setText("");
        respon_data.setText("");
        api.setText("");
    }
}
