@file:Suppress("FunctionName")

package dev.ggoraa.compose.touchbar.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ca.weblite.objc.NSObject
import ca.weblite.objc.Proxy
import ca.weblite.objc.RuntimeUtils.sel
import ca.weblite.objc.annotations.Msg
import dev.ggoraa.compose.touchbar.*
import dev.ggoraa.compose.touchbar.TouchBarComposition
import dev.ggoraa.compose.touchbar.TouchBarItemNode
import dev.ggoraa.compose.touchbar.createNSTouchBar
import dev.ggoraa.compose.touchbar.image.TouchBarImage

@Composable fun TouchBarScope.PopoverItem(
	collapsedLabel: String? = null,
	collapsedImage: TouchBarImage? = null,
	popoverController: PopoverController = remember { PopoverController() },
	showCloseButton: Boolean = true,
	showOnPressAndHold: Boolean = true,
	content: @Composable TouchBarScope.() -> Unit,
) {
	PopoverItem(
		collapsedLabel = collapsedLabel,
		collapsedImage = collapsedImage,
		popoverController = popoverController,
		showCloseButton = showCloseButton,
		popoverContent = content,
		pressAndHoldContent = if (showOnPressAndHold) content else null
	)
}

@Composable fun TouchBarScope.PopoverItem(
	collapsedLabel: String? = null,
	collapsedImage: TouchBarImage? = null,
	popoverController: PopoverController = remember { PopoverController() },
	showCloseButton: Boolean = true,
	popoverContent: @Composable (TouchBarScope.() -> Unit)?,
	pressAndHoldContent: @Composable (TouchBarScope.() -> Unit)?,
) {
	if (collapsedImage == null && collapsedLabel == null) {
		throw IllegalArgumentException("Not specified what to display in collapsed view; neither image or a label")
	}
	var popoverItem: Proxy by remember { mutableStateOf(Proxy()) }

	if (popoverContent != null) {
		TouchBarComposition(
			onTouchBarInvalidated = { spec ->
				println("popoverContent: touch bar invalidated")
				popoverItem["popoverTouchBar"] = client.createNSTouchBar(spec)
			},
			onDispose = {
				println("popoverContent: dispose")
				popoverItem["popoverTouchBar"] = client.createEmptyNSTouchBar()
			},
			content = popoverContent
		)
	}

	if (pressAndHoldContent != null) {
		TouchBarComposition(
			onTouchBarInvalidated = { spec ->
				println("pressAndHoldContent: touch bar invalidated")
				popoverItem["pressAndHoldTouchBar"] = client.createNSTouchBar(spec)
			},
			onDispose = {
				println("pressAndHoldContent: dispose")
				popoverItem["pressAndHoldTouchBar"] = null
			},
			content = pressAndHoldContent
		)
	}

	TouchBarItemNode(
		factory = { id ->
			popoverItem = client.sendProxy("NSPopoverTouchBarItem", "alloc")
				.sendProxy("initWithIdentifier:", id)
			println("created NSPopoverTouchBarItem")
			TouchBarItemNode(id, popoverItem)
		},
		update = {
			set(collapsedLabel) {
				it?.let {
					println("set collapsedLabel")
					itemImpl["collapsedRepresentationLabel"] = it
				}
			}
			set(collapsedImage) {
				collapsedImage?.let {
					println("set collapsedImage")
					itemImpl["collapsedRepresentationImage"] = it.nsImage
				}
			}
			set(showCloseButton) {
				println("set showCloseButton")
				itemImpl.send("setShowsCloseButton:", it)
			}
		}
	)

	DisposableEffect(popoverController) {
		// We don't need to key the effect on popoverItem since it will only be set once, and by the
		// time the effect runs it will already have been set.
		println("dispose")
		popoverController.popoverItems += popoverItem
		onDispose {
			popoverController.popoverItems -= popoverItem
		}
	}
}

class PopoverController {

	internal var popoverItems = listOf<Proxy>()
//
//	/** Helper object to execute show/dismiss methods on the AppKit main thread. */
//	private val executor = object : NSObject("NSObject") {
//
//		@Msg(selector = "showAll", signature = "v@:")
//		fun showAll() {
//			println("showAll")
//			popoverItems.forEach {
//				it.send("showPopover:", /* sender */ this)
//			}
//		}
//
//		@Msg(selector = "dismissAll", signature = "v@:")
//		fun dismissAll() {
//			println("dismissAll")
//			popoverItems.forEach {
//				it.send("dismissPopover:", /* sender */ this)
//			}
//		}
//	}
//
//	fun show() {
//		println("show")
//		executor.send(
//			"performSelectorOnMainThread:withObject:waitUntilDone:",
//			sel("showAll"),
//			executor,
//			/* waitUntilDone */ false
//		)
//	}
//
//	fun dismiss() {
//		println("dismiss")
//		executor.send(
//			"performSelectorOnMainThread:withObject:waitUntilDone:",
//			sel("dismissAll"),
//			executor,
//			/* waitUntilDone */ false
//		)
//	}
}
