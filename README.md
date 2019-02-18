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
* note that there is also enum `StackWalker.ExtendedOption` 
with only one, self-explaining value `LOCALS_AND_OPERANDS`
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
## methods
* factory
    * `static StackWalker getInstance()`
    * `static StackWalker getInstance(Option option)`
    * `static StackWalker getInstance(Set<Option> options)`
    * `static StackWalker getInstance(Set<Option> options, int estimateDepth)`
        * `estimateDepth` specifies the estimate number of stack frames
          this `StackWalker` will traverse that the `StackWalker` could
          use as a hint for the buffer size