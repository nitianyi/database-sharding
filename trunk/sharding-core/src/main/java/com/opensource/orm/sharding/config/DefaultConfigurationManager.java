/**
 * 
 */
package com.opensource.orm.sharding.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.opensource.orm.sharding.StatementType;
import com.opensource.orm.sharding.config.parser.ConfigParser;
import com.opensource.orm.sharding.config.parser.DataSourceConfigParser;
import com.opensource.orm.sharding.config.parser.TableConfigParser;
import com.opensource.orm.sharding.datasource.DbcpDataSourceFactory;
import com.opensource.orm.sharding.datasource.DelegateDataSourceFactory;
import com.opensource.orm.sharding.factory.DefaultObjectFactory;
import com.opensource.orm.sharding.factory.ObjectFactory;
import com.opensource.orm.sharding.hash.DefaultHashGenerator;
import com.opensource.orm.sharding.hash.HashGenerator;
import com.opensource.orm.sharding.router.DatabaseRouter;
import com.opensource.orm.sharding.router.DefaultDatabaseRouter;
import com.opensource.orm.sharding.utils.xml.DomUtils;

/**
 * @author luolishu
 * 
 */
public class DefaultConfigurationManager implements ConfigurationManager {

	protected static ConfigurationManager instance = null;
	protected static final Map<String, TableConfig> tableConfigs = new HashMap<String, TableConfig>();
	protected static final Map<String, TableDatabaseConfig> tableDatabaseConfigs = new HashMap<String, TableDatabaseConfig>();

	protected static final Map<String, List<TableConfig>> groupTableConfigs = new HashMap<String, List<TableConfig>>();
	protected static final Map<String, DataSourceConfig> dataSourceConfigs = new HashMap<String, DataSourceConfig>();
	protected static final Map<String, DataSource> datasourceMap = new HashMap<String, DataSource>();
	protected static final Map<String, HashGenerator> generatorMap = new HashMap<String, HashGenerator>();
	protected static DatabaseRouter router = new DefaultDatabaseRouter();
	protected static final ConfigParser<DataSourceConfig> dataSourceConfigParser = new DataSourceConfigParser();
	protected ConfigParser<TableConfig> tableConfigParser = new TableConfigParser();
	protected String location = null;
	protected static Random random = new Random();
	protected DelegateDataSourceFactory dataSourceFactory = new DelegateDataSourceFactory();
	protected ObjectFactory objectFactory = new DefaultObjectFactory();
	protected static final Set<String> shardedTables = new LinkedHashSet<String>();
	protected ThreadPoolExecutor threadPoolExecutor;
	static {
		generatorMap.put("default", new DefaultHashGenerator());
	}

	public DefaultConfigurationManager() {
		this(null);
	}

	public DefaultConfigurationManager(String location) {
		this.location = location;
		instance = this;
	}

	public void init() throws FileNotFoundException, Exception {
		dataSourceFactory.addDataSourceFactory(new DbcpDataSourceFactory());
		parseConfiguration(new FileInputStream(location));
		System.out.println("load success!");

	}

