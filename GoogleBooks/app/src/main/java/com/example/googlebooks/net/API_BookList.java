package com.example.googlebooks.net;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.googlebooks.Utility;
import com.example.googlebooks.models.BookModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.ResponseCache;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class API_BookList extends AsyncTask<Void,Void,Void> {
    final String BASE_URL="https://www.googleapis.com/books/v1/volumes?";
    final int NO_MATCHING_RESULT = -1;
    private  String q="";
    private  String filter ="";
    private  int maxResults = 10;
    private  String orderBy = "";
//    private  String printType = "all";
    private int resultCode;
    private  String mResponseStr;
    private ResponseClass mResponseClass;
    private Context mContext;

    public void setQ(String q) {
        this.q = q;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }


    public ResponseClass getmResponseClass() {
        return mResponseClass;
    }

    public int getResultCode() {
        return resultCode;
    }

    //完成通知主執行序可以進行下一步
    public interface JobCompletedListener{
        void OnJobCompleted(API_BookList sender);
    }
    private JobCompletedListener OnJobCompleted;

    public void setOnJobCompleted(JobCompletedListener onJobCompleted) {
        OnJobCompleted = onJobCompleted;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {
            //API網址參數
            Uri builtURI = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter("q", q)
                    .appendQueryParameter("filter",filter)
                    .appendQueryParameter("maxResults",String.valueOf(maxResults))
                    .appendQueryParameter("orderBy",orderBy)    //.appendQueryParameter("printType",printType)
                    .build();
            URL requestURL = new URL(builtURI.toString());
            Utility.printLog(requestURL.toString());

            HttpURLConnection connection = (HttpURLConnection)requestURL.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            resultCode = connection.getResponseCode();

            if(resultCode == HttpURLConnection.HTTP_OK){
                StringBuilder builder = new StringBuilder();
                String line;
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                while (((line = bufferedReader.readLine())) != null){
                    builder.append(line+"\n");
                }
                if(builder.length() == 0){
                    return null;
                }
                mResponseStr = builder.toString();
                Utility.printLog(mResponseStr);
            }else{
                String line;
                String mErrorResponseStr = "";
                inputStream = connection.getErrorStream();
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                while ((line = bufferedReader.readLine()) != null){
                    mErrorResponseStr += line;
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                }
                catch (Exception ex ){
                    ex.printStackTrace();
                }
            }
            if(inputStreamReader != null){
                try {
                    inputStreamReader.close();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }

        try {
            JSONObject jsonObject = new JSONObject(mResponseStr);
            mResponseClass = new ResponseClass();
            mResponseClass.setKind(jsonObject.getString("kind"));
            mResponseClass.setTotalItem(jsonObject.getInt("totalItems"));
            if(mResponseClass.getTotalItem() > 0){
                JSONArray items = jsonObject.getJSONArray("items");
                Log.d("Test",items.length()+"");
                ArrayList<BookModel> bookModels = new ArrayList<>();
                for (int i = 0; i < items.length(); i++){
                    BookModel model = new BookModel();
                    JSONObject item = items.getJSONObject(i);
                    model.setKind(Utility.getJsonString(item,"kind"));
                    model.seteTag(Utility.getJsonString(item,"etag"));
                    model.setSelfLink(Utility.getJsonString(item,"selfLink"));
                    //set 顯示的資料volumeInfo
                    JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                    model.getVolumeInfo().setTitle(Utility.getJsonString(volumeInfo,"title"));
                    model.getVolumeInfo().setSubtitle(Utility.getJsonString(volumeInfo,"subtitle"));
                    model.getVolumeInfo().setAuthors(Utility.getAuthorArrayToString(volumeInfo,"authors"));
                    model.getVolumeInfo().setPublisher(Utility.getJsonString(volumeInfo,"publisher"));
                    model.getVolumeInfo().setPublishedDate(Utility.getJsonString(volumeInfo,"publishedDate"));
                    model.getVolumeInfo().setImg_Url(Utility.getUrlToString(volumeInfo,"imageLinks"));
                    //get saleInfo buyLink and set
                    JSONObject saleInfo = item.getJSONObject("saleInfo");
                    model.getSaleInfo().setBuyLink(Utility.getJsonString(saleInfo,"buyLink"));
                    bookModels.add(model);
                }
                mResponseClass.setItems(bookModels);
            }
            else{
                resultCode = NO_MATCHING_RESULT;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            resultCode = NO_MATCHING_RESULT;
        }
        if(OnJobCompleted != null){
            OnJobCompleted.OnJobCompleted(this);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

    public class ResponseClass {
        private String kind;
        private int totalItem;
        private ArrayList<BookModel> items;

        public void setKind(String kind){
            this.kind = kind;
        }
        public String getKind(){
            return kind != null ? kind : "" ;
        }
        public void setTotalItem(int totalItem){
            this.totalItem = totalItem ;
        }
        public int getTotalItem(){
            return totalItem;
        }
        public ArrayList<BookModel> getItems() {
            return items;
        }
        public void setItems(ArrayList<BookModel> items) {
            this.items = items;
        }
    }
}
