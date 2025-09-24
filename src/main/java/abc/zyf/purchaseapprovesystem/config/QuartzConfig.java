package abc.zyf.purchaseapprovesystem.config;

import abc.zyf.purchaseapprovesystem.job.SummuryJob;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableScheduling
public class QuartzConfig {

//    // 自定义一个 Quartz 专用的数据源 也就是Quartz作业记录数据库，一般和业务数据库不是同一个。
//    @Bean
//    public DataSource quartzDataSource() {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl("jdbc:mysql://localhost:3306/quartz?useSSL=false&serverTimezone=Asia/Shanghai");
//        config.setUsername("root");
//        config.setPassword("your_password");
//        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        config.setMaximumPoolSize(10);
//        return new HikariDataSource(config);
//    }

    // 配置 SchedulerFactoryBean 设置Quart作业存储方式为jdbc，而不是内存
    @Bean
    //@DependsOn("quartzDataSource") // 确保数据源先被创建
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        // 使用外部数据源
        factory.setDataSource(dataSource);

        // 指定 Quartz 配置
        Properties props = new Properties();
        props.put("org.quartz.scheduler.instanceName", "MyScheduler");
        props.put("org.quartz.scheduler.instanceId", "AUTO");
        props.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        props.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        props.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        props.put("org.quartz.jobStore.dataSource", "myDS");
        props.put("org.quartz.jobStore.isClustered", "false");
        props.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        props.put("org.quartz.threadPool.threadCount", "10");

        factory.setQuartzProperties(props);
        factory.setOverwriteExistingJobs(true); // 是否覆盖已存在的 job
        factory.setWaitForJobsToCompleteOnShutdown(true); // 关机时等待任务完成

        return factory;
    }

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(SummuryJob.class)
                .withIdentity("summuryJob") // job name
                .storeDurably() // 即使没有触发器关联也不删除该job
                .build();
    }

    //触发器
    @Bean
    public Trigger trigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.dailyAtHourAndMinute(23, 59)
                .withMisfireHandlingInstructionDoNothing();
        return TriggerBuilder.newTrigger()
                .forJob("summuryJob")
                .withIdentity("summuryTrigger") // trigger name
                .withSchedule(scheduleBuilder)
                .build();
    }
}
