package com.shyptsolution.hackathon.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APi {

    public String send(Context context,String entry,String title,String username) throws IOException {
        try{
            String user=username;
            String titl=title;
            String ent=entry;
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"raunit\": \"\",\r\n    \"entry\": \"I am depressed\",\r\n    \"entry_title\": \"sad hackathon round\"\r\n}");
            Request request = new Request.Builder()
                    .url("https://mental-diaries.herokuapp.com/api/diary/entry/")
                    .method("POST", body)
                    .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjQ1OTE4ODU2LCJpYXQiOjE2NDU5MTcwNTYsImp0aSI6Ijc2ZTZjNDk4YjY0MDRjNjQ5ODk3ZmY5ZjllOTY1NzQ5IiwidXNlcl9pZCI6NH0.Ix9fFseTq-Dd_aMdG93TYT8o1C9uc1LJdURtuiUR-nQ")
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            String ret=response.message().toString();
            return ret;
//            Toast toast=Toast. makeText(context,response.toString(),Toast. LENGTH_LONG);
//            toast.show();

        }
        catch (Exception e){

        }
        return "";
    }
}
