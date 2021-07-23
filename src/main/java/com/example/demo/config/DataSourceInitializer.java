package com.example.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

import static java.util.concurrent.TimeUnit.MINUTES;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
public class DataSourceInitializer {

    private static final String persistenceUnitName = "dataSourcePersistenceUnit";
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceInitializer.class);

    private static final int MAXIMUM_POOL_SIZE = 20;
    private static final int MINIMUM_POOL_SIZE = 10;
    private static final long MAX_LIFE_TIME = MINUTES.toMillis(30);
    private static final long MINIMUM_IDLE_TIME = MINUTES.toMillis(5);

    @Value("${mysql.db.dialect}")
    private String dbDialect;
    @Value("${hibernate.format_sql}")
    private String propertyNameHibernateFmtSql;
    @Value("${hibernate.show_sql}")
    private String propertyNameHibernateShowSql;
    @Value("${hibernate.jdbc.batch_size}")
    private String propertyNameHibernateJdbcBatchSize;


    @Bean(name = "datasource", destroyMethod = "close")
   public  DataSource getDataSource() {

        try {
            final String url = "jdbc:mysql://localhost:3306/demo?verifyServerCertificate=false&useSSL=false&rewriteBatchedStatements=true&useCompression=true";
            final HikariDataSource hikariDataSource = DataSourceBuilder
                    .create()
                    .type(HikariDataSource.class)
                    .username("root")
                    .password("asdfjklp1")
                    .url(url)
                    .build();

            hikariDataSource.setMaxLifetime(MAX_LIFE_TIME);
            hikariDataSource.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
            hikariDataSource.setMinimumIdle(MINIMUM_POOL_SIZE);
            hikariDataSource.setIdleTimeout(MINIMUM_IDLE_TIME);
            hikariDataSource.setReadOnly(false);
            return ProxyDataSourceBuilder.create(hikariDataSource).name("Debug-Logger").asJson().countQuery().logQueryToSysOut().build();


        } catch (Exception e) {
            LOGGER.error("Connection to the Database failed", e);
            throw e;
        }
    }

    @Bean(name = "datasourceEntityManager")
    @DependsOn({"datasourcePersistenceUnitManager"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(@Qualifier("datasourcePersistenceUnitManager") DefaultPersistenceUnitManager defaultPersistenceUnitManager,
                                                                           @Qualifier("datasource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdaptor());
        entityManagerFactoryBean.setPersistenceUnitName(persistenceUnitName);
        entityManagerFactoryBean.setPersistenceUnitManager(defaultPersistenceUnitManager);
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setJpaProperties(jpaHibernateProperties());
        return entityManagerFactoryBean;

    }

    @Bean(name = "datasourcePersistenceUnitManager")
    @DependsOn({"datasource"})
    public DefaultPersistenceUnitManager persistenceUnitManager(@Qualifier("datasource") DataSource dataSource) {
        DefaultPersistenceUnitManager defaultPersistenceUnitManager = new DefaultPersistenceUnitManager();
        defaultPersistenceUnitManager.setDefaultPersistenceUnitName(persistenceUnitName);
        defaultPersistenceUnitManager.setDefaultDataSource(dataSource);
        defaultPersistenceUnitManager.setPackagesToScan("com.example.demo");
        return defaultPersistenceUnitManager;
    }

    @Bean(name = "datasourceTransactionManager")
    @DependsOn({"datasourceEntityManager"})
    public JpaTransactionManager jpaTransactionManager(@Qualifier("datasourceEntityManager") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
        return transactionManager;
    }

    private HibernateJpaVendorAdapter vendorAdaptor() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform(dbDialect);
        vendorAdapter.setShowSql(false);
        return vendorAdapter;
    }

    private Properties jpaHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.format_sql", propertyNameHibernateFmtSql);
        properties.put("hibernate.jdbc.batch_size", propertyNameHibernateJdbcBatchSize);
        properties.put("hibernate.show_sql", propertyNameHibernateShowSql);
        return properties;
    }

}
