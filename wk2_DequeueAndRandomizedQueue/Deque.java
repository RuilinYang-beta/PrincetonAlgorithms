import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

// try to implement with doubly linked list
public class Deque<Item> implements Iterable<Item> {
    private final Node startHandle;
    private final Node endHandle;
    private int size;

    private class Node {
        Item item;
        Node prev;
        Node next;
    }

    // construct an empty deque
    public Deque() {
        startHandle = new Node();
        endHandle = new Node();
        size = 0;

        startHandle.next = endHandle;
        endHandle.prev = startHandle;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        itemCantBeNull(item);

        Node oldFirst = startHandle.next;
        Node newFirst = new Node();
        newFirst.item = item;
        newFirst.prev = startHandle;
        newFirst.next = oldFirst;

        startHandle.next = newFirst;
        oldFirst.prev = newFirst;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        itemCantBeNull(item);

        Node oldLast = endHandle.prev;
        Node newLast = new Node();
        newLast.item = item;
        newLast.next = endHandle;
        newLast.prev = oldLast;

        endHandle.prev = newLast;
        oldLast.next = newLast;
        size++;
    }


    // remove and return the item from the front
    public Item removeFirst() {
        cannotRemoveFromEmptyDeque();

        Item oldFirstItem = startHandle.next.item;
        startHandle.next = startHandle.next.next;
        startHandle.next.prev = startHandle;
        size--;

        return oldFirstItem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        cannotRemoveFromEmptyDeque();

        Item oldLastItem = endHandle.prev.item;
        endHandle.prev = endHandle.prev.prev;
        endHandle.prev.next = endHandle;
        size--;

        return oldLastItem;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = startHandle;

        public boolean hasNext() {
            return (current.next.item != null);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (current.next.item == null) {
                throw new NoSuchElementException();
            }
            current = current.next;
            return current.item;
        }
    }


    private void itemCantBeNull(Item i) {
        if (i == null) {
            throw new IllegalArgumentException();
        }
    }

    private void cannotRemoveFromEmptyDeque() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> dq = new Deque<>();
        StdOut.println("freshly constructed dq is empty: " + dq.isEmpty());  // should be empty after construction
        StdOut.println("freshly constructed dq has size: " + dq.size());     // shoule be 0

        dq.addFirst(1);
        dq.addLast(3);
        dq.addFirst(5);
        dq.addLast(7);
        dq.addFirst(9);

        System.out.println("now has size: " + dq.size());   // should be 5

        Iterator<Integer> iter = dq.iterator();
        while (iter.hasNext()) {
            StdOut.print(iter.next());               // should be 9 5 1 3 7
            StdOut.print(" ");
        }
        StdOut.println();

        dq.removeFirst();
        dq.removeFirst();
        dq.removeLast();

        System.out.println("size: " + dq.size());   // should be 2
        iter = dq.iterator();
        while (iter.hasNext()) {
            StdOut.print(iter.next());              // should be 1 3
            StdOut.print(" ");
        }
    }

}
