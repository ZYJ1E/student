package test;



import java.util.LinkedList;

/*
泛型:
https://blog.csdn.net/s10461/article/details/53941091?
https://www.runoob.com/java/java-generics.html
java LinkedList的用法解析:
https://blog.csdn.net/linZinan_/article/details/114604893?
 */
public class EventLogger {
    public static final int UPDATE = 1010;
    public static final int DEL = 102;
    public static final int ADD = 103;
    private static EventLogger logger = null;
    private final LinkedList<Object[]> events;//链表初始化 链表的元素都是初始Object[] 泛型传参

    private EventLogger() {
        events = new LinkedList<>();
    }

    public static EventLogger getEventLogger() {
        if (logger == null) {
            logger = new EventLogger();
        }
        return logger;
    }

    private void operate(Object[] event) {
        events.add(event);
        System.out.println("logger has " + events.size());
        if (events.size() > 1000) {
            events.removeFirst();
        }
    }

    public void add(String id) {
        var res = new Object[]{ADD, id};
        operate(res);
    }

    public void remove(Student student) {
        var res = new Object[]{DEL, student};
        operate(res);
    }

    public void update(Student student) {
        var res = new Object[]{UPDATE, student};
        operate(res);
    }

    public Object[] takeOutLastEvent() {
        return events.pollLast();
    }

    public boolean isEmpty() {
        return events.size() == 0;
    }
}

