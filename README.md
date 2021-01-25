# Store
模块：
商品服务 


仓储服务 


订单服务  

优惠券服务 

用户服务  


共同： 

1）web openfeign 

2) 每个服务，包名com.store.xxx(product/order/ware/coupon/memeber) 

3) 模块名：store-coupon 

springCloudAlibaba的优势 

SpringCloudAlibaba-Nacos:注册中心（服务发现/注册） 

SpringCloudAlibaba-Nacos:配置中心（动态配置管理） 

SpringCloud-Ribbon:负载均衡 

SpringCloud-Feign：声明式HTTP客户端（调用远端服务） 

SpringCloudAlibaba-Sentinel:服务容错（限流、降级、熔断） 

SpringCloud-Gateway：API 网关（webflux编程模式） 

SpringCloud-Sleuth：调用链监控 

SpringCloudAlibaba-Seata：原分布式事务解决方案 

## Nacos注册中心
单机模式启动的命令：sh startup.sh -m standalone 

本地单机模式要改配置，具体

## ElasticSearch
概念：1.索引-->datablase 2.类型-->table 3.文档-->数据行 4.属性-->mysql列 

docker安装es和kibana 

sudo docker pull elasticsearch 

docker pull kibana:7.4.2 

创建实例 

1.ElasticSearch 

mkdir -p /mydata/elasticsearch/config 

mkdir -p /mydata/elasticsearch/data 

echo "http.host: 0.0.0.0" >> /mydata/elasticsearch/config/elasticsearch.yml 

docker run --name elasticsearch -p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JVAV_OPTS="-Xms64m -Xmx128m -XX:+UseG1GC" \
-v /Users/Java/mydata/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /Users/Java/mydata/elasticsearch/data:/usr/share/elasticsearch/data \
-v /Users/Java/mydata/elasticsearch/plugin:/usr/share/elasticsearch/plugin \
-d elasticsearch:7.4.2 

启动后访问浏览器127.0.0.1:9200端口失败，docker logs elasticsearch可以查看错误日志 

安装kibana 

docker run --name kibana -e ELASTICSEARCH_HOSTS=http://192.168.124.8:9200 -p 5601:5601 \
-d kibana:7.4.2
### 初步检索
1._cat  

GET _cat/nodes:查看所有节点  

GET _cat/health:查看es健康状况 

GET _cat/master:查看主节点 

GET _cat/indices:查看所有索引 show databases;  


#### 2.索引一个文档（保存）  

保存一个数据，保存着哪个索引的哪个类型下，哪个数据库的哪个标识 
PUT customer/external/1: 在customer索引下的external类型下保存1号数据为 

#### 3.查询文档 
/customer/external/1 

结果 

{ 

    "_index": "customer",//在哪个索引 

    "_type": "external", //在哪个类型 

    "_id": "1", //记录id 

    "_version": 1, //版本号 

    "_seq_no": 0, //并发控制字段，每次更新就会+1，用来做乐观锁 

    "_primary_term": 1, //同上，主分片重新分配，如重启，就会变化 

    "found": true, 

    "_source": { //真正的内容 

        "name": "Jone Doe" 

   } 

} 

4.更新携带 ?if_seq_no=0&if_primary_term=1 

更新文档 
post customer/external/1/_update 

{
    "doc": {
        "name": "John Dewn"
    }
} 

重复提交相同内容，版本号不会改变


更新同时增加属性 
方式1：post带update，json内容必须带doc 

{ 

    "doc": { 

        "name": "John Dewn", 

	  "age": "20" 

    } 

} 
方式2:post和put不带update 

{ 

&nbsp;&nbsp;&nbsp;&nbsp;"name": "John Dewn", 

&nbsp;&nbsp;&nbsp;&nbsp;"age": 20 

} 

5.删除文档&索引 

delete /customer/external/1/ 

delete /customer/

6.bulk批量导入数据  

elasticsearch官方数据地址：https://raw.githubusercontent.com/elastic/elasticsearch/master/docs/src/test/resources/accounts.json 
语法格式 
{"index": {"_id": "1"}} \
{"name": "John Doe"}
{"index": {"_id": "2"}}
{"name": "John"}

## searchAPI 
ES支持两种请求方式

1.通过REST request URI发送搜索参数（uri+检索参数） 

2.通过REST request body来发送他们（uri请求） 

queryDSL的查询结构
{     
&nbsp;&nbsp;&nbsp;&nbsp;QUERY_NAME:{  

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ARGUMENT:VALUE,  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ARGUMENT:VALUE, 

&nbsp;&nbsp;&nbsp;&nbsp;}

}  

如果是针对某个字段，它的结构如下： 
{     
&nbsp;&nbsp;&nbsp;&nbsp;QUERY_ NAME:{  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FIELD__NAME:{  

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ARGUMENT:VALUE,  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ARGUMENT:VALUE,  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}


&nbsp;&nbsp;&nbsp;&nbsp;}

}  

## aggregations聚合 （执行聚合） 

搜索address中包含mill的所有人的年龄分布以及平均年龄，但不显示这些人的详情 

