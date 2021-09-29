@file:Suppress("FunctionName")

package dev.ggoraa.compose.touchbar

import ca.weblite.objc.Client
import ca.weblite.objc.Proxy

fun TouchBarSystemImage(name: String, accessibilityDescription: String): TouchBarImage  {
  val client = Client.getInstance()
  return TouchBarImage(client.sendProxy("NSImage", "imageWithSystemSymbolName:accessibilityDescription:", name, accessibilityDescription))
}

data class TouchBarImage(val nsImage: Proxy)

