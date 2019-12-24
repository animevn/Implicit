package com.haanhgs.app.implicitdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String ETAG = "E.MainActivity";

    @BindView(R.id.etUrl)
    EditText etUrl;
    @BindView(R.id.bnUrl)
    Button bnUrl;
    @BindView(R.id.etLoc)
    EditText etLoc;
    @BindView(R.id.bnLoc)
    Button bnLoc;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.bnMessage)
    Button bnMessage;

    private void setFullScreenInLandscapeMode(){
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
            if (getSupportActionBar() != null) getSupportActionBar().hide();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFullScreenInLandscapeMode();
        ButterKnife.bind(this);
    }

    private void openIntent(Intent intent){
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else {
            Log.e(ETAG, "no app to open the link");
        }
    }

    private void onButtonWebsiteClicked(){
        if (!TextUtils.isEmpty(etUrl.getText())){
            try{
                String query = URLEncoder.encode(etUrl.getText().toString(), "UTF-8");
                Uri uri = Uri.parse("https://www.google.com/#q=" + query);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                openIntent(intent);
            }catch (UnsupportedEncodingException e){
                Log.e(ETAG, e.toString());
            }
        }
    }

    private void onButtonLocationClicked(){
        if (!TextUtils.isEmpty(etLoc.getText())){
            String loc = etLoc.getText().toString();
            Uri uri = Uri.parse("geo:0,0?q=" + loc);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            openIntent(intent);
        }
    }

    private void onButtonMessageClicked(){
        if (!TextUtils.isEmpty(etMessage.getText())){
            String message = etMessage.getText().toString();
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setChooserTitle(getResources().getString(R.string.shareTitle))
                    .setText(message)
                    .startChooser();
        }
    }

    @OnClick({R.id.bnUrl, R.id.bnLoc, R.id.bnMessage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bnUrl:
                onButtonWebsiteClicked();
                break;
            case R.id.bnLoc:
                onButtonLocationClicked();
                break;
            case R.id.bnMessage:
                onButtonMessageClicked();
                break;
        }
    }

}
