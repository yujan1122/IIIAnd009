package tw.org.iii.iiiand09;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private ImageView img;
    private Bitmap bitmap;
    private UIHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new UIHandler();
        img = findViewById(R.id.img);
        queue = Volley.newRequestQueue(this);
    }

    public void test1(View view) {
        new Thread(){ //執行序物件實體
            public void run(){  //做什麼事
                test11();
            }
        }.start(); //物件要呼叫方法才會出動
    }
    private void test11(){
        try {
            URL url = new URL("http://10.0.2.2/brad01.html");//new URL出現紅線,用try catch解決
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();//呼叫得到連線,強制轉型
            conn.connect();//去連線吧,呼叫方法去做動作
            conn.getInputStream();

            //燒開水  ==> 茶壺去裝水,拿去瓦斯爐燒, 燒開後倒入茶葉壺,
            //管子一根接一根
            InputStream in = conn.getInputStream();
            InputStreamReader irs = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(irs);

            String line;
            while ((line = br.readLine()) != null ){
                Log.v("brad",line);
            }
            br.close();

        }catch (Exception e){
            Log.v("brad",e.toString());
        }
    }

    public void test2(View view) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("brad",response);
                parseJSON(response);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("brad","error: " + error.toString());
            }
        };
        StringRequest request = new StringRequest(Request.Method.GET,
                "http://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvAttractions.aspx",
                listener,errorListener);
        queue.add(request);

    }

    private void parseJSON(String json){
        try {
            JSONArray root = new JSONArray(json);
            for (int i=0; i<root.length(); i++){
                JSONObject row = root.getJSONObject(i);
                String title = row.getString("Title");
                Log.v("brad",title);

            }
        }catch (Exception e){
            Log.v("brad",e.toString());

        }
    }

    //圖檔
    public void test3(View view){
        new Thread(){
            @Override
            public void run() {
                test13();
            }
        }.start();
    }

    private void test13(){
        try {
            URL url = new URL("https://s.yimg.com/uu/api/res/1.2/7bAeY6RVTpBXHaiWikKBLQ--~B/Zmk9dWxjcm9wO2N3PTcyMztkeD0wO2NoPTQzNDtkeT0xMjt3PTM5MjtoPTMwODtjcj0xO2FwcGlkPXl0YWNoeW9u/https://media-mbst-pub-ue1.s3.amazonaws.com/creatr-uploaded-images/2019-11/ead84020-0a9f-11ea-acf6-75d608fabb1a");//new URL出現紅線,用try catch解決
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();//呼叫得到連線,強制轉型
            conn.connect();//去連線吧,呼叫方法去做動作

            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            handler.sendEmptyMessage(0);

        }catch (Exception e){
            Log.v("brad",e.toString());
        }

    }

    private class UIHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            img.setImageBitmap(bitmap);
        }
    }
}
