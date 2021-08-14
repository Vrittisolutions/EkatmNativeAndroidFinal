package com.vritti.sales.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vritti.ekatm.R;
import com.vritti.sales.adapters.CategoryAdapter;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.data.AnyMartData;

import java.util.ArrayList;

/**
 * Created by 300151 on 3/2/2016.
 */
public class CategorySearchActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<AllCatSubcatItems> list;
    ViewPager viewPager;
    LinearLayoutManager llm;
    CategoryAdapter adapter;

    // DividerItemDecoration itemDecoration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_main);

        /* getSupportActionBar().setTitle("Search Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        listView = (ListView) findViewById(R.id.listview_home_category_list);
        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    protected void mySearch(String query) {

        list = new ArrayList<AllCatSubcatItems>();

        for (AllCatSubcatItems s : MainActivity.arrayList) {
            String a = s.getCategoryName();
           /* if (a.contains(query)) {
                list.add(s);
            }*/

            if (a == null || query == null) {
                // return false;
            }
            int len = query.length();
            int max = a.length() - len;
            for (int i = 0; i <= max; i++) {
                if (a.regionMatches(true, i, query, 0, len)) {
                    list.add(s);
                    //  return true;
                }
            }
        }

        adapter = new CategoryAdapter(CategorySearchActivity.this, list);
        listView.setAdapter(adapter);


        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                AnyMartData.selectedCategoryName =
                        list.get(position).getCategoryName();
                Intent intent = new Intent(CategorySearchActivity.this, SubCategoryActivity.class);
                intent.putExtra("CategoryId", list.get(position).getCategoryName());
                startActivity(intent);
                finish();
            }
        });

    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
           // Toast.makeText(getApplicationContext(), "Search Reult..." + query, Toast.LENGTH_LONG).show();
            mySearch(query);
        }
    }
}

