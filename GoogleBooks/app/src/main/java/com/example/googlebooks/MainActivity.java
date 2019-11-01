package com.example.googlebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.googlebooks.models.BookModel;
import com.example.googlebooks.net.API_BookList;

import org.json.JSONArray;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Dictionary;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public String q; //書名
    public String filter; //種類
    public int maxResults; //查詢數量 10~40
    public String orderBy; //排序

    private EditText etxt;
    private Spinner spinner;
    private SeekBar Skb;
    private TextView txt_Quantity;
    private RadioGroup radioGroup;
    private RadioButton radioN;
    private RadioButton radioR;
    private Button btn ;

    boolean isClick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("User" , MODE_PRIVATE);

        setContentView(R.layout.activity_main);

        etxt = findViewById(R.id.eTxt_BookName);
        spinner = findViewById(R.id.spinner_BookFilter);
        txt_Quantity = findViewById(R.id.txt_QuantityNum);
        Skb = findViewById(R.id.skBar_Quantity);

        txt_Quantity.setText(Skb.getProgress()+"");
        radioGroup = findViewById(R.id.radio_Group);
        radioN = findViewById(R.id.radioBtn_New);
        radioR = findViewById(R.id.radioBtn_Relevance);
        btn = findViewById(R.id.btn_Search);

        //加入spinner iTem
        String[] bookFilter = {"ebooks","free-ebooks","full","paid-ebooks","partial"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bookFilter);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        //Filter
        String tmp_Filter = sharedPreferences.getString("Filter", "");//讀取儲存條件

        if(tmp_Filter != ""){
            if(tmp_Filter.equals("ebooks")){//全部電子書
                spinner.setSelection(0);
            }
            else if(tmp_Filter.equals("free-ebooks")){ //免費電子書
                spinner.setSelection(1);
            }
            else if(tmp_Filter.equals("full")){ //全部閱讀
                spinner.setSelection(2);
            }
            else if(tmp_Filter.equals("paid-ebooks")){ //付費電子書
                spinner.setSelection(3);
            }
            else{//
                spinner.setSelection(4);//部分閱讀
            }
        }
        //seekBar
        int tmp_Seek = sharedPreferences.getInt("Quantity", 10);//讀取儲存條件
        if(tmp_Seek != 10){
            Skb.setProgress(tmp_Seek);
            txt_Quantity.setText(tmp_Seek+"");
        }
        //radioBtn
        String tmp_Radio = sharedPreferences.getString("OrderBy", "");//讀取儲存條件
        if(tmp_Radio != ""){
            if(tmp_Radio.equals("newest")){
                radioN.setChecked(true);
            }
            else{
                radioR.setChecked(true);
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q = etxt.getText().toString();
                if("".equals(q.trim())){ //防呆 不可空白送出
                    Toast.makeText(getApplicationContext(),"不要那麼調皮",Toast.LENGTH_SHORT).show();
                }
                filter = spinner.getSelectedItem().toString();
                maxResults = Integer.parseInt(txt_Quantity.getText().toString());
                orderBy =((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId()))
                                .getText().toString();
                //將值存入sharedPreferences
                sharedPreferences.edit().putString("Filter", filter).apply();
                sharedPreferences.edit().putInt("Quantity", maxResults).apply();
                sharedPreferences.edit().putString("OrderBy", orderBy).apply();
                //防止Button連續觸發多次API
                if(isClick == false) {
                    isClick = true;
                    API_BookList apiBookList = new API_BookList();
                    apiBookList.setQ(q);
                    apiBookList.setFilter(filter);
                    apiBookList.setMaxResults(maxResults);
                    apiBookList.setOrderBy(orderBy);
                    apiBookList.setmContext(getApplicationContext());
                    apiBookList.setOnJobCompleted(new API_BookList.JobCompletedListener() {
                        @Override
                        public void OnJobCompleted(API_BookList sender) {
                            if (sender.getResultCode() == HttpURLConnection.HTTP_OK) { //連線成功
                                API_BookList.ResponseClass responseClass = sender.getmResponseClass();
                                ArrayList<BookModel> bookModels = responseClass.getItems();
                                BookModel[] models = new BookModel[bookModels.size()];
                                bookModels.toArray(models);
                                Bundle bundle = new Bundle();
                                bundle.putParcelableArray(Utility.KEY_PARAMS, models);
                                //防止API未完成被觸發多次查詢事件
                                isClick = false;

                                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                                intent.putExtra(Utility.KEY_PARAMS, bundle);
                                startActivity(intent);

                            } else {
                                //Error handle 查無資料
                                if (sender.getResultCode() == -1) {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                Looper.prepare();
                                                Toast.makeText(getApplicationContext(), "查無資料", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                            } catch (Exception e) {
                                                Utility.printLog(e.getMessage());
                                            }
                                        }
                                    }.start();
                                }
                            }
                        }
                    });
                    apiBookList.execute();
                }
//                try{
//                    if(apiBookList.getResultCode() != 0){
//                        Toast.makeText(getApplicationContext(),"查無資料",Toast.LENGTH_LONG).show();
//                    }
//                }
//               catch (Exception ex){
//                    ex.printStackTrace();
//               }
            }
        });
        //SeekBar
        Skb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//SeekBar 數量
                txt_Quantity.setText(progress +"");//顯示數字
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

}
