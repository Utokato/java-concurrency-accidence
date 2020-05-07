# Semaphore

有时候需要多个线程都获得同一把锁，去做一件事情，那怎么办呢？



没关系，信号量（Semaphore）出马，创建信号量的时候得指定一个整数(例如10)， 表明同一时刻最多有10个线程可以获得锁： 

Semaphore semaphore= new Semaphore(10);



当然每个线程都需要调用semaphore.aquire(), semaphore.release()去申请/释放锁。 