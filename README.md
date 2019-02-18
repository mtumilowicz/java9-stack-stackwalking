# java9-stack-stackwalking

# preface
* theory: https://github.com/mtumilowicz/java-stack
* comparison to java 8: https://github.com/mtumilowicz/java8-stack-stackwalking
* java 9 introduced a new API for stack-walking
* API is represented by single `StackWalker` class
* easy and efficient - evaluates the stack frames lazily (stream based)
* method to get the reference of the caller's class
* is thread-safe

# StackWalker
## StackWalker.Option
* enum
* configures StackWalker
* values
    * `RETAIN_CLASS_REFERENCE` - `StackFrame` will contain
    reference to the `Class` object of the method represented
    by this frame
    * `SHOW_HIDDEN_FRAMES` - show all hidden frames (for example -
    implementation specific and reflection frames are not included
    by default)
    * `SHOW_REFLECT_FRAMES` - show only reflection frames (
    specific implementation frames could still be hidden)
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
* creating instance of `StackWalker`
    * `static StackWalker getInstance()`
    * `static StackWalker getInstance(Option option)`
    * `static StackWalker getInstance(Set<Option> options)`
    * `static StackWalker getInstance(Set<Option> options, int estimateDepth)`
        * `estimateDepth` specifies the estimate number of stack frames
          this `StackWalker` will traverse that the `StackWalker` could
          use as a hint for the buffer size
* traverse the stack
    * `void forEach(Consumer<? super StackFrame> action)`
        * traversing from the top frame of the stack, which is the method calling this `forEach` method
    * `<T> T walk(Function<? super Stream<StackFrame>, ? extends T> function)`    
        * returning a Stream<StackFrame> would be unsafe, as the stream could
          be used to access the stack frames in an uncontrolled manner
        * applies the given function to the stream of `StackFrames`
          for the current thread, traversing from the top frame of the stack
        * stream will be closed when this method returns
        * the Java virtual machine is free to reorganize a thread's control stack, for example, via
          deoptimization - by taking a `Function` parameter, this method allows access to stack frames 
          through a stable view of a thread's control stack