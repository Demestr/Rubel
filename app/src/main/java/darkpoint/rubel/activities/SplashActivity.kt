package darkpoint.rubel.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import darkpoint.rubel.R
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_splash.*

const val splashTimeOut = 2000L

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_splash)



        Handler().postDelayed({
            val i = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }, splashTimeOut)

        val anim = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in)
        anim.duration = splashTimeOut
        splash_r.animation = anim
        splash_u.animation = anim
        splash_b.animation = anim
        splash_e.animation = anim
        splash_l.animation = anim
        splash_j.animation = anim
    }
}
