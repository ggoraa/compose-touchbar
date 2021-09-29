@file:Suppress("FunctionName")

package dev.ggoraa.compose.touchbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

@Composable fun TouchBarScope.ButtonItem(
	/** The title displayed on the button when it’s in an off state. */
	title: String,
	alternateTitle: String = "",
	image: TouchBarImage? = null,
	alternateImage: TouchBarImage? = null,
	bezelColor: Color? = null,
	/** A Boolean value that defines whether a button’s action has a destructive effect. */
	hasDestructiveAction: Boolean = false,
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
				if (it != null) {
					viewImpl!!["image"] = it.nsImage
				}
			}
			set(alternateImage) {
				if (it != null) {
					viewImpl!!["alternateImage"] = it.nsImage
				}
			}
			set(bezelColor) {
				it?.let { color ->
					viewImpl!!["bezelColor"] = color.toNSColor()
				}
			}
			set(hasDestructiveAction) {
				viewImpl!!["hasDestructiveAction"] = it
			}
		}
	)
}
