package com.haanhgs.app.implicitdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFullScreenInLandscapeMode();
        ButterKnife.bind(this);
    }

    @SuppressWarnings("deprecation")
    private void setFullScreenInLandscapeMode() {
        if (getDisplay() != null){
            int rotation = getDisplay().getRotation();
            if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
                if (getSupportActionBar() != null) getSupportActionBar().hide();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    final WindowInsetsController controller = getWindow().getInsetsController();
                    if (controller != null){
                        controller.hide(WindowInsets.Type.statusBars());
                    }
                }else {
                    getWindow().setFlags(
                            WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN
                    );
                }
            }
        }
    }

    @OnClick({R.id.bnUrl, R.id.bnLoc, R.id.bnMessage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bnUrl:
                openUrl();
                break;
            case R.id.bnLoc:
                openLocation();
                break;
            case R.id.bnMessage:
                sendMessage();
                break;
        }
    }

    private void openUrl() {
        if (!TextUtils.isEmpty(etUrl.getText())){
            try{
                String query = URLEncoder.encode(etUrl.getText().toString().trim(), "UTF-8");
                Uri uri = Uri.parse("https://" + query);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                openIntent(intent);
            }catch (UnsupportedEncodingException error){
                Log.e(ETAG, error.toString());
            }
        }
    }

    private void openLocation() {
        if (!TextUtils.isEmpty(etLoc.getText())){
            String loc = etLoc.getText().toString().trim();
            Uri uri = Uri.parse("geo:0,0?q=" + loc);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            openIntent(intent);
        }
    }

    private void sendMessage() {
        if (!TextUtils.isEmpty(etMessage.getText())){
            String message = etMessage.getText().toString().trim();
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setChooserTitle(getResources().getString(R.string.shareTitle))
                    .setText(message)
                    .startChooser();
        }
    }

    private void openIntent(Intent intent){
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }else {
            Log.e(ETAG, "No app to open link");
        }
    }

}
