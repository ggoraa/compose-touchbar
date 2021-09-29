@file:Suppress("FunctionName")

package dev.ggoraa.compose.touchbar.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import dev.ggoraa.compose.touchbar.GenericActionTarget
import dev.ggoraa.compose.touchbar.TouchBarScope
import dev.ggoraa.compose.touchbar.TouchBarViewNode
import dev.ggoraa.compose.touchbar.image.TouchBarImage
import dev.ggoraa.compose.touchbar.toNSColor

@Composable
fun TouchBarScope.ButtonItem(
	/** The title displayed on the button when it’s in an off state. */
	title: String,
	alternateTitle: String = "",
	image: TouchBarImage? = null,
	alternateImage: TouchBarImage? = null,
	bezelColor: Color? = null,
	onClick: () -> Unit,
) {
	val updatedOnClick by rememberUpdatedState(onClick)

	TouchBarViewNode(
		viewFactory = {
			val actionTarget = object : GenericActionTarget() {
				override fun triggerNullary() {
					updatedOnClick()
				}
			}

			client.sendProxy(
				"NSButton", "buttonWithTitle:target:action:",
				/* title */ title,
				/* target */ actionTarget,
				/* action(selector) */ "triggerNullary"
			)
		},
		update = {
			set(title) {
				viewImpl!!["title"] = it
			}
			set(alternateTitle) {
				viewImpl!!["alternateTitle"] = it
			}
			set(image) {
				it?.let {
					viewImpl!!["image"] = it.nsImage
				}
			}
			set(alternateImage) {
				it?.let {
					viewImpl!!["alternateImage"] = it.nsImage
				}
			}
			set(bezelColor) {
				it?.let { color ->
					viewImpl!!["bezelColor"] = color.toNSColor()
				}
			}
		}
	)
}
