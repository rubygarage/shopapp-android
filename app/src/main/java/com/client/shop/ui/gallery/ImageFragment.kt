package com.client.shop.ui.gallery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.client.shop.R
import com.client.shop.ui.custom.zoomable.DoubleTapGestureListener
import com.client.shop.ui.custom.zoomable.ZoomableDraweeView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.shopapicore.entity.Image


class ImageFragment : Fragment() {

    companion object {

        private const val IMAGE = "image"
        private const val IS_THUMBNAIL_MODE = "IS_THUMBNAIL_MODE"

        fun newInstance(image: Image?, isFullscreenMode: Boolean): ImageFragment {
            val fragment = ImageFragment()
            val args = Bundle()
            args.putParcelable(IMAGE, image)
            args.putBoolean(IS_THUMBNAIL_MODE, isFullscreenMode)
            fragment.arguments = args
            return fragment
        }
    }

    var imageClickListener: View.OnClickListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val thumbnailMode = arguments.getBoolean(IS_THUMBNAIL_MODE, false)
        val image: Image? = arguments.getParcelable(IMAGE)

        if (view is ZoomableDraweeView && !TextUtils.isEmpty(image?.src)) {

            if (thumbnailMode) {
                view.isZoomEnabled = false
            } else {
                view.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
                view.setIsLongpressEnabled(false)
                view.setTapListener(DoubleTapGestureListener(view))
            }

            imageClickListener?.let { view.setOnClickListener(it) }
            val controller = Fresco.newDraweeControllerBuilder()
                    .setUri(image?.src)
                    .setCallerContext("ImageFragment")
                    .build()
            view.controller = controller
        }
    }
}