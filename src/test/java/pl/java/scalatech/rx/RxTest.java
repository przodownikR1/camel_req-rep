package pl.java.scalatech.rx;

import org.junit.Test;

import rx.Observable;
import rx.functions.Action1;

public class RxTest {
    @Test
    public void test2(){
        Observable.from(new Integer[]{1, 2, 3, 4, 5,6,7,7})
        .filter(i -> i > 2)
        .toList()
        .subscribe(System.out::println);

    }
    @Test
    public void test3(){
        Observable.just("a","b","c","d","e","f")
        .window(2,2)
        .subscribe(new Action1<Observable<String>>() {
            @Override
            public void call(Observable<String> stringObservable) {
                System.out.println("stringObservable = " + stringObservable);
                stringObservable.subscribe(s -> System.out.println("s = " + s));
            }
        });
    }

}
