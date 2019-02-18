[![Build Status](https://travis-ci.com/mtumilowicz/java9-stack-stackwalking.svg?branch=master)](https://travis-ci.com/mtumilowicz/java9-stack-stackwalking)

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
# project description
1. for test purposes
    ```
    class StackWalking {
        void m1() {
            m2();
        }
    
        private void m2() {
            m3();
        }
    
        private void m3() {
            System.out.println("---");
            System.out.println("stack from m3():");
            StackWalker.getInstance(Set.of(RETAIN_CLASS_REFERENCE, SHOW_HIDDEN_FRAMES), 4)
                    .walk(frames -> {
                        frames.limit(4).forEach(System.out::println);
                        return null;
                    });
            System.out.println("---");
    
            m4();
        }
    
        private void m4() {
            System.out.println("---");
            System.out.println("stack from m4():");
            StackWalker.getInstance(Set.of(RETAIN_CLASS_REFERENCE, SHOW_HIDDEN_FRAMES), 5)
                    .walk(frames -> {
                        frames.limit(5).forEach(System.out::println);
                        return null;
                    });
            System.out.println("---");
        }
    }
    ```
1. simulation
    ```
    @Test
    public void simulation() {
        new StackWalking().m1();
    }
    ```
    produces
    ```
    ---
    stack from m3():
    StackWalking.m3(StackWalking.java:22)
    StackWalking.m2(StackWalking.java:15)
    StackWalking.m1(StackWalking.java:11)
    StackWalkingTest.simulation(StackWalkingTest.java:10)
    ---
    ---
    stack from m4():
    StackWalking.m4(StackWalking.java:35)
    StackWalking.m3(StackWalking.java:28)
    StackWalking.m2(StackWalking.java:15)
    StackWalking.m1(StackWalking.java:11)
    StackWalkingTest.simulation(StackWalkingTest.java:10)
    ---
    ```