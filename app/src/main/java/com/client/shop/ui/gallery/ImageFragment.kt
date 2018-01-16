package com.client.shop.ui.gallery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.client.shop.R
import com.client.shop.ui.custom.zoomable.DoubleTapGestureListener
import com.client.shop.ui.custom.zoomable.ZoomableDraweeView
import com.domain.entity.Image
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.view.SimpleDraweeView


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
    private var thumbnailMode = false
    private var image: Image? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        thumbnailMode = arguments.getBoolean(IS_THUMBNAIL_MODE, false)
        image = arguments.getParcelable(IMAGE)
        val layout = if (thumbnailMode) R.layout.fragment_image else R.layout.fragment_zoomable_image
        return inflater?.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (view is ZoomableDraweeView) {
            view.hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
            view.setIsLongpressEnabled(false)
            view.setTapListener(DoubleTapGestureListener(view))

            imageClickListener?.let { view.setOnClickListener(it) }
            val controller = Fresco.newDraweeControllerBuilder()
                    .setUri(image?.src)
                    .setCallerContext("ImageFragment")
                    .build()
            view.controller = controller
        } else if (view is SimpleDraweeView) {
            view.setImageURI(image?.src)
            imageClickListener?.let { view.setOnClickListener(it) }
        }
    }
}