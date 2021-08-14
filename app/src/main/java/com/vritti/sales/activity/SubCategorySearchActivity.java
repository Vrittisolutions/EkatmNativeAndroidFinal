/*
package com.vritti.orderbilling.customer;

*/
/**
 * Created by 300151 on 5/7/2016.
 *//*

public class SubCategorySearchActivity {
}
*/
package com.vritti.sales.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vritti.ekatm.R;

import com.vritti.sales.adapters.SubcategoryAdapter;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.data.AnyMartData;

import java.util.ArrayList;

/**
 * Created by 300151 on 3/2/2016.
 */
public class SubCategorySearchActivity extends AppCompatActivity {
    private Context parent;
    ListView listView;
    ArrayList<AllCatSubcatItems> list;
    ViewPager viewPager;
    LinearLayoutManager llm;
    SubcategoryAdapter adapter;

    // DividerItemDecoration itemDecoration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  getSupportActionBar().setTitle("Search Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        parent = SubCategorySearchActivity.this;

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

        for (AllCatSubcatItems s : SubCategoryActivity.arrayList) {
            String a = s.getSubCategoryName();
          //  containsIgnoreCase(a,query);
            //containsIgnoreCase(String str, String searchStr)
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

        adapter = new SubcategoryAdapter(SubCategorySearchActivity.this, list);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                AnyMartData.selectedCategoryName =
                        list.get(position).getCategoryName();
                // startActivity(new Intent(parent, ItemListActivity.class));

                Intent intent = new Intent(SubCategorySearchActivity.this, ItemListActivity.class);
                intent.putExtra("SubCategoryId", list.get(position).getSubCategoryId());
                startActivity(intent);
                finish();
            }
        });


    }
    /*public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (str.regionMatches(true, i, searchStr, 0, len)) {
                list.add(s);
                return true;
            }
        }
        return false;
    }*/

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
          //  Toast.makeText(getApplicationContext(), "Search Reult..." + query, Toast.LENGTH_LONG).show();
            mySearch(query);

        }
    }
}

