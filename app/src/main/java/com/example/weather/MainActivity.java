package com.example.weather;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private TextView cityNameTv,TempTV,ConditionTV;
    private EditText cityEdt;

    private ImageView backIV,iconIV,SearchIV;

    private RecyclerView weatherRV;
    private ArrayList<weatherRvModal> weatherRvModalArrayList;
    private weatherRVAdapter weatherRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeRL =findViewById(R.id.idRlHome);
        cityNameTv=findViewById(R.id.idTVCityName);
        TempTV=findViewById(R.id.idTVTemp);
        ConditionTV=findViewById(R.id.idTVCondition);
        cityEdt=findViewById(R.id.idETCity);
        backIV=findViewById(R.id.idIVBack);
        iconIV=findViewById(R.id.idIVIcon);
        SearchIV=findViewById(R.id.idIVSearch);
        weatherRV=findViewById(R.id.idRVWeather);
        weatherRvModalArrayList=new ArrayList<>();
        weatherRVAdapter=new weatherRVAdapter(this,weatherRvModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);

        SearchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String cityName= cityEdt.getText().toString();
                String url="https://api.weatherapi.com/v1/forecast.json?key=955dbb560fa34f4988454516231712&q="+cityName+"&days=1&aqi=no&alerts=no";
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String temperature = response.getJSONObject("current").getString("temp_c")+"°c";
                            cityNameTv.setText(cityName);
                            TempTV.setText(temperature);
                            JSONObject tempObj=response.getJSONObject("current");
                            String condition = tempObj.getJSONObject("condition").getString("text");
                            int isDay=response.getJSONObject("current").getInt("is_day");
                            String conditionIcon=tempObj.getJSONObject("condition").getString("icon");
                            Picasso.get().load("https:".concat(conditionIcon)).into(iconIV);
                            ConditionTV.setText(condition);
                            if(isDay==1){
                                Picasso.get().load("https://media.istockphoto.com/id/1007768414/photo/blue-sky-with-bright-sun-and-clouds.jpg?s=612x612&w=0&k=20&c=MGd2-v42lNF7Ie6TtsYoKnohdCfOPFSPQt5XOz4uOy4=").into(backIV);
                            }else{
                                Picasso.get().load("https://r1.ilikewallpaper.net/iphone-11-pro-wallpapers/download/27124/Paris-Night-France-City-Dark-Eiffel-Tower-iphone-wallpaper-ilikewallpaper_com.jpg").into(backIV);
                            }
                            JSONObject forecastObj=response.getJSONObject("forecast");
                            JSONObject forecastO=forecastObj.getJSONArray("forecastday").getJSONObject(0);

                            JSONArray hourArray=forecastO.getJSONArray("hour");
                            for(int i=0;i<hourArray.length();i++){
                                JSONObject hourObj=hourArray.getJSONObject(i);
                                String time=hourObj.getString("time");
                                String temper=hourObj.getString("temp_c");
                                String img=hourObj.getJSONObject("condition").getString("icon");
                                String wind=hourObj.getString("wind_kph");
                                weatherRvModalArrayList.add(new weatherRvModal(time,temper,img,wind));
                            }
                            weatherRVAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = error.getMessage();
                        if (errorMessage != null) {
                            Log.e("API_ERROR", errorMessage); // Log the error message for debugging
                        }
                        Toast.makeText(MainActivity.this, "Please enter valid city Name", Toast.LENGTH_SHORT).show();
                    }
                });
                Volley.newRequestQueue(MainActivity.this).add(jsonObjectRequest);
            }
        });

    }
}