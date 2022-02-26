package com.shyptsolution.hackathon.RecyclerView;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APi {

    public void send(Context context) throws IOException {
        try{

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("username","raunit")
                    .addFormDataPart("password","raunit")
                    .build();
            Request request = new Request.Builder()
                    .url("https://mental-diaries.herokuapp.com/api/users/register/")
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();
            Toast toast=Toast. makeText(context,response.toString(),Toast. LENGTH_LONG);
            toast.show();
        }
        catch (Exception e){
            Toast toast=Toast. makeText(context,e.toString(),Toast. LENGTH_LONG);
            toast.show();
        }

    }
}
