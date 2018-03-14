package com.example.johnny.aplicatieandroid_12;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    public static  final String url="https://api.stackexchange.com/2.2/users?pagesize=10&order=desc&sort=reputation&site=stackoverflow";

    private static final String TAG_ITEMS="items";

    private static final String TAG_ID="id";
    private static final String TAG_NAME="name";
    private static final String TAG_LOCATION="location";
    ListView listView;

    JSONArray items=null;

    ArrayList<HashMap<String,String >> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemsList=new ArrayList<HashMap<String, String>>();
        new GetItems().execute();

    }

    private class GetItems extends AsyncTask{
        @Override
        protected  void onPreExecute(){
            super.onPreExecute();
            pDialog=new ProgressDialog( MainActivity.this);
            pDialog.setMessage("please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String jsonStr= makeJSONCall(url);
            Log.d("Response: ","> "+jsonStr);
            loadJSONObject(jsonStr);

            return null;
        }




        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            if(pDialog.isShowing())
                pDialog.dismiss();


            ListAdapter adapter=new SimpleAdapter(
                    MainActivity.this,
                    itemsList,
                    R.layout.list_item,
                    new String[]{TAG_NAME,TAG_LOCATION},
                    new int[]{R.id.name,R.id.location}
            );

            listView.setAdapter(adapter);
           //setListAdapter(adapter);

        }

        public String makeJSONCall(String url)  {
            String respone=null;
            HttpURLConnection connection;
            try {
                URL urll=new URL(url);
                connection=(HttpURLConnection) urll.openConnection();
                connection.connect();
                respone=connection.getResponseMessage();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }

           /* DefaultHttpClient httpClient= new DefaultHttpClient();

            HttpEntity httpEntity=null;
            HttpRespone httpRespone=null;

            HttpGet httpGet=new HttpGet(url);
            try{
                httpRespone=httpClient.execute(httpGet);
                httpEntity=httpRespone.getEntity();
                respone=EntityUtils.toString(httpEntity);
            }
            catch (ClientProtocolException e){
                e.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }*/
            return respone;
        }

        public void loadJSONObject(String jsonStr){
            if(jsonStr!=null)
            {
                try{
                    JSONObject jsonObj=new JSONObject(jsonStr);

                    items=jsonObj.getJSONArray(TAG_ITEMS);

                    for(int i=0;i<items.length();i++){
                        JSONObject c=items.getJSONObject(i);
                        String id=c.getString(TAG_ID);
                        String name=c.getString(TAG_NAME);
                        String location=c.getString(TAG_LOCATION);

                        HashMap<String,String> item=new HashMap<String, String>();
                        item.put(TAG_ID,id);
                        item.put(TAG_NAME,name);
                        item.put(TAG_LOCATION,location);

                        itemsList.add(item);

                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }else {
                Log.e("ServiceHandler","Couldn't get any data from url");
            }
        }
    }
}
