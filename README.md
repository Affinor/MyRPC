1、myrpc-spring-boot-starter是源码工程，myrpc-provider-test和myrpc-consumer-test是测试工程。
2、首先要把MyRPC根项目进行打包install,然后再把myrpc-provider-test和myrpc-consumer-test进行
打包install,并且启动，之后访问http://localhost:8081/hello?msg=haha，会从页面返回hello:haha!
3、注意：myrpc默认使用8888端口，如有端口占用，请更换，还有本项目默认使用JDK11环境运行。