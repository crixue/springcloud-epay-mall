该项目是Eureka的高可用节点，在实际部署项目中按照以下命令启动项目：
java -jar xxx.jar --spring.profiles.active=peer1
java -jar xxx.jar --spring.profiles.active=peer2
即可，另一个节点配置文件在doc当中