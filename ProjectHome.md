QQ交流群74914061

**前言：**<br />
> 在互联网技术开发中 ，大多数都是基于数据库的开发，而随着应用维护的历史，数据量越来越大， <br />
以及业务变得更加复杂，要求更高的响应时间，和更高的计算能力，单台服务器，单表单库的方式存在着比较大的瓶颈。通过加机器，提升硬件的方式成本越来越高。 <br />
随着数据量和访问量的增长，业内已经有各种方式的解决方案，对数据进行水平和垂直方面的切分。<br />

根据以往分库分表的一些经验，特意写了这套框架，实现对分库分表的透明性，降低开发难度和复杂 度，提高对分库分表的监控调试和方便维护。 <br />

分库分表方面上最主要关注的是如何切分，根据哪些字段进行切分的，这个需要具体的业务使用场景进行分析。 <br />

**根据以往使用的经验来看：**<br />
1. 按照哈希的方式<br />
> 优点：数据散列比较平均，单库单表访问压力分布比较均匀<br />
> 缺点：预先需要做好容量规划，扩容方面不容易，需要重新进行数据hash迁移<br />
> 适用场景：数据变更频繁，并发访问量比较高的情况下，可以利用多台机器将访问压力均匀分布，合理利用资源<br />
2. 按时间段或者按ID段进行划分<br />
> 优点：可以持续扩容，降低单表压力<br />
> 缺点：有时候业务数据上，存在着一部分的旧数据，但是这部分数据很少使用到，基于这样的分库分表的方式，不能充分的利用好系统资源。<br />
> 使用场景：数据归档或者备份，或者数据不存在短期内国企的情况下的场景。<br />

更多的请大家补充................<br />

以上的功能，这套框架都能够很好的支持，<br />
一套基于数据库分库分表的解决方案，当前版本仅支持mybatis，设计上往后可以支持Jdbc方面的扩展，开发的目的是方便在日常开发中对分库分表透明，使用上管理方便，简化开发难度。<br />

**目前具有的分库分表功能包括：**<br />

1.分库分表路由（目前完成水平切分功能，后续提供垂直切分支持）<br />

2.读写分离<br />

3.跨库跨表事务支持（特殊场景下可以使用，尽量避免使用）<br />

4.查询结果合并<br />

5.分库表直接查询<br />



**后续会支持以下功能：**<br />

1.对多个库读写的负载均衡<br />

2.failover<br />

3.提供垂直切分功能<br />

4.流量统计功能<br />

5.性能监控功能<br />

6.配置集成管理功能<br />

7.数据复制同步<br />

......<br />



### 与Spring集成： ###
```
<bean id="sqlSessionFactory"
		class="com.opensource.mybatis.spring.ShardingSqlSessionFactoryBean">
		<property name="typeAliasesPackage" value="com.opensource.mybatis.sharding" />
		<property name="dataSource" ref="dataSource1" />
		<property name="plugins" ref="plugins" />
		<property name="shardingConfigLocation" value="classpath*:sharding.xml" />
	</bean>


	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<property name="basePackage" value="com.opensource.mybatis.sharding" />
	</bean>
```

### 分库分表配置 ###
```
<?xml version="1.0" encoding="UTF-8"?>
<configurations>
	<datasource id="springconfig" ref="springdatasource-config" />
	<generator id="customGenerator" class="com.test.CustomGenerator" />
	<datasource id="parent" abstract="true">
		<url>jdbc:mysql://localhost:3306/sharding?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull
		</url>
		<username>root</username>
		<password>root</password>
		<minIdle>5</minIdle>
		<maxIdle>5</maxIdle>
		<initialSize>5</initialSize>
		<maxWait>30000</maxWait>
		<maxActive>128</maxActive>
		<testOnBorrow>true</testOnBorrow>
		<testWhileIdle>true</testWhileIdle>
		<validationQuery>select 1</validationQuery>
		<driverClassName>com.mysql.jdbc.Driver</driverClassName>
	</datasource>
	<datasource id="master1" parent="parent">
		<url>jdbc:mysql://localhost:3306/sharding?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull
		</url>		 
	</datasource>
	<datasource id="slave1" parent="parent">
		<url>jdbc:mysql://localhost:3306/sharding?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull
		</url> 
	</datasource>
	<datasource id="master2" parent="parent">
		<url>jdbc:mysql://localhost:3306/sharding2?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull
		</url> 
	</datasource>
	<datasource id="slave2" parent="parent">
		<url>jdbc:mysql://localhost:3306/sharding?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull
		</url> 
	</datasource>
	<datasource id="master3" parent="parent">
		<url>jdbc:mysql://localhost:3306/sharding?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull
		</url> 
	</datasource>
	<datasource id="slave3" parent="parent">
		<url>jdbc:mysql://localhost:3306/sharding?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull
		</url> 
	</datasource>
	<datasource id="slave4" parent="parent">
		<url>jdbc:mysql://localhost:3306/sharding?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull
		</url>
	</datasource>
	<group id="trade">
		<table name="user_copy" length="16">
			<database-config>
				<database masters="master1" slaves="slave1">
					<tables range="1-8" />
				</database>
				<database masters="master2" slaves="slave2,slave3">
					<tables range="9-16" />
				</database>
			</database-config>
			<hashcolumns generator="default">
				<column>id</column>
				<script></script>
			</hashcolumns>
		</table>
		<table name="sample_table" length="16"><!-- <names> <name>table1</name> 
				<name>table2</name> <value>table3,table4,table5,table6,table7,table8,table9,table10</value> 
				</names> -->
			<database-config>
				<database masters="master1" slaves="slave1">
					<tables range="1-8" />
				</database>
				<database masters="master2" slaves="slave2,slave3"
					readwrite="true">
					<tables>
						<names>
							<name>table1</name>
							<name>table2</name>
							<value>table3,table4,table5,table6</value>
						</names>
					</tables>
				</database>
				<database masters="master3" slaves="slave3:80,slave4:10"
					readwrite="true">
					<tables>
						<names>
							<name>table1</name>
							<name>table2</name>
							<value>table3,table4,table5,table6</value>
						</names>
					</tables>
				</database>
			</database-config>
			<hashcolumns generator="default">
				<column>member_id</column>
				<script></script>
			</hashcolumns>
		</table>
	</group>

	<group id="member">

	</group>
	<group id="market">

	</group>
</configurations>
```

QQ讨论群74914061