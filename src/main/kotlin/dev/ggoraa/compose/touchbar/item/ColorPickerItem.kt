package dev.ggoraa.compose.touchbar.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import ca.weblite.objc.RuntimeUtils.sel
import dev.ggoraa.compose.touchbar.*
import dev.ggoraa.compose.touchbar.GenericActionTarget
import dev.ggoraa.compose.touchbar.TouchBarItemNode
import dev.ggoraa.compose.touchbar.toNSColor

sealed interface ColorPickerButtonType {
  object Standard : ColorPickerButtonType
  object Text : ColorPickerButtonType
  object Stroke : ColorPickerButtonType
}

@Composable fun TouchBarScope.ColorPickerItem(
  color: Color,
  onColorChange: (Color) -> Unit,
  buttonType: ColorPickerButtonType = ColorPickerButtonType.Standard,
) {
  val updatedOnColorChange by rememberUpdatedState(onColorChange)

  // Just recreate the entire component if the button type changes since we need to create with a
  // different constructor.
  key(buttonType) {
    TouchBarItemNode(
      factory = { id ->
        val constructor = when (buttonType) {
          ColorPickerButtonType.Standard -> "colorPickerWithIdentifier:"
          ColorPickerButtonType.Stroke -> "strokeColorPickerWithIdentifier:"
          ColorPickerButtonType.Text -> "textColorPickerWithIdentifier:"
        }
        val item = client.sendProxy("NSColorPickerTouchBarItem", constructor, id)

        val actionTarget = object : GenericActionTarget() {
          override fun triggerNullary() {
            val currentColor = item.getProxy("color") // NSColor
            updatedOnColorChange(currentColor.nsColorToComposeColor())
          }
        }
        item["target"] = actionTarget
        // For some reason we can't set this property using .set(), it has its own set selector.
        item.send("setAction:", sel("triggerNullary"))

        TouchBarItemNode(id, item)
      },
      update = {
        set(color) {
          itemImpl["color"] = color.toNSColor()
        }
      }
    )
  }
}
