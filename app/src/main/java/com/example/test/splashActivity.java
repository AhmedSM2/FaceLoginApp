package com.example.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splashActivity extends AppCompatActivity
{
    private ImageView img_aboElAref ,img_map, img_map_icon;
    private Animation map_anim ,map_icon_anim, abo_el3oref_txt_anim;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img_aboElAref=findViewById(R.id.imageViewSplach1);
        img_map=findViewById(R.id.imageViewSplach2);
        img_map_icon=findViewById(R.id.imageViewSplach3);

        /*_____________________________animation__________________________________*/

        map_anim= AnimationUtils.loadAnimation(this ,R.anim.logo_anim);
        img_map.startAnimation(map_anim);

        map_icon_anim= AnimationUtils.loadAnimation(this ,R.anim.translate_map_icon_anim);
        img_map_icon.startAnimation(map_icon_anim);

        abo_el3oref_txt_anim=AnimationUtils.loadAnimation(this ,R.anim.translate_text_anim);
        img_aboElAref.startAnimation(abo_el3oref_txt_anim);

        /*_____________________________________________________________________________*/

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(3000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent Login = new Intent(getApplicationContext(),Login_Activity.class);
                    startActivity(Login);

                }

            }
        };
        thread.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        finish();
    }
}
