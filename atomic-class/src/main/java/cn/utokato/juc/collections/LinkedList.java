package cn.utokato.juc.collections;

/**
 * @author lma
 * @date 2019/06/06
 */
public class LinkedList<E> {

    private Node<E> first;

    private final Node<E> NULL = null;

    private final static String PLAIN_NULL = "NULL";

    private int size;

    public LinkedList() {
        this.first = NULL;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public static <E> LinkedList<E> of(E... elements) {
        final LinkedList<E> list = new LinkedList<>();
        if (elements.length != 0) {
            for (E e : elements) {
                list.addFirst(e);
            }
        }
        return list;
    }

    public LinkedList<E> addFirst(E e) {
        final Node<E> newNode = new Node<>(e);
        newNode.next = first;
        this.size++;
        this.first = newNode;
        return this;
    }

    public boolean contains(E e) {
        Node<E> current = first;
        while (current != null) {
            if (current.value == e) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public E removeFirst() {
        if (this.isEmpty()) throw new NoElementException("The LinkedList is empty.");

        Node<E> toReturnNode = first;
        first = toReturnNode.next;
        size--;
        return toReturnNode.value;

    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "[]";
        } else {
            StringBuilder builder = new StringBuilder("[");
            Node<E> current = first;
            while (current != null) {
                builder.append(current.toString()).append(",");
                current = current.next;
            }
            builder.replace(builder.length() - 1, builder.length(), "]");
            return builder.toString();
        }
    }

    static class NoElementException extends RuntimeException {
        public NoElementException(String message) {
            super(message);
        }
    }

    private static class Node<E> {
        E value;
        Node<E> next;

        public Node(E value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return null != value ? value.toString() : PLAIN_NULL;
        }
    }

    public static void main(String[] args) {
        LinkedList<String> list = LinkedList.of("Hello", "World", "Scala", "Java", "Thread");
        list.addFirst("Concurrency").addFirst("Test");
        System.out.println(list.size());
        System.out.println(list.contains("Scala"));
        System.out.println("====================");
        System.out.println(list);
        while (!list.isEmpty()) {
            System.out.println(list.removeFirst());
        }
        System.out.println(list.size);
        System.out.println(list.isEmpty());
    }

}
