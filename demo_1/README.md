# Demo_1

### 1、继承 FireStreaming 实现 handle 方法

```scala
    object ReadKafkaDemo extends FireStreaming {
      /**
        * 处理函数
        *
        * @param ssc
        */
      override def handle(ssc: StreamingContext): Unit = {
    
        val source = new KafkaDirectSource[String, String](ssc)
    
        val logs = source.getDStream[String](_.value())
    
        logs.foreachRDD((rdd, time) => {
          rdd.take(10).foreach(println)
          source.updateOffsets(time.milliseconds)
        })
    
    
      }
    }
```

### 2、生成默认配置文件

```shell
    
    bash create_default_conf.sh > demo_1/deploy/conf/test/readKafkaDemo.properties
    
```

### 3、配置文件中补全必要配置

```properties
#必须设置,执行class的全包名称
spark.run.main=fire.spark.demo.ReadKafkaDemo

#必须设置,包含main class的jar包
#jar文件必须包含在lib.path当中
spark.run.main.jar=demo_1-1.0-SNAPSHOT.jar


#用户代码依赖jar包的所在目录
#可以是绝对路径,也可以是相对此配置文件的相对路径,相对路径会自动补全
spark.run.lib.path=../../lib

#kafka集群的主机和端口号,可以配置多个,每个主机之间用逗号[,]隔开
#default=
spark.source.kafka.consume.bootstrap.servers=hadoop102:9092

```
### 4、本地运行测试

```properties

配置 VM options -Dspark.properties.file=/Users/guoning/IdeaProjects/Git-Project/fire-spark-demo/demo_1/deploy/conf/online/readKafkaDemo.properties

直接运行 ReadKafkaDemo

```

### 5、打包部署

```shell
#进入要打包的工程根目录  
cd demo_1
# 打包，如果需要将第三方依赖包夜打入gz包，则加参数 -Pwithjar
mvn clean package -Pwithjar
# 将升的gz包demo_1/target/demo_1-1.0-SNAPSHOT.tar.gz 上传到部署机，解压
# 在部署及上执行名，启动App
bash run.sh conf/online/readKafkaDemo.properties
```