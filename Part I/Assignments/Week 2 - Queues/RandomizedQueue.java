import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] randQueue;
    private int count;
    private Item retItem;
    private int tmpIndex;


    // construct an empty randomized queue
    public RandomizedQueue(){
        randQueue = (Item[]) new Object[2];
    }

    private void resize(int dim){
        Item newRandQueue[] = (Item[]) new Object[dim];

        for(int i=0; i<count;i++){
            if(randQueue[i] == null)
                break;
            newRandQueue[i] = randQueue[i];
        }
        
        randQueue = newRandQueue;
    }

    // is the randomized queue empty?
    public boolean isEmpty(){
        return count == 0;
    }

    // return the number of items on the randomized queue
    public int size(){
        return count;
    }

    // add the item
    public void enqueue(Item item){
        if (item == null)
            throw new IllegalArgumentException("Null argument!");

        randQueue[count++] = item;

        if(count == randQueue.length)
            resize(randQueue.length * 2);
    }

    // remove and return a random item
    public Item dequeue(){
        if (isEmpty())
            throw new NoSuchElementException("Empty Deque");
        
        tmpIndex = StdRandom.uniform(count);
        retItem = randQueue[tmpIndex];
        randQueue[tmpIndex] = randQueue[count-1];
        count--;

        if(count == randQueue.length / 4)
            resize(randQueue.length / 2);
        
        return retItem;
    }

    // return a random item (but do not remove it)
    public Item sample(){
        if (isEmpty())
            throw new NoSuchElementException("Empty Deque");

        return randQueue[StdRandom.uniform(count)];
    }

    private Item[] arrayCpy(){
        Item[] copy = (Item[]) new Object[count];
        
        for(int i = 0; i < count; i++)
            copy[i] = randQueue[i];
        
        return copy;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandQueueIterator();
    }

    private class RandQueueIterator implements Iterator<Item> {
        
        private Item[] iterQueue = arrayCpy();
        private int length = iterQueue.length;
        private int index;
        private Item retItem;

        public boolean hasNext() {
            return length > 0;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("Empty Deque");
            
            index = StdRandom.uniform(length);
            retItem = iterQueue[index];
            iterQueue[index] = iterQueue[--length];

            return retItem;
        }

        public void remove() { // Not implemented
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args){

        RandomizedQueue<String> r = new RandomizedQueue<String>();

        StdOut.println(r.isEmpty());
        StdOut.println("Size: " + r.size());

        for(int i = 0; i < 10; i++)
            r.enqueue(String.valueOf(i + 1));

        StdOut.println("--------------------------------------------");
        
        StdOut.println(r.isEmpty());
        StdOut.println("Size: " + r.size());

        for(String i: r)
            StdOut.println(i);

        for(int i = 0; i < 5; i++)
            StdOut.println("Dequeue: " + r.dequeue());
        
        for(int i = 0; i < 5; i++)
            StdOut.println("Sample: " + r.sample());

        StdOut.println("--------------------------------------------");
        
        StdOut.println(r.isEmpty());
        StdOut.println("Size: " + r.size());

        for(String i: r)
            StdOut.println(i);

    }

}
