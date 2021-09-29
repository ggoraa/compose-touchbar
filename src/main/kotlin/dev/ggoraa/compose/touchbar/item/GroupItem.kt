package dev.ggoraa.compose.touchbar.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ca.weblite.objc.Proxy
import dev.ggoraa.compose.touchbar.TouchBarComposition
import dev.ggoraa.compose.touchbar.TouchBarItemNode
import dev.ggoraa.compose.touchbar.TouchBarScope
import dev.ggoraa.compose.touchbar.createNSTouchBar

@Composable fun TouchBarScope.GroupItem(
  content: @Composable TouchBarScope.() -> Unit
) {
  var groupItem: Proxy? by remember { mutableStateOf(null) }

  TouchBarItemNode(
    factory = { id ->
      val item = client.sendProxy("NSGroupTouchBarItem", "alloc")
        .sendProxy("initWithIdentifier:", id)
      groupItem = item
      TouchBarItemNode(id, item)
    },
    update = {}
  )

  TouchBarComposition(
    onTouchBarInvalidated = {
      val group = groupItem ?: return@TouchBarComposition
      group["groupTouchBar"] = client.createNSTouchBar(it)
    },
    content = content
  )
}
