package com.jdd.partition.generate;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * <p>
 * 测试生成代码
 * </p>
 *
 */
public class MysqlGenerator {
	public static final String BASE_PACKAGE = "com.jdd.partition";//生成代码所在的基础包名称，可根据自己公司的项目修改（注意：这个配置修改之后需要手工修改src目录项目默认的包路径，使其保持一致，不然会找不到类）
    private static final String PROJECT_PATH = System.getProperty("user.dir");//项目在硬盘上的基础路径
    public static void main(String[] args) {
//    	generateCode("b2c_epay_partner_messagesend","b2c_epay_partner_protocolquery","b2c_epay_partner_register","b2c_pass_free_payment_query_messagesend","b2c_pass_free_payment_sign");
//    	generateCode("etc_user_apply_log","etc_user_img_log","etc_user_order_log","etc_user_sign_ret_log");
    	generateCode("partition_packet_cash");
	}
    
    public static void generateCode(String... tableNames) {
        String packageName = BASE_PACKAGE;
        boolean serviceNameStartWithI = false;//user -> UserService, 设置成true: user -> IUserService
        generateByTables(serviceNameStartWithI, packageName, tableNames);
    }

    private static void generateByTables(boolean serviceNameStartWithI, String packageName, String... tableNames) {
        GlobalConfig config = new GlobalConfig();
        String dbUrl = "jdbc:mysql://192.168.2.153:3306/jdd-partition";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig
                .setUrl(dbUrl)
                .setUsername("root")
                .setPassword("kdd@2016")
                .setDriverName("com.mysql.cj.jdbc.Driver");
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
        		.setRestControllerStyle(true)
                .setCapitalMode(true)
                .setEntityLombokModel(false)
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组
        config.setActiveRecord(false).setOpen(false).setSwagger2(true)
                .setAuthor("pengbaoning")
                .setOutputDir(PROJECT_PATH+"/src/main/java")
                .setEnableCache(false)// XML 二级缓存
                .setBaseResultMap(true)// XML ResultMap
                .setFileOverride(true);
        if (!serviceNameStartWithI) {
            config.setServiceName("%sService");
        }
        AutoGenerator mpg = new AutoGenerator()
        		.setTemplateEngine(new FreemarkerTemplateEngine())
        		.setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setController("app/rest")
                                .setEntity("entity")
                                .setService("service")
                                .setServiceImpl("service.impl")
                );
        mpg.setTemplate(new TemplateConfig().setController("/generator/template/controller.java"));
        mpg.execute();
    }

    private void generateByTables(String packageName, String... tableNames) {
        generateByTables(true, packageName, tableNames);
    }
}