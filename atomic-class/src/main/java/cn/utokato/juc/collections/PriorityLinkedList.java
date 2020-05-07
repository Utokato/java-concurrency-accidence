package cn.utokato.juc.collections;

/**
 * @author lma
 * @date 2019/06/06
 */
public class PriorityLinkedList<E extends Comparable<E>> {

    private Node<E> first;

    private final Node<E> NULL = null;

    private final static String PLAIN_NULL = "NULL";

    private int size;

    public PriorityLinkedList() {
        this.first = NULL;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public static <E extends Comparable<E>> PriorityLinkedList<E> of(E... elements) {
        final PriorityLinkedList<E> list = new PriorityLinkedList<>();
        if (elements.length != 0) {
            for (E e : elements) {
                list.addFirst(e);
            }
        }
        return list;
    }

    public PriorityLinkedList<E> addFirst(E e) {
        final Node<E> newNode = new Node<>(e);
        Node<E> previous = NULL;
        Node<E> current = first;
        while (current != null && e.compareTo(current.value) > 0) {
            // 通过while循环找到新增节点的位置
            previous = current;
            current = current.next;
        }
        if (previous == NULL) {
            first = newNode;
        } else {
            previous.next = newNode;

        }
        newNode.next = current;
        size++;
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
            StringBuilder builder = new StringBuilder("[ ");
            Node<E> current = first;
            while (current != null) {
                builder.append(current.toString()).append(",");
                current = current.next;
            }
            builder.replace(builder.length() - 1, builder.length(), " ]");
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
        PriorityLinkedList<Integer> list = PriorityLinkedList.of(10, 9, 8, 7, 0, -10);
        System.out.println(list);
    }
}
