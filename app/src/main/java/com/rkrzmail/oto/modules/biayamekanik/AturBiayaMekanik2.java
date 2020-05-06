package com.rkrzmail.oto.modules.biayamekanik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.naa.data.Nson;
import com.naa.utils.InternetX;
import com.naa.utils.Messagebox;
import com.rkrzmail.oto.AppActivity;
import com.rkrzmail.oto.AppApplication;
import com.rkrzmail.oto.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class AturBiayaMekanik2 extends AppActivity {

    public static final String TAG = "AturBiayaMekanik2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biaya_mekanik2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ATUR BIAYA MEKANIK2");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newProses(new Messagebox.DoubleRunnable() {
            Nson result;
            @Override
            public void run() {
                Map<String, String> args2 = AppApplication.getInstance().getArgsData();
                //args2.put("UMK", "UMK");
                result = Nson.readJson(InternetX.postHttpConnection(AppApplication.getBaseUrlV3("viewbiayamekanik"),args2)) ;
                Log.d("UMK", result.get(0).get("UMK").asString());
            }

            @Override
            public void runUI() {

                if (result.get("status").asString().equalsIgnoreCase("OK")){
                    find(R.id.txtUpakKota, EditText.class).setText(result.get("data").get(0).get("UMK").asString());
                    find(R.id.txtUpahJam, EditText.class).setText(result.get("data").get(0).get("UPAH_MINIM").asString());
                    Log.d("UMK", result.get("data").get(0).get("UMK").asString());
                }
            }
        });

            find(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (find(R.id.txtMek1, EditText.class).getText().toString().equalsIgnoreCase("")){
                        showError("Mekanik 1 harus di isi");return;
                    }else if (find(R.id.txtMek2, EditText.class).getText().toString().equalsIgnoreCase("")){
                        showError("Mekanik 2 harus di isi");return;
                    }else if (find(R.id.txtMek3, EditText.class).getText().toString().equalsIgnoreCase("")) {
                        showError("Mekanik 3 harus di isi");
                    }else if (find(R.id.txtMinWB, EditText.class).getText().toString().equalsIgnoreCase("")) {
                        showError("Waktu Bayar 1 harus di isi");
                        return;
                    }
                    insertdata();
                }
            });
    }


    private void insertdata() {
        newProses(new Messagebox.DoubleRunnable() {
            Nson result;
            Nson result2;

            public void run() {
                Map<String, String> args = AppApplication.getInstance().getArgsData();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
                String dateTime = simpleDateFormat.format(calendar.getTime());


                String waktu = find(R.id.txtMinWB, EditText.class).getText().toString();
                String mekanik1 = find(R.id.txtMek1, EditText.class).getText().toString();
                String mekanik2 = find(R.id.txtMek2, EditText.class).getText().toString();
                String mekanik3 = find(R.id.txtMek3, EditText.class).getText().toString();

                args.put("mekanik1", mekanik1);
                args.put("mekanik2", mekanik2);
                args.put("mekanik3", mekanik3);
                args.put("waktu", waktu);
                args.put("tanggal", dateTime);

                //mekanik1, waktu,mekanik2, mekanik3, tanggal, user
                result = Nson.readJson(InternetX.postHttpConnection(AppApplication.getBaseUrlV3("aturbiayamekanik"), args));

            }

            public void runUI() {
                if (result.get("status").asString().equalsIgnoreCase("OK")) {
                    Log.d(TAG, "success add data" + result.get("status").asString());
                    startActivity(new Intent(AturBiayaMekanik2.this, BiayaMekanik2Activity.class));
                    finish();
                } else {
                    showError(result.get("message").asString());
                    Log.d(TAG, "error");
                }
            }
        });
    }
}