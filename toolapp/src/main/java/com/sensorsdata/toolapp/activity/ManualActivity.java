package com.sensorsdata.toolapp.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.sensorsdata.toolapp.R;

import br.tiagohm.markdownview.MarkdownView;

public class ManualActivity extends AppCompatActivity {

    private MarkdownView mMarkdownView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("用户手册");
        }
        setContentView(R.layout.activity_user);
        mMarkdownView=findViewById(R.id.mark_view);
        //去除回到顶部控件
        mMarkdownView.removeJavaScript(MarkdownView.MY_SCRIPT);
        mMarkdownView.loadMarkdownFromUrl("https://raw.githubusercontent.com/liuweiqiang2016/page.io/gh-pages/user.md");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //actionbar navigation up 按钮
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
