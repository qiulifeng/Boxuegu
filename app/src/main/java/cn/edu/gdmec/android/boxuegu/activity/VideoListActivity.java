package cn.edu.gdmec.android.boxuegu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.boxuegu.Bean.VideoBean;
import cn.edu.gdmec.android.boxuegu.R;
import cn.edu.gdmec.android.boxuegu.utils.DBUtils;
import cn.edu.gdmec.android.boxuegu.adapter.VideoListAdapter;

public class VideoListActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_intro;
    private TextView tv_video;
    private ListView lv_video_list;
    private TextView tv_chapter_intro;
    private ScrollView sv_chapter_intro;
    private List<VideoBean> videoList;
    private int chapterId;
    private String intro;
    private DBUtils db;
    private VideoListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        chapterId = getIntent().getIntExtra("id", 0);
        intro = getIntent().getStringExtra("intro");
        db = DBUtils.getInstance(VideoListActivity.this);
        initData2();
        initView();
    }

    private void initData() {
        JSONArray jsonArray;
        try {
            InputStream is = getResources().getAssets().open("data.json");
            jsonArray = new JSONArray(read(is));
            Log.i("jsonArray1",jsonArray.toString());
            videoList = new ArrayList<VideoBean>();
            for (int i = 0; i < jsonArray.length(); i++) {
                VideoBean bean = new VideoBean();
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                //Log.i("jsonObj",jsonObj.toString());
                if (jsonObj.getInt("chapterId") == chapterId) {
                    bean.chapterId = jsonObj.getInt("chapterId");
                    bean.videoId = Integer.parseInt(jsonObj.getString("videoId"));
                    bean.title = jsonObj.getString("title");
                    bean.secondTitle = jsonObj.getString("secondTitle");
                    bean.videoPath = jsonObj.getString("videoPath");
                    videoList.add(bean);
                }
                bean = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void initData2() {
        JSONArray jsonArray;
        try {
            InputStream is = getResources().getAssets().open("data.json");
            jsonArray = new JSONArray(read(is));
            videoList = new ArrayList<VideoBean>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                Log.i("jsonObj",jsonObj.toString());
                if (jsonObj.getInt("chapterId") == chapterId) {
                    JSONArray jsonArray1=jsonObj.getJSONArray("data");
                    for (int j=0;j<jsonArray1.length();j++){
                        VideoBean bean = new VideoBean();
                        bean.chapterId = jsonObj.getInt("chapterId");
                        JSONObject jsonObject=jsonArray1.getJSONObject(j);
                        Log.i("jsonObject",jsonObject.toString());
                        bean.videoId = Integer.parseInt(jsonObject.getString("videoId"));
                        bean.title = jsonObject.getString("title");
                        bean.secondTitle = jsonObject.getString("secondTitle");
                        bean.videoPath = jsonObject.getString("videoPath");
                        videoList.add(bean);
                        bean = null;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String read(InputStream is) {
        BufferedReader reader = null;
        StringBuilder sb = null;
        String line = null;
        try {
            sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(is));

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (is != null) {
                try {
                    is.close();
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private void initView() {
        tv_intro = (TextView) findViewById(R.id.tv_intro);
        tv_video = (TextView) findViewById(R.id.tv_video);
        lv_video_list = (ListView) findViewById(R.id.lv_video_list);
        tv_chapter_intro = (TextView) findViewById(R.id.tv_chapter_intro);
        sv_chapter_intro = (ScrollView) findViewById(R.id.sv_chapter_intro);
        adapter = new VideoListAdapter(this);
//                adapter.setSelectedPosition(position);
//                VideoBean bean = videoList.get(position);
//                String videoPath = bean.videoPath;
//                adapter.notifyDataSetChanged();
//                if (TextUtils.isEmpty(videoPath)) {
//                    Toast.makeText(VideoListActivity.this, "本地没有此视频，暂无法播放", Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    if (readLoginStatus()) {
//                        String userName = AnalysisUtils.readLoginUserName(VideoListActivity.this);
//                        db.saveVideoPlayList(videoList.get(position), userName);
//                    }
//                    //视频播放
//                    Intent intent = new Intent(VideoListActivity.this, VideoPlayActivity.class);
//                    intent.putExtra("videoPath", videoPath);
//                    intent.putExtra("position", position);
//                    startActivityForResult(intent, 1);

        lv_video_list.setAdapter(adapter);
        tv_intro.setOnClickListener(this);
        tv_video.setOnClickListener(this);
        Log.i("videoList", videoList.size()+"");
        adapter.setData(videoList);
        tv_chapter_intro.setText(intro);
        tv_intro.setBackgroundColor(Color.parseColor("#30B4FF"));
        tv_video.setBackgroundColor(Color.parseColor("#FFFFFF"));
        tv_intro.setTextColor(Color.parseColor("#FFFFFF"));
        tv_video.setTextColor(Color.parseColor("#000000"));
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_intro:
                lv_video_list.setVisibility(View.GONE);
                sv_chapter_intro.setVisibility(View.VISIBLE);
                tv_intro.setBackgroundColor(Color.parseColor("#30B4FF"));
                tv_video.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_intro.setTextColor(Color.parseColor("#FFFFFF"));
                tv_video.setTextColor(Color.parseColor("#000000"));
                break;
            case R.id.tv_video:
                lv_video_list.setVisibility(View.VISIBLE);
                sv_chapter_intro.setVisibility(View.GONE);
                tv_intro.setBackgroundColor(Color.parseColor("#FFFFFF"));
                tv_video.setBackgroundColor(Color.parseColor("#30B4FF"));
                tv_intro.setTextColor(Color.parseColor("#000000"));
                tv_video.setTextColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            int position = data.getIntExtra("position", 0);
            adapter.setSelectedPosition(position);
            lv_video_list.setVisibility(View.VISIBLE);
            sv_chapter_intro.setVisibility(View.GONE);
            tv_intro.setBackgroundColor(Color.parseColor("#FFFFFF"));
            tv_video.setBackgroundColor(Color.parseColor("#30B4FF"));
            tv_intro.setTextColor(Color.parseColor("#000000"));
            tv_video.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }
}