	protected void parseConfiguration(InputStream inputStream) throws Exception {
		Document doc = this.parseDocument(inputStream);
		List<Element> elements = DomUtils.getChildElements(doc
				.getDocumentElement());
		for (Element item : elements) {
			if ("datasource".equalsIgnoreCase(item.getNodeName())) {
				DataSourceConfig datasourceConfig = dataSourceConfigParser
						.parse(item);
				String key = datasourceConfig.getId();
				dataSourceConfigs.put(key, datasourceConfig);

			}
			if ("generator".equalsIgnoreCase(item.getNodeName())) {
				this.parseGenerator(item);
			}
		}
		for (Map.Entry<String, DataSourceConfig> entry : dataSourceConfigs
				.entrySet()) {
			DataSourceConfig datasourceConfig = entry.getValue();
			if (datasourceConfig.getParent() != null
					&& datasourceConfig.getParent().trim().length() > 0) {
				DataSourceConfig config = dataSourceConfigs
						.get(datasourceConfig.getParent());
				if (config != null) {
					Properties props = new Properties();
					props.putAll(config.properties);
					props.putAll(datasourceConfig.properties);
					datasourceConfig.setProperties(props);
				} else {
					throw new IllegalArgumentException(
							"configuration Error!parent="
									+ datasourceConfig.getParent());
				}
			}
			DataSource dataSource = dataSourceFactory.create(datasourceConfig);
			if (dataSource == null) {
				continue;
			}
			String key = datasourceConfig.getId();
			datasourceMap.put(key, dataSource);

		}
		for (Element item : elements) {
			if ("group".equalsIgnoreCase(item.getNodeName())) {
				List<Element> groupTables = DomUtils.getChildElements(item);
				String groupId = item.getAttribute("id");
				List<TableConfig> groupList = new LinkedList<TableConfig>();
				groupTableConfigs.put(groupId, groupList);
				for (Element table : groupTables) {
					if ("table".equalsIgnoreCase(table.getNodeName())) {
						TableConfig tableConfig = tableConfigParser
								.parse(table);
						shardedTables.addAll(Arrays.asList(tableConfig
								.getTables()));
						groupList.add(tableConfig);
						tableConfigs.put(tableConfig.getName().toUpperCase(),
								tableConfig);
						for (String shardTable : tableConfig.getTables()) {
							tableConfigs.put(shardTable.toUpperCase(),
									tableConfig);
						}
						for (TableDatabaseConfig database : tableConfig
								.getDatabaseConfigs()) {
							for (String itemTable : database.getTables()) {
								tableDatabaseConfigs.put(itemTable, database);
							}
						}
					}
				}
			}
		}
	}

	protected void parseGenerator(Element e) throws Exception {
		String id = DomUtils.getAttribuite(e, "id");
		String className = DomUtils.getAttribuite(e, "class");
		Class<?> claz = Class.forName(className);
		HashGenerator generator = (HashGenerator) claz.newInstance();
		generatorMap.put(id, generator);
	}

	protected Document parseDocument(InputStream inputStream) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(inputStream);
		return document;
	}

	public static ConfigurationManager getInstance() {
		return instance;
	}

	public DatabaseRouter getRouter() {
		return router;
	}

	public DataSource getShardDataSource(StatementType statementType,
			String shardTableName) {
		TableDatabaseConfig databaseConfig = this
				.getDatabaseConfig(shardTableName);
		String dataSourceId = null;
		switch (statementType) {
		case SELECT:
			if (databaseConfig.isReadwrite()) {
				dataSourceId = databaseConfig.getSlaves()
						.get(random.nextInt(databaseConfig.getSlaves().size()))
						.getDataSourceId();
			} else {

				dataSourceId = databaseConfig
						.getMasters()
						.get(random.nextInt(databaseConfig.getMasters().size()))
						.getDataSourceId();
			}
			break;
		case INSERT:
			dataSourceId = databaseConfig.getMasters()
					.get(random.nextInt(databaseConfig.getMasters().size()))
					.getDataSourceId();
			break;

		case UPDATE:
			dataSourceId = databaseConfig.getMasters()
					.get(random.nextInt(databaseConfig.getMasters().size()))
					.getDataSourceId();
			break;
		case DELETE:
			dataSourceId = databaseConfig.getMasters()
					.get(random.nextInt(databaseConfig.getMasters().size()))
					.getDataSourceId();
			break;

		}
		return datasourceMap.get(dataSourceId);
	}

	public TableConfig getTableConfig(String tableName) {
		return tableConfigs.get(tableName.toUpperCase());
	}

	public TableDatabaseConfig getDatabaseConfig(String tableName) {
		return tableDatabaseConfigs.get(tableName);
	}

	public DataSourceConfig getDataSourceConfig(String id) {
		return dataSourceConfigs.get(id);
	}

	public ObjectFactory getObjectFactory() {
		return objectFactory;
	}

	public Set<String> getShardedTables() {
		return shardedTables;
	}

	@Override
	public DataSource getDataSource(String id) {
		return datasourceMap.get(id);
	}

	@Override
	public ThreadPoolExecutor getQueryThreadPool() {
		return threadPoolExecutor;
	}

	@Override
	public HashGenerator getHashGenerator(String id) {
		return generatorMap.get(id);
	}

	public static void main(String... args) {
		DefaultConfigurationManager configurationManager = new DefaultConfigurationManager(
				"D:/workspace_opensource/business_framework_svn/sharding-common/src/main/resources/sharding.xml");
	}

}
