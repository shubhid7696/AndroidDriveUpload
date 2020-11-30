package com.eesl.myapplication

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("loadNormalImage")
fun loadingNormalImage(view: ImageView, url: String) {
    /*view.load(url.toUri()) {
        crossfade(750)
        //placeholder(errorPlaceHolder)
        //transformations(CircleCropTransformation())
        //error(errorPlaceHolder)
        scale(Scale.FILL)
    }*/
    Glide.with(view.context).load(url.toUri()).into(view);
}