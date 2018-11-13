package darkpoint.rubel;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    TextView r, u, b, e, l, j;
    private static int splashTimeOut;

    static {
        splashTimeOut = 2000;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        r = findViewById(R.id.splash_r);
        u = findViewById(R.id.splash_u);
        b = findViewById(R.id.splash_b);
        e = findViewById(R.id.splash_e);
        l = findViewById(R.id.splash_l);
        j = findViewById(R.id.splash_j);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, splashTimeOut);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_fade);
        anim.setDuration(2000);
        r.setAnimation(anim);
        u.setAnimation(anim);
        b.setAnimation(anim);
        e.setAnimation(anim);
        l.setAnimation(anim);
        j.setAnimation(anim);

    }
}
