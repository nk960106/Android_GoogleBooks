package com.example.googlebooks.models;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlebooks.DetailActivity;
import com.example.googlebooks.net.DownloadImageTask;
import com.example.googlebooks.R;
import com.example.googlebooks.Utility;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.BookViewHolder> {
    private ArrayList<BookModel> mBookList;
    private LayoutInflater mInflater;
    private Context mContext;


    public Adapter(Context context, ArrayList<BookModel> mBookList){
        mInflater = LayoutInflater.from(context);
        this.mBookList = mBookList;
        mContext = context;
    }

    @NonNull
    @Override
    public Adapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.item_layout , parent,false);
        BookViewHolder holder = new BookViewHolder(mItemView);
        holder.ItemTitle = mItemView.findViewById(R.id.txt_ItemTitle);
        holder.ItemSubTitle = mItemView.findViewById(R.id.txt_SubTitle);
        holder.ItemAuthors = mItemView.findViewById(R.id.txt_Authors);
        holder.imageView = mItemView.findViewById(R.id.imgView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.BookViewHolder holder, int position) {
        BookModel mCurrent = mBookList.get(position);
        holder.ItemTitle.setText(mCurrent.getVolumeInfo().getTitle());
        holder.ItemSubTitle.setText(mCurrent.getVolumeInfo().getSubtitle());
        holder.ItemAuthors.setText(mCurrent.getVolumeInfo().getAuthors());
        new DownloadImageTask(holder.imageView).execute(mCurrent.getVolumeInfo().getImg_Url());//呼叫下載圖片function
        holder.arrayList.clear(); //先清空ArrayList
        holder.arrayList.add(mCurrent);//將選擇的資料加入 ArrayList
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        private TextView ItemTitle;
        private TextView ItemSubTitle;
        private TextView ItemAuthors;
        private ImageView imageView;
        ArrayList<BookModel> arrayList = new ArrayList<BookModel>();
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //將資料使用bundle傳送到第3頁DetailActivity
                    ArrayList<BookModel> bookModels = arrayList;
                    BookModel[] models = new BookModel[bookModels.size()];
                    bookModels.toArray(models);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArray(Utility.KEY_PARAMS, models);

                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra(Utility.KEY_PARAMS, bundle);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
