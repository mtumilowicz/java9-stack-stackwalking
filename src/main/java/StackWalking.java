import java.util.Set;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;
import static java.lang.StackWalker.Option.SHOW_HIDDEN_FRAMES;

/**
 * Created by mtumilowicz on 2019-02-18.
 */
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
