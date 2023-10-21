package edu.luc.cs.consoleapp;

import java.util.Queue;
import java.util.stream.Stream;

import org.apache.commons.collections4.queue.CircularFifoQueue;



class SlidingQueue {

  private final Queue<String> queue;

  private SlidingQueue(final int queueSize) {
    this.queue = new CircularFifoQueue<>(queueSize);
  }

   public SlidingQueue process(final Stream<String> input, final OutputObserver output) {
    Stream<String> transformedInput = transformWithMapAndScanLeft(input);

    int queueCapacity = 5; 
    Stream<Queue<String>> slidingQueueInput = transformToSlidingQueue(transformedInput, queueCapacity);

    Stream<Integer> longestWordLengths = transformToLongestWordLengths(slidingQueueInput);

    return this; 
  }

  private Stream<String> transformWithMapAndScanLeft(Stream<String> input) {
    return input.map(s -> s.toUpperCase()).scanLeft("", (acc, item) -> acc + item);
  }

  private Stream<Queue<String>> transformToSlidingQueue(Stream<String> input, int capacity) {
    SlidingQueue slidingQueue = new SlidingQueue(capacity);
    return input.scanLeft(slidingQueue, (queue, item) -> {
      queue.add(item);
      return queue;
    });
  }

  private Stream<Integer> transformToLongestWordLengths(Stream<Queue<String>> input) {
    return input.map(queue -> {
      int maxWordLength = 0;
      for (String word : queue) {
        maxWordLength = Math.max(maxWordLength, word.length());
      }
      return maxWordLength;
    });
  }
}

