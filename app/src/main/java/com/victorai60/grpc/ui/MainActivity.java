package com.victorai60.grpc.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.victorai60.grpc.R;
import com.victorai60.grpc.entity.TitleBean;
import com.victorai60.grpc.ui.adapter.MyAdapter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.demo.DemoGrpc;
import io.grpc.demo.HelloReply;
import io.grpc.demo.HelloRequest;

public class MainActivity extends AppCompatActivity {
    private static final String HOST = "172.16.254.76";
    private static final int PORT = 50051;
    private ManagedChannel mManagedChannel;
    private RecyclerView rvNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        new DemoTask().execute();
    }

    private void initView() {
        final RecyclerView rvNavigation = (RecyclerView) findViewById(R.id.rv_navigation);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvNavigation.setLayoutManager(linearLayoutManager);
        rvNavigation.setItemAnimator(null);
        rvNavigation.setHasFixedSize(true);
        List<TitleBean> titleBeen = new ArrayList<>();
        TitleBean titleBean = new TitleBean();
        titleBean.setTitle("新闻");
        titleBean.setSelected(true);
        titleBeen.add(titleBean);
        TitleBean titleBean2 = new TitleBean();
        titleBean2.setTitle("今日头条");
        titleBeen.add(titleBean2);
        TitleBean titleBean3 = new TitleBean();
        titleBean3.setTitle("科技");
        titleBeen.add(titleBean3);
        TitleBean titleBean4 = new TitleBean();
        titleBean4.setTitle("娱乐");
        titleBeen.add(titleBean4);
        TitleBean titleBean5 = new TitleBean();
        titleBean5.setTitle("体育");
        titleBeen.add(titleBean5);
        TitleBean titleBean6 = new TitleBean();
        titleBean6.setTitle("军事");
        titleBeen.add(titleBean6);
        TitleBean titleBean7 = new TitleBean();
        titleBean7.setTitle("本地");
        titleBeen.add(titleBean7);
        TitleBean titleBean8 = new TitleBean();
        titleBean8.setTitle("美图");
        titleBeen.add(titleBean8);
        TitleBean titleBean9 = new TitleBean();
        titleBean9.setTitle("段子");
        titleBeen.add(titleBean9);
        TitleBean titleBean10 = new TitleBean();
        titleBean10.setTitle("小说");
        titleBeen.add(titleBean10);
        TitleBean titleBean11 = new TitleBean();
        titleBean11.setTitle("成都");
        titleBeen.add(titleBean11);
        TitleBean titleBean12 = new TitleBean();
        titleBean12.setTitle("媒体");
        titleBeen.add(titleBean12);
        MyAdapter myAdapter = new MyAdapter(titleBeen);
        myAdapter.setOnItemSelectedListener(new MyAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int itemCount, int currentPosition, int position) {
                int offset = position + (currentPosition < position ? 2 : -2);
                if (offset < 0) {
                    offset = 0;
                }
                if (offset > itemCount) {
                    offset = itemCount;
                }
                rvNavigation.scrollToPosition(offset);
            }
        });
        rvNavigation.setAdapter(myAdapter);
    }

    private class DemoTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                mManagedChannel = ManagedChannelBuilder.forAddress(HOST, PORT)
                        .usePlaintext(true)
                        .build();
                DemoGrpc.DemoBlockingStub stub = DemoGrpc.newBlockingStub(mManagedChannel);
                HelloRequest message = HelloRequest.newBuilder().setName("郑松").build();
                HelloReply reply = stub.sayHello(message);
                return reply.getMessage();
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                pw.flush();
                return String.format("Failed... : %n%s", sw);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                mManagedChannel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
}