## 分词
一个tokenizer（分词器）接受一个字符流，将值分给为独立的tokens，然后输出分词流 

https://github.com/medcl/elasticsearch-analysis-ik/releases?after=v7.7.0

docker run -p 80:80 --name nginx \
-v /Users/Java/mydata/nginx/html:/usr/share/nginx/html \
-v /Users/Java/mydata/nginx/logs:/var/log/nginx \
-v /Users/Java/mydata/nginx/conf/:/etc/nginx -d nginx:1.10 




## Dokcer安装RabbitMQ

docker run -d --name rabbitmq -p 5671:5671 -p 5672:5672 -p 4369:4369 -p 25672:25672 -p 15671:15671 -p 15672:15672 rabbitmq:management 

4369，25672（Elang发现&集群端口） 

5672，5671（AMQP端口） 

15672（web管理后台端口） 

61613，61614（STOMP协议端口） 

1883，8883（MQTT协议端口） 

docker update rabbitmq --restart=always

### RabbitMQ运行机制 
Exchange类型 
direct:直接交换机，单播模式、点对点通信模式完全匹配 

fanout：实现发布订阅，广播模式，不关心路由件是什么 

topic：发布订阅模式，部分广播，绑定关系可以用通配符，#匹配0个或多个单词，*匹配一个单词  

## 认证中心（社交登录，Oauth2.0，单点登录）

## 购物车
离线购物车（临时购物车）： 

浏览器即使关闭，下次进入临时购物车数据还在  
存储数据选型 

localstorage （浏览器存储）缺点：无法分析数据，个性化推荐

redis （采用）

登录购物车 ：登录以后会将临时购物车的数据合并，临时购物车会被清空 

放入数据库 mangodb ：读写高并发，数据库压力大（不采用） 

放入redis： 

优点：数据结构好组织，并发性能高 ，

缺点：内存数据库，解决：指定redis持久化效率

- 用户可以使用购物车结算下单 
- 给购物车添加商品 
- 修改商品的数量、移除、选中商品
- 在购物车中展示优惠信息 
- 提示购物车的价格变化

## 幂等性 
### 天然幂等性： 

select * from table_name where id=? 无论执行多少次都不会改变状态，是天然幂等的 

update tab1 set co1=1 where co2=2, 无论执行多少次都不会改变状态，是天然幂等的  

delete from user where userId=1,多次操作，结果一样，具备幂等性 

insert into user(userid,username) values(1,'a');//userid为唯一主键，重复操作上面的操作，只会插入一条用户数据，具备幂等性 

### 不具备幂等性
update tab1 set  col1=col1+1 where col2=2,每次执行结果都会发生变化，不是幂等 

nsert into user(userid,username) values(1,'a');userid不是主键，可以重复，不是幂等的 

## 幂等解决方案
### 1.token机制
1.服务端提供了发送token的接口。哪些业务存在幂等性问题，就要在业务执行之前，先去获取token，服务器会把token保存在redis中。 

2.客户端调用服务器请求的时候，把token放入请求头带过去， 

3.服务器判断token是否在redis中，存在表示第一次请求，然后删除token，继续执行业务 

4.如果判断token不存在redis中，就表示重复操作，直接返回重复标识给客户端，保证业务代码不被重复执行。 

#### 危险性
1.先删除token还是后删除token 

先删除可能导致：业务没有执行，重试还带上之前的token，导致重试也不能执行 

后删除可能导致：业务处理成功，但是服务闪断，出现超时，没有删除token，重试后，导致业务执行两次 

2.token获取，比较和删除必须是原子性 

1.redis.get(token), token.equals, redis.del(token)必须为原子性，可以通过lua脚本执行 

### 2.各种锁机制
1.数据库悲观锁 

select * from xxx where id=1 for update; id字段一定是唯一索引或者主键，不然可能造成锁表 

2.数据库乐观锁机制 

update table_name set count = count-1, version=version+1 where good_id=1 and version=1; 

3.业务层使用分布式锁 

获取到锁的必须先判断这个数据是否被处理过 

### 3.唯一性约束  
1.数据库唯一约束   

插入数据，按照唯一索引进行插入，这个机制利用了数据库的主键唯一约束性，解决了insert场景幂等性的问题，但是主键的要求不是自增的主键，就需要业务生成全局唯一的主键。如果是分库分表的场景下，路由规则要保证相同请求下，落地在同一个数据库和同一表中，否则数据库主键约束就不起效果了。

2.redis set防重 

例如可以计算数据的md5放入redis的set，每次处理数据，先看md5是否存在，存在就不处理 

### 4.防重表 

使用订单号orderno作为去重表的唯一索引，把唯一索引插入去重表，在进行业务操作，且他们在同一个事务中。去重表和业务表应该在同一个库中，就保证了能够在同一个事务中 

### 5.全局请求唯一id 

调用接口时，生成一个唯一id，redis将数据保存到集合中，存在即处理过。可以使用nginx设置每一个请求的唯一id 

proxy_set_header X-Request-id $request_id;
