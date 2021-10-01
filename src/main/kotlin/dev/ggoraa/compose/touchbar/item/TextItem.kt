package dev.ggoraa.compose.touchbar.item

import androidx.compose.runtime.Composable
import ca.weblite.objc.RuntimeUtils.str
import dev.ggoraa.compose.touchbar.TouchBarScope
import dev.ggoraa.compose.touchbar.TouchBarViewNode

@Composable fun TouchBarScope.TextItem(text: String) {
  TouchBarViewNode(
    viewFactory = {
      client.sendProxy("NSTextField", "labelWithString:", text)
    },
    update = {
      set(text) {
        viewImpl!!.send("setStringValue:", str(it))
      }
    }
  )
}
