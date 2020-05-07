package cn.utoakto.concurrency.chapter12;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @date 2019/5/3
 */
public class BalkingData {


    private final String fileName;
    private String content;
    private boolean changed;

    public BalkingData(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
        this.changed = true;
    }

    public synchronized void change(String newContent) {
        this.content = newContent;
        this.changed = true;
    }

    /**
     * 如果没有发生改变，直接返回
     * 如果发生了改变，通过doSave方法真正执行保存的逻辑
     */
    public synchronized void save() throws IOException {
        if (!changed) return;
        doSave();
        this.changed = false;
    }

    private void doSave() throws IOException {
        System.out.println(Thread.currentThread().getName() + " calls to do save, content is " + content);
        try (Writer writer = new FileWriter(fileName, true)) {
            writer.write(content);
            writer.write("\n");
            writer.flush();
        }
    }
}
