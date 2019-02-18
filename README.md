# java9-stack-stackwalking

# preface
* theory: https://github.com/mtumilowicz/java-stack
* https://github.com/mtumilowicz/java8-stack-stackwalking
* java 9 introduced a new API for stack-walking
* API is represented by single `StackWalker` class
* easy and efficient (stream of frames)
* evaluates the stack frames lazily
* methods to get the reference of the caller's class

# StackWalker
## StackWalker.Option
* enum
* configures StackWalker
* values
    * `RETAIN_CLASS_REFERENCE` - `StackFrame` will contain
    reference to the `Class` object of the method represented
    by this frame
    * `SHOW_HIDDEN_FRAMES` - show all hidden frames (for example
    implementation specific and reflection frames are not included
    by default)
    * `SHOW_REFLECT_FRAMES` - show only reflection frames (
    specific implementation frames could be still hidden)
## StackWalker.StackFrame
* interface
* we don't have direct access to concrete implementation
* useful methods:
    * `String getClassName()`
    * `String getMethodName()`
    * `int getLineNumber()`
    * `public Class<?> getDeclaringClass()`
        * `UnsupportedOperationException` if not configured with
        `RETAIN_CLASS_REFERENCE`

## Methods