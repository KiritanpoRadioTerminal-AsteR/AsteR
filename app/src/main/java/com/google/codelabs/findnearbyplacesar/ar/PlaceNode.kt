package com.google.codelabs.findnearbyplacesar.ar

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.ImageView
import android.widget.TextView
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.codelabs.findnearbyplacesar.R
import com.google.codelabs.findnearbyplacesar.c_count
import com.google.codelabs.findnearbyplacesar.cornerArray
import com.google.codelabs.findnearbyplacesar.model.Place
import com.google.codelabs.findnearbyplacesar.near.nearby
import kotlin.random.Random


class PlaceNode(
    val context: Context,
    val place: Place?,
    val corner: String
) : Node() {

    private var placeRenderable: ViewRenderable? = null
    private var textViewPlace: TextView? = null
    private var imageViewArrow: ImageView? = null

    override fun onActivate() {
        super.onActivate()

        if (scene == null) {
            return
        }

        if (placeRenderable != null) {
            return
        }

        if (place != null) {
            if (place.id == "y") {
                ViewRenderable.builder()
                    .setView(context, R.layout.arrow)
                    .build()
                    .thenAccept { renderable ->
                        setRenderable(renderable)
                        placeRenderable = renderable

                        place?.let {
                            textViewPlace = renderable.view.findViewById(R.id.navgation1)
                            textViewPlace?.text = it.text
                            imageViewArrow = renderable.view.findViewById(R.id.arrowImage)
                            val arrowImageView: ImageView = renderable.view.findViewById(R.id.arrowImage)
                            if (Regex(cornerArray[c_count]).containsMatchIn("左")) {
                                imageViewArrow?.setImageResource(R.drawable.arrow_left)
                                val objectAnimator = ObjectAnimator.ofFloat(arrowImageView, "translationX", 400f, 0f)
                                objectAnimator.duration = 2000
                                objectAnimator.repeatCount = -1
                                objectAnimator.start()
                            } else {
                                imageViewArrow?.setImageResource(R.drawable.allow_right)
                                val objectAnimator = ObjectAnimator.ofFloat(arrowImageView, "translationX", 0f, 400f)
                                objectAnimator.duration = 2000
                                objectAnimator.repeatCount = -1
                                objectAnimator.start()
                            }
                        }
                    }
            } else {
                ViewRenderable.builder()
                    .setView(context, R.layout.place_view)
                    .build()
                    .thenAccept { renderable ->
                        setRenderable(renderable)
                        placeRenderable = renderable


                        place?.let {
                            textViewPlace = renderable.view.findViewById(R.id.textView2)
                            textViewPlace?.text = it.name
                        }
                    }
            }
        }
    }


    fun showInfoWindow() {
        // Show text
        textViewPlace?.let {
            it.visibility = if (it.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        // Hide text for other nodes
        this.parent?.children?.filter {
            it is PlaceNode && it != this
        }?.forEach {
            (it as PlaceNode).textViewPlace?.visibility = View.GONE
        }
    }

    private fun getTranslation(value: Int, ratio: Float): Float {
        return value * (ratio - 1.0f) * (Random.nextFloat() - 0.5f)
    }
}