package com.rkrzmail.oto.modules.sparepart;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.naa.data.Nson;
import com.naa.utils.InternetX;
import com.naa.utils.Messagebox;
import com.rkrzmail.oto.AppActivity;
import com.rkrzmail.oto.AppApplication;
import com.rkrzmail.oto.R;
import com.rkrzmail.oto.modules.discount.AturFrekwensiDiscount_Acitivity;
import com.rkrzmail.srv.NikitaRecyclerAdapter;
import com.rkrzmail.srv.NikitaViewHolder;
import com.rkrzmail.utils.Tools;

import java.util.Map;

public class PenjualanPart_Activity extends AppActivity {

    private RecyclerView rvJualPart;
    private SearchView mSearchview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_part_);
        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_jualPart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Penjualan Part");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        FloatingActionButton fab = findViewById(R.id.fab_tambah_jualPart);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AturFrekwensiDiscount_Acitivity.class));
            }
        });

        rvJualPart = findViewById(R.id.recyclerView_freDisc);
        rvJualPart.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvJualPart.setAdapter(new NikitaRecyclerAdapter(nListArray, R.layout.item_frekwensi_disc) {
            @Override
            public void onBindViewHolder(@NonNull NikitaViewHolder viewHolder, int position) {
                super.onBindViewHolder(viewHolder, position);
                String tglDisc = Tools.setFormatDayAndMonth(nListArray.get(position).get("").asString());

                viewHolder.find(R.id.tv_tgl_jualPart, TextView.class).setText(nListArray.get(position).get("").asString());
                viewHolder.find(R.id.tv_userJual_jualPart, TextView.class).setText(nListArray.get(position).get("").asString());
                viewHolder.find(R.id.tv_namaUsaha_jualPart, TextView.class).setText(nListArray.get(position).get("").asString());
                viewHolder.find(R.id.tv_noPhone_jualPart, TextView.class).setText(nListArray.get(position).get("").asString());
                viewHolder.find(R.id.tv_harga_jualPart, TextView.class).setText("Rp. " + nListArray.get(position).get("").asString());
                viewHolder.find(R.id.tv_disc_jualPart, TextView.class).setText("Rp. " + nListArray.get(position).get("").asString());
                viewHolder.find(R.id.tv_total_jualPart, TextView.class).setText("Rp. " + nListArray.get(position).get("").asString());
                viewHolder.find(R.id.tv_status_jualPart, TextView.class).setText(nListArray.get(position).get("").asString());
                viewHolder.find(R.id.tv_userUpdate_jualPart, TextView.class).setText(nListArray.get(position).get("").asString());


            }
        });
        catchData();
    }

    private void catchData() {
        newProses(new Messagebox.DoubleRunnable() {
            Nson result;

            @Override
            public void run() {
                Map<String, String> args = AppApplication.getInstance().getArgsData();
                result = Nson.readJson(InternetX.postHttpConnection(AppApplication.getBaseUrlV3("frekwensidiscount"), args));
            }

            @Override
            public void runUI() {
                if (result.get("status").asString().equalsIgnoreCase("OK")) {

                } else {
                    showInfo("Gagal");
                }
            }
        });
    }
}