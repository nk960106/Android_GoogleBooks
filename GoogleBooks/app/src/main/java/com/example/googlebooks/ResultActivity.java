package com.example.googlebooks;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlebooks.models.Adapter;
import com.example.googlebooks.models.BookModel;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        mRecyclerView = findViewById(R.id.list_Book);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));


//        resultList = findViewById(R);
//        resultList
        ArrayList<BookModel> bookModels = new ArrayList<>();

        Intent intent = getIntent();//問系統有沒有東西要給這裡
        Bundle bundle = intent.getBundleExtra(Utility.KEY_PARAMS);
        Parcelable[] parcelables = bundle.getParcelableArray(Utility.KEY_PARAMS);
        if(parcelables != null){
            for(Parcelable parcelable : parcelables){
                bookModels.add((BookModel) parcelable);
            }
        }

        Adapter adapter = new Adapter(this,bookModels);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);//將資料放入RecyclerView
    }
}
