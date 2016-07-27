package com.wallstcn.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wallstcn.R;

/**
 * Created by _GZL on 2016/3/5.
 */
public class GlideUtil {

    public static void bindImage(Context ctxt,String uri,ImageView iv){
        Glide.with(ctxt).load(uri).error(R.drawable.error).into(iv);
    }
}
