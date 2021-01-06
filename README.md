# Store
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
