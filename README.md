1、myrpc-spring-boot-starter是源码工程，myrpc-provider-test和myrpc-consumer-test是测试工程。
2、首先要把MyRPC根项目进行打包install,然后再把myrpc-provider-test和myrpc-consumer-test进行
打包install,并且启动，之后访问http://localhost:8081/hello?msg=haha，会从页面返回hello:haha!
3、注意：myrpc默认使用8888端口，如有端口占用，请更换，还有本项目默认使用JDK11环境运行。

基于Zookeeper实现简易版服务的注册与发现机制
    模块：myrpc-spring-boot-starter
    主类：com.jin.myrpc.spirngboot.registry.ZkRegister
实现基于Zookeeper的简易版负载均衡策略
    模块：myrpc-spring-boot-starter
    主类：com.jin.myrpc.spirngboot.cluster.LeastActiveLoadBalance
基于Zookeeper实现简易版配置中心
    模块：myconfig-center-springboot-starter
    测试模块：myconfig-center-springboot-test
