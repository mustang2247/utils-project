提供一个棋牌游戏的骨架，开发新的玩法时可以共用。

使用方法：

GameServer.getInstance().setChannelInitializer( xxx );
GameServer.getInstance().initServer( xxx );
GameServer.getInstance().startServer();

TODO

把聊天功能写完——可能要加个callback √
codec系统，提供一下amf3编解码    √
在net模块新建netty的channelInitializer
一个房间模型，定时启动任务  √
事件驱动的两种模式，一个是每次将下一个事件扔入timer
另一个是一个房间在开始时将自己加入一个eventloop，结束时从eventloop中拿出，eventloop以一定的频率进行检测，对列表中元素调用update方法。

分发系统，根据命令号，分发命令，预先用了两个线程池名称 “SYNC” “DIRECT”，做特殊处理  √
目前设想的是在Login时将UserID放到ChannelHandlerContext的attr中，其他业务则是读取这个attr获得UserID √


如果房间（或者房间组）单独为进程，通过端口对外提供服务。

是否需要将UserID和Channel的映射关系也加进来。  √

做一个兼容多协议的Codec  √

写一个简单的客户端框架

最后断开连接的时候，还会收到一个decodeLast的通知，需要处理


github仓库生成:
mvn deploy -DaltDeploymentRepository=mustang-mvn-repo::default::file:/Users/muskong/code/maven-repo2/repository/
