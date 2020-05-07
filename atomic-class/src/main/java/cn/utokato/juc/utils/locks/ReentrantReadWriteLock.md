# ReentrantReadWriteLock

一个线程要写共享变量， 可是还有几个线程要同时读， 怎么办？ 你写的时候可以锁住， 但总不能读的时候也只允许一个线程吧？  

只好来一个读写锁了ReadWriteLock， 为了保证可重入性， 元老院体贴的实现了ReentrantReadWriteLock。