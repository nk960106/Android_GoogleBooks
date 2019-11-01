package com.example.googlebooks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.googlebooks.models.BookModel;
import com.example.googlebooks.net.DownloadImageTask;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private TextView txt_title;
    private TextView txt_subTitle;
    private TextView txt_author;
    private TextView txt_publisher;
    private TextView txt_publishedDate;
    private ImageView imgView;
    private Button btn_Buy;
    Uri uri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        //指派元件給變數
        txt_title = findViewById(R.id.txt_titleD);
        txt_subTitle =  findViewById(R.id.txt_subTitleD);
        txt_author =  findViewById(R.id.txt_authorD);
        txt_publisher =  findViewById(R.id.txt_publisherD);
        txt_publishedDate =  findViewById(R.id.txt_publishedDateD);
        imgView = findViewById(R.id.imageView);
        btn_Buy = findViewById(R.id.btn_Buy);
        txt_title.setMovementMethod(new ScrollingMovementMethod());
        txt_subTitle.setMovementMethod(new ScrollingMovementMethod());
        txt_author.setMovementMethod(new ScrollingMovementMethod());

        ArrayList<BookModel> bookModels = new ArrayList<>();
        Intent intent = getIntent();//問系統有沒有東西要給這裡
        Bundle bundle = intent.getBundleExtra(Utility.KEY_PARAMS);
        Parcelable[] parcelables = bundle.getParcelableArray(Utility.KEY_PARAMS);
        if(parcelables != null){
            for(Parcelable parcelable : parcelables){
                bookModels.add((BookModel) parcelable);
            }
        }

        BookModel mCurrent = bookModels.get(0);//取得bundle裡面的資料

        txt_title.setText(mCurrent.getVolumeInfo().getTitle());
        txt_subTitle.setText(mCurrent.getVolumeInfo().getSubtitle());
        txt_author.setText(mCurrent.getVolumeInfo().getAuthors());
        txt_publisher.setText(mCurrent.getVolumeInfo().getPublisher());
        txt_publishedDate.setText(mCurrent.getVolumeInfo().getPublishedDate());
        new DownloadImageTask(imgView).execute(mCurrent.getVolumeInfo().getImg_Url());
        //放入購買網址
        uri = Uri.parse(mCurrent.getSaleInfo().getBuyLink());
        //判斷是否有購買網址控制button顯示
        if(uri.equals(Uri.EMPTY)){
            btn_Buy.setVisibility(View.INVISIBLE);
        }
        //button監聽
        btn_Buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
            }
        });

    }
}

