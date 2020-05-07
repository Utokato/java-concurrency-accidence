# 说明
1. 模拟一个银行的叫号服务，在第一个版本中BankVersion1中，银行窗口(TicketWindow)继承了Thread类，这实际上耦合性比较大。
    将叫号的算法和运行线程耦合在了一起。在测试过程中，总票数需要通过static来修饰，这样一来这个数据就会伴随整个程序所有线程的生命周期，浪费了一定的空间。
    如果不使用static来修饰的话，多线程下每个线程都有一份独立的票，不能实现多个线程竞争一份票号
2. 第二个版本中，我们的银行窗户(TicketWindowRunnable)类实现了Runnable接口，实现了其中的run方法
    这就是一个单纯的任务类，然后在测试中，我们创建多个线程，这些线程都来执行这个TicketWindowRunnable任务，就实现了多个线程并发竞争这个任务。
    通过Runnable接口，将线程类和任务类(run方法)进行了分离，更加适合单一原则
    并且，在应对变化时，只需要扩展一个类来重新实现Runnable接口及run方法，也符合开闭原则，这也就是接口存在最大价值。
3. 这里需要深入理解Thread类和Runnable接口之间的关系
4. 为了理解Thread类和Runnable接口之间的关系，我们自定义了一个税法计算器类(TaxCalculator)和计算策略接口(CalculatorStrategy)
    其中TaxCalculator相当于Thread类，其中的calculate方法相当于start方法
    就像Thread需要一个Runnable接口的实现类来定义run方法一样，TaxCalculator需要一个CalculatorStrategy接口来定义真正的计算方法
    
    
Runnable接口的存在，将线程运行和业务逻辑分开，实现单一控制