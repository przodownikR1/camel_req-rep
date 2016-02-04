package pl.java.scalatech.component;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class Counter {
  @Getter  
  private AtomicInteger countMessage = new AtomicInteger();
}

