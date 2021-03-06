package com.rkrzmail.oto.gmod;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.naa.data.Nson;
import com.naa.data.Utility;
import com.naa.utils.InternetX;
import com.naa.utils.MessageMsg;
import com.naa.utils.Messagebox;
import com.rkrzmail.oto.AppActivity;
import com.rkrzmail.oto.AppApplication;
import com.rkrzmail.oto.R;
import com.rkrzmail.oto.modules.part.AdapterListBasic;
import com.rkrzmail.oto.modules.part.People;
import com.rkrzmail.srv.NikitaRecyclerAdapter;
import com.rkrzmail.srv.NikitaViewHolder;
import com.rkrzmail.utils.DataGenerator;

import java.util.List;
import java.util.Map;

public class Penampung_ItemActivity extends AppActivity {
    private View parent_view;

    private RecyclerView recyclerView;
    private AdapterListBasic mAdapter;

    final int REQUEST_DETAIL = 63;
    final int REQUEST_PERSETUAN  = 124;
    final int REQUEST_STOCK  = 125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penampung__item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //Intent intent =  new Intent(getActivity(), PendaftaranLayananActivity.class);
                Intent intent =  new Intent(getActivity(), Lokasi_PersediaanActivity.class);
                startActivityForResult(intent, REQUEST_PERSETUAN);
            }
        });


        getSupportActionBar().setTitle("PART");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        parent_view = findViewById(android.R.id.content);

        initToolbar();
        initComponent();

    }
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ITEM PERSEDIAAN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private void reload(final String nama){
        MessageMsg.showProsesBar(getActivity(), new Messagebox.DoubleRunnable() {
            Nson result ;
            @Override
            public void run() {
                Map<String, String> args = AppApplication.getInstance().getArgsData();


                args.put("nama", nama);
                result = Nson.readJson(InternetX.postHttpConnection(AppApplication.getBaseUrl("itemlokasi.php"),args)) ;

            }

            @Override
            public void runUI() {
                if (result.isNsonArray()) {
                    nListArray.asArray().clear();
                    nListArray.asArray().addAll(result.asArray());
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }
    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        List<People> items = DataGenerator.getPeopleData(this);
        items.addAll(DataGenerator.getPeopleData(this));
        items.addAll(DataGenerator.getPeopleData(this));

       /* //set data and list adapter
        mAdapter = new AdapterListBasic(this, items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListBasic.OnItemClickListener() {
            @Override
            public void onItemClick(View view, People obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.name + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });*/

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new NikitaRecyclerAdapter(nListArray,R.layout.item_lokasi_persediaan){
            @Override
            public void onBindViewHolder(@NonNull NikitaViewHolder viewHolder, int position) {
                viewHolder.find(R.id.txtLokasi, TextView.class).setText(nListArray.get(position).get("ID").asString());

                viewHolder.find(R.id.txtPenempatan, TextView.class).setText(nListArray.get(position).get("PENEMPATAN").asString());

                viewHolder.find(R.id.txtPalet, TextView.class).setText(nListArray.get(position).get("PALET").asString());
                viewHolder.find(R.id.txtRak, TextView.class).setText(nListArray.get(position).get("RAK").asString());
                viewHolder.find(R.id.txtNoFolder, TextView.class).setText(nListArray.get(position).get("NO_FOLDER").asString());
                viewHolder.find(R.id.tblEdit).setTag(String.valueOf(position));
                viewHolder.find(R.id.tblEdit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = Utility.getInt(  String.valueOf(view.getTag())  );
                        Intent intent =  new Intent(getActivity(), Stock_OpnameActivity.class);
                        intent.putExtra("DATA", nListArray.get(position).toJson());
                        intent.putExtra("ID", nListArray.get(position).get("ID").asString());
                        intent.putExtra("NO_FOLDER", nListArray.get(position).get("NO_FOLDER").asString());
                        startActivityForResult(intent, REQUEST_STOCK);
                    }
                });
            }
        }.setOnitemClickListener(new NikitaRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Nson parent, View view, int position) {
                //Toast.makeText(getActivity(),"HHHHH "+position, Toast.LENGTH_SHORT).show();
                /*Intent intent =  new Intent(getActivity(), ControlLayanan.class);
                intent.putExtra("ID", nListArray.get(position).get("MEKANIK").asInteger());
                intent.putExtra("DATA", nListArray.get(position).toJson());
                startActivityForResult(intent, REQUEST_CONTROL);*/

                //Snackbar.make(parent_view, "Item  "+position+"  clicked", Snackbar.LENGTH_SHORT).show();

                Intent intent =  new Intent(getActivity(), Penampung_Item_DetailActivity.class);
                intent.putExtra("DATA", nListArray.get(position).toJson());
                intent.putExtra("ID", nListArray.get(position).get("ID").asString());
                startActivityForResult(intent, REQUEST_DETAIL);

            }
        }));

        reload("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DETAIL && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra("DATA", getIntentStringExtra(data, "DATA"));
            intent.putExtra("ID", getIntentStringExtra(data,"ID"));
            setResult(RESULT_OK, intent);
            finish();
        } else if (requestCode == REQUEST_PERSETUAN && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra("DATA", getIntentStringExtra(data, "DATA"));
            setResult(RESULT_OK, intent);
            finish();
        } else if (requestCode == REQUEST_STOCK && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra("DATA", getIntentStringExtra(data, "DATA"));
            intent.putExtra("ID", getIntentStringExtra(data,"ID"));
            intent.putExtra("NO_FOLDER", getIntentStringExtra(data,"NO_FOLDER"));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    SearchView mSearchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_part, menu);


        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = new SearchView(getSupportActionBar().getThemedContext());
        mSearchView.setQueryHint("Search"); /// YOUR HINT MESSAGE
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        final MenuItem searchMenu = menu.findItem(R.id.action_search);
        searchMenu.setActionView(mSearchView);
        searchMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);


        //SearchView searchView = (SearchView)  menu.findItem(R.id.action_search).setActionView(mSearchView);
        // Assumes current activity is the searchable activity
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                //filter(newText);
                return true;
            }
            public boolean onQueryTextSubmit(String query) {
                //searchMenu.collapseActionView();
                //filter(null);
                reload(query);
                return true;
            }
        };
        mSearchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}


