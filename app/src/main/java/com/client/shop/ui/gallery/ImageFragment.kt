package com.client.shop.ui.gallery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.client.shop.R
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.view.SimpleDraweeView
import com.shopapicore.entity.Image


class ImageFragment : Fragment() {

    companion object {

        private const val IMAGE = "image"
        private const val IS_THUMBNAIL_MODE = "IS_THUMBNAIL_MODE"

        fun newInstance(image: Image, isFullscreenMode: Boolean): ImageFragment {
            val fragment = ImageFragment()
            val args = Bundle()
            args.putParcelable(IMAGE, image)
            args.putBoolean(IS_THUMBNAIL_MODE, isFullscreenMode)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val image: Image = arguments.getParcelable(IMAGE)
        val isThumbnailMode = arguments.getBoolean(IS_THUMBNAIL_MODE, false)

        if (view is SimpleDraweeView && !TextUtils.isEmpty(image.src)) {
            view.setImageURI(image.src)
            view.hierarchy.actualImageScaleType = if (isThumbnailMode)
                ScalingUtils.ScaleType.CENTER_CROP
            else
                ScalingUtils.ScaleType.FIT_CENTER
        }
    }
}