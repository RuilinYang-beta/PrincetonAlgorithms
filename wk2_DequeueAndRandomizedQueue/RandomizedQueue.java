import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    // invariant: make the array fully filled from index [0, end] any time after any method finishes

    private Item[] arr;
    private int end;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        arr = (Item[]) new Object[1];
        end = -1;  // end pointing to the last valid element
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        // no more free cells, enlarging the array
        if (end == arr.length - 1) {
            resize(arr.length * 2);
        }
        // first increment to point to the next available cell, then fill the cell
        arr[++end] = item;
        size++;
    }

    private void resize(int newSize) {
        Item[] newArr = (Item[]) new Object[newSize];
        for (int i = 0; i < end + 1; i++) {
            newArr[i] = arr[i];
        }
        arr = newArr;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        // find a random value to rm
        int chosenIndex = StdRandom.uniform(0, end + 1);
        Item chosenItem = arr[chosenIndex];
        // fill the hole with last valid element
        arr[chosenIndex] = arr[end];
        end--;
        arr[end + 1] = null;
        // decr size
        size--;
        // consider shrinking the array
        if (end > 0 && end == arr.length / 4) {
            resize(arr.length / 2);
        }
        return chosenItem;
    }


    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int chosenIndex = StdRandom.uniform(0, end + 1);
        return arr[chosenIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final Integer[] indexArr;
        private int currentPointer;

        RandomizedQueueIterator() {
            indexArr = new Integer[end + 1];
            for (int i = 0; i < indexArr.length; i++) {
                indexArr[i] = i;
            }
            StdRandom.shuffle(indexArr);
            currentPointer = -1;
        }

        public boolean hasNext() {
            return currentPointer + 1 < indexArr.length;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (currentPointer == indexArr.length - 1) {
                throw new NoSuchElementException();
            }
            currentPointer++;
            int chosenIndex = indexArr[currentPointer];
            return arr[chosenIndex];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        StdOut.println("freshly constructed dq is empty: " + rq.isEmpty());  // should be empty after construction
        StdOut.println("freshly constructed dq has size: " + rq.size());     // shoule be 0

        for (int i = 0; i < 10; i++) {                 // add 10 items, 0 through 9
            rq.enqueue(i);
        }
        StdOut.println("adding 10 items, now size is: " + rq.size());   // size should be 10

        for (int i = 0; i < 5; i++) {
            rq.dequeue();
        }
        StdOut.println("removing 5 items, now size is: " + rq.size());   // size should be 5


        Iterator<Integer> iter = rq.iterator();
        StdOut.print("remaining items are: ");       // print the 5 remaining items
        while (iter.hasNext()) {
            StdOut.print(iter.next());
            StdOut.print(" ");
        }
        StdOut.print("\n");


        for (int i = 0; i < 10; i++) {               // do 10 times random sampling out of the 5 items
            StdOut.println("random sample: " + rq.sample());
        }

        for (int j = 0; j < 3; j++) {                // go through the remaining items 3 times, each time random order
            iter = rq.iterator();
            StdOut.print("walk through randomly: ");
            while (iter.hasNext()) {
                StdOut.print(iter.next());
                StdOut.print(" ");
            }
            System.out.print("\n");
        }


    }

}
