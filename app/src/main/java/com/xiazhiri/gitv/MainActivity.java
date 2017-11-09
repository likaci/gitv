package com.xiazhiri.gitv;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rc = findViewById(R.id.recyclerView);

        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(new MyAdapter());
        rc.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e(TAG, "onScrollStateChanged: " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<MyVH> {
        @Override
        public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyVH(new Button(MainActivity.this));
        }

        @Override
        public void onBindViewHolder(MyVH holder, int position) {
            holder.tv.setText("" + position);
        }

        @Override
        public int getItemCount() {
            return 1000;
        }
    }

    private class MyVH extends RecyclerView.ViewHolder{
        public Button tv;
        public MyVH(View itemView) {
            super(itemView);
            tv = (Button) itemView;
        }
    }
}
