package org.esiea.sqalli_hiezely.ms_app;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetBiersServices extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS




    // TODO: Rename parameters

    public GetBiersServices() {
        super("GetBiersServices");
    }

    /**
     * Starts this service to perform action get_all_biers with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionget_all_biers(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GetBiersServices.class);
        //intent.putExtra(EXTRA_PARAM1, param1);
        //intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method


    private void copyInputStreamToFile(InputStream in, File file){
        try{
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onHandleIntent(Intent intent) {



        if (intent != null) {


            Log.i("tag","handleActionget_all_biers");

            Log.d("Tag","thread service name" + Thread.currentThread().getName());
            URL url = null;
            try{
                url = new URL("http://binouze.fabrigli.fr/bieres.json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                    copyInputStreamToFile((conn.getInputStream()), new File(getCacheDir(), "bieres.json"));
                    Log.d("Tag","Beer json downloaded !");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.BIERS_UPDATE));
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }


}
