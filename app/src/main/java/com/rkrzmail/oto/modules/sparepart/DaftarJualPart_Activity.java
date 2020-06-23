package com.rkrzmail.oto.modules.sparepart;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.naa.data.Nson;
import com.naa.utils.InternetX;
import com.naa.utils.Messagebox;
import com.rkrzmail.oto.AppActivity;
import com.rkrzmail.oto.AppApplication;
import com.rkrzmail.oto.R;
import com.rkrzmail.srv.NikitaRecyclerAdapter;
import com.rkrzmail.srv.NikitaViewHolder;
import com.rkrzmail.srv.RupiahFormat;

import java.util.Map;

public class DaftarJualPart_Activity extends AppActivity {

    private static final int REQUEST_DETAIL = 13;
    private EditText etTotal;
    private RecyclerView rvTotalJualPart;
    private static final int REQUEST_CARI_PART = 14;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_jual_part_);
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Daftar Jual Part");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        initToolbar();
        Intent i = getIntent();
        nListArray.add(Nson.readJson(getIntentStringExtra(i, "part")));

        rvTotalJualPart = findViewById(R.id.recyclerView_jualPart);
        rvTotalJualPart.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTotalJualPart.setAdapter(new NikitaRecyclerAdapter(nListArray, R.layout.item_total_jual_part) {
                    @Override
                    public void onBindViewHolder(@NonNull NikitaViewHolder viewHolder, int position) {
                        super.onBindViewHolder(viewHolder, position);

                        viewHolder.find(R.id.tv_noPart_jualPart, TextView.class).setText(nListArray.get(position).get("NO_PART").asString());
                        viewHolder.find(R.id.tv_namaPart_jualPart, TextView.class).setText(nListArray.get(position).get("NAMA").asString());
                        viewHolder.find(R.id.tv_harga_jualPart, TextView.class).setText(nListArray.get(position).get("HARGA").asString());
                        viewHolder.find(R.id.tv_disc_jualPart, TextView.class).setText(nListArray.get(position).get("DISC").asString());
                        viewHolder.find(R.id.tv_jumlah_jualPart, TextView.class).setText(nListArray.get(position).get("JUMLAH").asString());
                        viewHolder.find(R.id.tv_total_jualPart, TextView.class).setText(nListArray.get(position).get("TOTAL").asString());
                    }

                    @Override
                    public int getItemCount() {
                        if (nListArray.size() == 0) {
                            find(R.id.btn_simpan_jualPart, Button.class).setVisibility(View.GONE);
                            find(R.id.et_totalHarga_jualPart, EditText.class).setVisibility(View.GONE);
                        } else {
                            find(R.id.btn_simpan_jualPart, Button.class).setVisibility(View.VISIBLE);
                            find(R.id.et_totalHarga_jualPart, EditText.class).setVisibility(View.VISIBLE);
                        }
                        return super.getItemCount();
                    }
                }.setOnitemClickListener(new NikitaRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Nson parent, View view, int position) {
                        deleteData();
                    }
                })
        );
        find(R.id.btn_simpan_jualPart, Button.class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        find(R.id.et_totalHarga_jualPart, EditText.class).addTextChangedListener(new RupiahFormat(find(R.id.et_totalHarga_jualPart, EditText.class)));
        for (int j = 0; j < nListArray.size(); j++) {
            if(nListArray.containsValue("TOTAL")){
                int total = nListArray.get(j).get("TOTAL").asInteger();
                total += total;
                find(R.id.et_totalHarga_jualPart, EditText.class).setText(String.valueOf(total));
            }
        }
    }

    private void deleteData() {
        newProses(new Messagebox.DoubleRunnable() {
            Nson result;

            @Override
            public void run() {
                Nson n = Nson.readJson(getIntentStringExtra("part"));
                Map<String, String> args = AppApplication.getInstance().getArgsData();
                result = Nson.readJson(InternetX.postHttpConnection(AppApplication.getBaseUrlV3("aturjualpart"), args));
            }

            @Override
            public void runUI() {
                if (result.get("status").asString().equalsIgnoreCase("OK")) {
                    showInfo("Sukses Memperharui Data");
                    startActivity(new Intent(getActivity(), DaftarJualPart_Activity.class));
                    finish();
                } else {
                    showInfo("Gagagl Memperbarui Data");
                }
            }
        });
    }


    private void saveData() {
        newProses(new Messagebox.DoubleRunnable() {
            Nson result;

            @Override
            public void run() {
                Nson n = Nson.readJson(getIntentStringExtra("part"));
                Map<String, String> args = AppApplication.getInstance().getArgsData();
                //action(add), CID, jenis, nohp, nama, nusaha, nopart, napart, hpart, disc, jumlah, total

                args.put("action", "add");
                args.put("jenis", n.get("jenis").asString());
                args.put("nohp", n.get("nohp").asString());
                args.put("nama", n.get("nama").asString());
                args.put("nusaha", n.get("nusaha").asString());
                args.put("nopart", n.get("NO_PART").asString());
                args.put("napart", n.get("NAMA").asString());
                //args.put("hpart",);
//                args.put("disc",);
//                args.put("jumlah",);
//                args.put("total",);
                result = Nson.readJson(InternetX.postHttpConnection(AppApplication.getBaseUrlV3("aturjualpart"), args));
            }

            @Override
            public void runUI() {
                if (result.get("status").asString().equalsIgnoreCase("OK")) {
                    showInfo("Sukses Memperharui Data");
                    startActivity(new Intent(getActivity(), DaftarJualPart_Activity.class));
                    finish();
                } else {
                    showInfo("Gagagl Memperbarui Data");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_part, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent i = new Intent(this, CariPart_Activity.class);
            i.putExtra("flag", "ALL");
            startActivityForResult(i, REQUEST_CARI_PART);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CARI_PART) {
            nListArray.add(Nson.readJson(getIntentStringExtra(data, "part")));
            rvTotalJualPart.getAdapter().notifyDataSetChanged();
            //find(R.id.et_totalHarga_jualPart, TextView.class).setText("");
        }
    }

}
