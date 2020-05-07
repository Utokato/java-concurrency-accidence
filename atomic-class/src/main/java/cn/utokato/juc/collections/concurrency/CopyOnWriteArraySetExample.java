package cn.utokato.juc.collections.concurrency;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * {@link CopyOnWriteArraySet}
 * 通过 {@link CopyOnWriteArrayList}来实现
 * add 时进行判断，没有该元素时才加入
 * <p>
 * {@link CopyOnWriteArraySet#add(Object)}
 * {@link CopyOnWriteArrayList#addIfAbsent(Object)}
 *
 * @author lma
 * @date 2019/06/13
 */
public class CopyOnWriteArraySetExample {
}
