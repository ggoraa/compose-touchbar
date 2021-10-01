# compose-touchbar

Proof-of-concept of a [Compose for Desktop](https://www.jetbrains.com/lp/compose/) API for Macbook Touchbars.

Base made by @zach-klippenstein, (soon) turned to a real usable thing by @ggoraa

This project currently waits when [kAppKit](https://github.com/ggoraa/kappkit)
project will be implemented in a way that it can be used for Touch Bar manipulation
(with classes which are used in Touch Bar)

## Usage

```kotlin
fun main() = singleWindowApplication {
  TouchBar {
    var sliderValue by remember { mutableStateOf(0f) }
    
    TextField("Hello, world!")
    
    Button("Click me!", onClick = { println("I was clicked!") })

    Group {
      Button("Button1", onClick = {})
      Button("Button2", onClick = {})
    }
    
    Popover(
      collapsedLabel = "Expandable",
      popoverContent = {
        Slider(
          min = 0f,
          max = 10f,
          onValueChanged = { sliderValue = it }
        )
      }
    )
  }
}
```

## TODO

This is just a proof-of-concept, and there are some major issues that would need to be addressed before this could be a real thing:

- [ ] Still getting crashes in native code now and then, e.g., on button clicks.
  
- [ ] ObjC interop library Java-Objective-C-Bridge requires manual
  build. Either package a fat JAR, or wait for the library to be
  published to a public repo (https://github.com/shannah/Java-Objective-C-Bridge/issues/17).
  
- [ ] Build more item composables. `NSScrubber` and maybe a
  picker seem like wonderful candidates. See
  https://developer.apple.com/documentation/appkit/nstouchbaritem?language=objc.
  
- [x] Check for memory leaks. I'm not sure how
  Java-Objective-C-Bridge handles memory management. I
  would be surprised if we aren't leaking native `NSObject`s
  all over the place.
  
- [ ] Check thread usage. I've seen lots of warnings
  about how interactions with UI must happen on the
  AppKit main thread, but there have only been two
  places where performing such operations on the AWT
  thread instead actually caused crashes. But there
  are lots of warnings being spat out about operating
  on the constraint engine on other threads, I think
  particularly with `NSSlider`, that should be addressed,
  and I wonder if this isn't also why things like button
  clicks sometimes randomly crash.
  
- [ ] Figure out an API to support customization.
  macOS automatically provides the ability for users to
  add/remove/reorder items in a touch bar, but the developer
  must specify which items are customizable/required, and
  provide additional labels.
  
- [ ] Improve generation of item identifiers.
  I should pass an argument to each of item composables, so customization code will be possible.
