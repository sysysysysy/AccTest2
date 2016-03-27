package com.marswang.whj.acctest2;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class DateList extends ListActivity {


    public static final String DATA_LIST = "com.marswang.whj.acctest2.dataList";
    private String[] dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_date_list);
       float [] data = this.getIntent().getFloatArrayExtra(DATA_LIST);
        dataList = new String[data.length];
        for (int i=0;i<data.length;i++){
//            System.out.println(data[i] + "");
                dataList[i] = "第" + (i+1) + "组" + data[i] ;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        setListAdapter(adapter);
    }
}
