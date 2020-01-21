# springcloud-eureka-monitor-zuul
微服务整合  

前面用Nginx转发到zuul上

配置nginx-conf

 #gzip  on;

     #配置上游服务器网关端口集群
    upstream  backServer{
        server 192.168.2.177:8080 weight=1;
        server 192.168.2.177:8081 weight=1;
    }

    server {
        listen       80;
        server_name  www.brade.com;
        
        
        
        
        请求接口方式POST：http://www.brade.com/partition/app/token/partitionRedPacketUser/partition
        
        partition 通过网关配置映射到对应的jdd-partition项目中




