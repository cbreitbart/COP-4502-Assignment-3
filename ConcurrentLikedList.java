import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentLinkedList {
    private class Node {
        int data;
        Node next;
        Node prev;

        Node(int data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private Node head = null;
    private Node tail = null;
    private final ReentrantLock lock = new ReentrantLock();
    private int size = 0;

    public void remove(int key) {
        lock.lock();
        try {
            if (head == null) {
                return;
            }

            Node current = head;
            if (current.data == key) {
                head = head.next;
                if (head != null) {
                    head.prev = null;
                }
                size--;
            } else {
                while (current.next != null) {
                    if (current.next.data == key) {
                        Node temp = current.next;
                        current.next = current.next.next;

                        if (current.next != null) {
                            current.next.prev = current;
                        }

                        size--;
                        break;
                    }
                    current = current.next;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public int removeHead() {
        lock.lock();
        try {
            if (head == null) {
                return Integer.MIN_VALUE;
            }

            int value = head.data;
            head = head.next;
            if (head != null) {
                head.prev = null;
            }

            size--;
            return value;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        return size;
    }

    public boolean empty() {
        return head == null;
    }

    public void insert(int data) {
        lock.lock();
        try {
            Node newNode = new Node(data);

            if (head == null) {
                head = newNode;
                tail = newNode;
            } else if (head.data >= newNode.data) {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            } else {
                Node current = head;

                while (current.next != null && current.next.data < newNode.data) { //Sorted in ascending order
                    current = current.next;
                }

                newNode.next = current.next;
                if (current.next != null) {
                    current.next.prev = newNode;
                }

                current.next = newNode;
                newNode.prev = current;

                if (newNode.next == null) {
                    tail = newNode;
                }
            }

            size++;
        } finally {
            lock.unlock();
        }
    }

    public boolean contains(int key) {
        lock.lock();
        try {
            Node current = head;

            while (current != null) {
                if (current.data == key) {
                    return true;
                }
                current = current.next;
            }

            return false;
        } finally {
            lock.unlock();
        }
    }
}
