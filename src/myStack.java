import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Created by llxxll on 1/23/2018.
 */
public class myStack<Item> implements Iterable<Item> {
    private node<Item> top; // top of stack
    private int n;

    class node<Item> {
        Item item;
        node<Item> next;
        node(Item item){this.item = item; this.next = null;}
        node(Item item, node<Item> next) {this.item = item; this.next = next;}
    }

    public myStack() {
        top = null;
        n = 0;
    }

    public boolean isEmpty() {return top == null;}

    public int size() {return n;}

    public void push(Item item) {
        node<Item> preTop = top;
        top = new node<Item>(item, preTop);
        n ++;
    }

    public Item pop() {
        if (isEmpty()) throw new NoSuchElementException("The stack is currently empty.");
        Item item = top.item;
        top = top.next;
        n --;
        return item;
    }

    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("The stack is currently empty.");
        return top.item;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Item item : this) {
            str.append(item + " ");
        }
        return str.toString();
    }

    public void print() {
        Iterator<Item> iter = this.iterator();
        System.out.println("STACK ->");
        while (iter.hasNext()) System.out.println(iter.next());
        System.out.println("FIN.");
    }

    public Iterator<Item> iterator() {return new stackIterator<Item>(top);}

    private class stackIterator<Item> implements Iterator<Item> {
        private node<Item> cur;
        private node<Item> pre = new node<>(null);
        public stackIterator(node<Item> top) {cur = top;}
        public boolean hasNext() {return cur != null;}
        public void remove() {
            /* remove current */
            if (cur != null) {
                pre.next = cur.next;
                cur = pre;
                n --;
            } else {
                throw new NoSuchElementException("No current element to remove");
            }
        }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No more elements in stack. ");
            Item item = cur.item;
            pre = cur;
            cur = cur.next;
            return item;
        }
    }

    public static void main(String[] args) {
        myStack<Integer> s = new myStack<>();
        for (int i = 0;i < 5; i++) s.push(i);
        System.out.println(s.toString());
        System.out.println(s.peek());
        System.out.println(s.pop());
        System.out.println(s.toString());
        Iterator<Integer> iter = s.iterator();
        //while(iter.hasNext())   System.out.println(iter.next());
        iter.remove();
        Iterator<Integer> iter1 = s.iterator();
        while (iter1.hasNext()) System.out.println(iter1.next());
        System.out.println(s.toString());
        s.print();
    }
}
