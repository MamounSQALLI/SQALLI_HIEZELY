package org.esiea.sqalli_hiezely.ms_app;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

   public DatePickerDialog dpd;


   public void First_button(View v){
        Toast.makeText(getApplicationContext(),getString(R.string.msg),Toast.LENGTH_LONG).show();

    }



    private  RecyclerView rv_biere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            TextView tv_hw = (TextView)findViewById(R.id.tv_hello_world);

                String h =getString(R.string.hello_world);
                String now = DateUtils.formatDateTime(getApplicationContext(),(new Date()).getTime(), DateFormat.FULL);
        DatePickerDialog.OnDateSetListener l = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int dayOfMonth, int monthOfYear, int year) {
                        TextView tv_hw = (TextView)findViewById(R.id.tv_hello_world);
                        int Z = 1;
                        tv_hw.setText(getString(R.string.hello_world) + dayOfMonth +" / " + (monthOfYear+1)+ " / " + year);
                    }
                };
            //    dpd = new DatePickerDialog(this, l , int year, int month, int dayOfMonth);
        DatePickerDialog dpd = new DatePickerDialog(this,l,07,11,2016);
        dpd.show();

                        //DatePickerDialog.OnDateSeftListener listener, int year, int month, int dayOfMonth)


        notif();

        GetBiersServices.startActionget_all_biers(this, "","");
        IntentFilter intentFilter = new IntentFilter(BIERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BierUpdate(),intentFilter);



        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_biere);
        //rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));



        rv_biere = (RecyclerView) findViewById(R.id.rv_biere);
        rv_biere.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv_biere.setAdapter(new BiersAdapter(getBiersFromFile()));
    }


    public void Second_button(View v){

        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }

    public void Third_button(View v){

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.fr"));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void notif(){
      NotificationCompat.Builder mBuilder =
              new NotificationCompat.Builder(this)
                      .setSmallIcon(R.drawable.notification_icon)
                      .setContentTitle("MY MS_APP IS RUNNING")
                      .setContentText("Welcome, Click to get a View of our Beers!");

      NotificationManager mNotificationManager =
              (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
      mNotificationManager.notify(1, mBuilder.build());

        Intent resultIntent = new Intent(this,Main2Activity.class);
        PendingIntent resultPendingIntent= PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

    }

    public static final String BIERS_UPDATE = "com.octip.cours.inf4042_11.BIERS_UPDATE";

    public class BierUpdate extends BroadcastReceiver{
        @Override

        public void onReceive(Context context, Intent intent){
            Log.d("Tag",getIntent().getAction());

        }
    }

    public class BierReceiver extends BroadcastReceiver{
        @Override

        public void onReceive(Context context, Intent intent){
            ((BiersAdapter)rv_biere.getAdapter()).setNewBiers(getBiersFromFile());

        }
    }






class BiersAdapter extends RecyclerView.Adapter<BierHolder> {

    private JSONArray biers;

    public BiersAdapter(JSONArray biers) {
        this.biers = biers;
    }

    public void setNewBiers(JSONArray biers){
        this.biers=biers;
    }
    public BierHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.rv_bier_element, parent, false);
        return new BierHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BierHolder holder, int position) {
        try {
            holder.name.setText(biers.getJSONObject(position).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return biers.length();
    }
}
    public JSONArray getBiersFromFile(){
        try {
            InputStream is = new FileInputStream(getCacheDir() + "/" + "bieres.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8"));
        } catch(IOException e){
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e){
            e.printStackTrace();
            return new JSONArray();
        }
    }
class BierHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public BierHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.rv_bier_element_name);
    }
}

}





