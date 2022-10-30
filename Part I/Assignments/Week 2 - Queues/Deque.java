import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Item[] deque;
    private int count;
    private int leftP; // left pointer
    private int rightP; // right pointer
    private int length;
    private int tmp;
    private boolean resetL;
    private boolean resetR;
    private int bias;
    private Item retItem;
    private int shiftValue;

    // construct an empty deque
    public Deque() {
        length = 2;
        deque = (Item[]) new Object[length];
        count = 0;
        leftP = length / 2 - 1;
        rightP = length / 2;

    }

    // it creates a new array
    private void resize(int dim) {
        Item[] newDeque = (Item[]) new Object[dim];
        tmp = (dim - length) / 2;
        resetL = true;
        resetR = false;

        if (tmp > 0) {  // enlarge array
            for (int i = 0; i < length; i++) {
                newDeque[i + tmp] = deque[i];   // center the elements in the array

                /* //DEBUG
                StdOut.println(newDeque[i + tmp]);
                */
                if (newDeque[i + tmp] != null && resetL) { // reset leftP
                    leftP = i + tmp - 1;
                    resetR = true; // unlock reset of rightP
                    resetL = false; // lock reset of leftP

                    /* //DEBUG
                    StdOut.println("\tCheck leftP: " + leftP);
                    */
                }

                if (newDeque[i + tmp] == null && resetR) { // reset rightP
                    rightP = i + tmp;
                    resetR = false; // lock reset of rightP

                    /* //DEBUG
                    StdOut.println("\tCheck rightP: " + rightP);
                    */

                    break; // cause every other element would be null
                }

            }
            if (resetR) // fix rightP wrong position when array enlarges and rightP was equal to lenght - 1
                rightP = dim - tmp;
        }

        else { // shrink array
            tmp = (length - dim) / 2;
            for (int i = 0; i < dim; i++) {
                newDeque[i] = deque[i + tmp];

                /* //DEBUG
                StdOut.println(newDeque[i]);
                */

                if (newDeque[i] != null && resetL) { // reset leftP
                    leftP = i - 1;
                    resetR = true; // unlock reset of rightP
                    resetL = false; // lock reset of leftP

                    /* //DEBUG
                    StdOut.println("\tCheck leftP");
                    */
                }

                if (newDeque[i] == null && resetR) { // reset rightP
                    rightP = i;
                    resetR = false; // lock reset of rightP

                    /* //DEBUG
                    StdOut.println("\tCheck rightP");
                    */

                    break; // cause every other element would be null
                }

            }
            if (resetL) { // if leftP hasn't been reset <=> all elements are null
                leftP = dim / 2 - 1;
                rightP = dim / 2;
            }
            if (resetR) // fix rightP wrong position when array shrinks and rightP was equal to lenght - 1
                rightP = dim - 1;
        }

        deque = newDeque; // swap arrays
        length = dim; // update array dim
        bias = (length - length / 2) / 2; // update bias
    }

    private void shiftLeft() {
        shiftValue = rightP - (length / 2 + (count / 2));
        //if (shiftValue <= 2)
        //    return;

        for (int i = leftP + 1; i < rightP; i++) {
            deque[i - shiftValue] = deque[i];
            deque[i] = null;
        }
        // reset leftP and rightP
        leftP -= shiftValue;
        rightP -= shiftValue;
    }

    private void shiftRight() {
        shiftValue = length / 2 - (count / 2 + 1) - leftP;
        //if (shiftValue <= 2)
        //    return;

        /* // DEBUG
        StdOut.println("shiftValue: " + shiftValue);
        */
        for (int i = rightP - 1; i > leftP; i--) {
            deque[i + shiftValue] = deque[i];
            deque[i] = null;
        }
        // reset leftP and rightP
        leftP += shiftValue;
        rightP += shiftValue;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null argument!");

        deque[leftP] = item;
        count++;
        if (leftP == 0) // array full to the left
            resize(length * 2);
        else {
            leftP--;
            // if the deque is unbalanced and leftP is on the second position or under
            // and the array is not going to be resized soon
            if (leftP + 1 <= length - rightP - 2 && leftP <= (((length - length / 2) /2) + 1) && count < length - 2) {
                /* // DEBUG
                StdOut.println("SHIFT RIGHT \t rightP: " + rightP + ", leftP: " + leftP);
                */
                shiftRight();

            }
        }

        /* // DEBUG
        for (int i = 0; i < length; i++) {
            if (deque[i] == null)
                StdOut.print(i + ": //\t");
            else StdOut.print(i + ": " + deque[i] + "\t");
        }
        StdOut.println("rightP: " + rightP + "leftP: " + leftP + "\n\n");
        */
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Null argument!");

        deque[rightP] = item;
        count++;
        if (rightP == length - 1)  // array full to the right
            resize(length * 2);
        else {
            rightP++;
            // if the deque is unbalanced, rightP in on the second to last position or above
            // and the array is not going to be resized soon
            if (length - rightP <= leftP - 1 && rightP >= length - ((length - length / 2) /2) - 1 && count < length - 4) {
                shiftLeft();

                /* // DEBUG
                StdOut.println("SHIFT RIGHT");
                 */
            }
        }

        /* // DEBUG
        for (int i = 0; i < length; i++) {
            if (deque[i] == null)
                StdOut.print(i + ": //\t");
            else StdOut.print(i + ": " + deque[i] + "\t");
        }
        StdOut.println("rightP: " + rightP + "leftP: " + leftP + "\n\n");
        */
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Empty Deque");
        retItem = deque[leftP + 1];
        deque[leftP + 1] = null;
        count--;

        if (leftP > bias)
            resize(length / 2);
        else {
            if (count == 0) {
                rightP = length / 2;
                leftP = length / 2 - 1;
            }
            else leftP++;
        }

        /* // DEBUG
        for (int i = 0; i < length; i++) {
            if (deque[i] == null)
                StdOut.print(i + ": //\t");
            else StdOut.print(i + ": " + deque[i] + "\t");
        }
        StdOut.println("rightP: " + rightP + "leftP: " + leftP + "\n\n");
        */
        return retItem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Empty Deque");
        retItem = deque[rightP - 1];
        deque[rightP - 1] = null;
        count--;


        if (rightP < length - bias - 2)
            resize(length / 2);
        else {
            if (count == 0) {
                rightP = length / 2;
                leftP = length / 2 - 1;
            }
            else rightP--;
        }

        /* // DEBUG
        for (int i = 0; i < length; i++) {
            if (deque[i] == null)
                StdOut.print(i + ": //\t");
            else StdOut.print(i + ": " + deque[i] + "\t");
        }
        StdOut.println("rightP: " + rightP + "leftP: " + leftP + "\n\n");
        */
        return retItem;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private int i = leftP;

        public boolean hasNext() {
            return i < rightP - 1;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("Empty Deque");
            return deque[++i];
        }

        public void remove() { // Not implemented
            throw new UnsupportedOperationException();
        }
    }


    // unit testing (required)
    public static void main(String[] args) {

        Deque<String> deck = new Deque<String>();

        StdOut.println("size: " + deck.size());
        StdOut.println(deck.isEmpty());

        deck.addFirst("1");
        StdOut.println("size: " + deck.size());
        deck.addFirst("2");
        deck.addFirst("3");
        deck.addFirst("4");
        StdOut.println("size: " + deck.size());
        deck.addLast("9");
        deck.addLast("10");

        for (String i : deck)
            StdOut.println(i);

        StdOut.println("size: " + deck.size());
        deck.removeFirst();
        StdOut.println("size: " + deck.size());
        deck.removeLast();
        StdOut.println("size: " + deck.size());
        deck.removeLast();
        deck.removeLast();

        for (String i : deck)
            StdOut.println(i + "\t");

        deck.removeLast();
        deck.removeFirst();

        StdOut.println("--------------------------------------------");

        for (int i = 0; i < 25; i++) {
            StdOut.println(i + 1);
            deck.addFirst(String.valueOf(i + 1));
            StdOut.println(deck.removeLast());
        }

        StdOut.println("--------------------------------------------");
        StdOut.println(deck.isEmpty());

        for (int i = 0; i < 50; i++) {
            StdOut.println(i + 1);
            deck.addLast(String.valueOf(i + 1));
            StdOut.println(deck.removeFirst());

        }

        StdOut.println("--------------------------------------------");

        for (int i = 0; i < 50; i++) {
            StdOut.println(i + 1);
            deck.addFirst(String.valueOf(i + 1));
            StdOut.println(deck.removeLast());

        }

        StdOut.println("--------------------------------------------");

        int e = 0;
        for (String i : deck)
            StdOut.println(e++ + ": " + i);
        

    }

}
