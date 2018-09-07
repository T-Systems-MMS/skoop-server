//package io.knowledgeassets.myskills.server.report.batch;
//
//import io.knowledgeassets.myskills.server.skill.SkillResponse;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.item.data.Neo4jItemReader;
//import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.batch.JpaBatchConfigurer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableBatchProcessing
//public class BatchConfiguration {
//
//    @Autowired
//    public JobBuilderFactory jobBuilderFactory;
//
//    @Autowired
//    public StepBuilderFactory stepBuilderFactory;
//	Neo4jItemReader
//
//    // tag::readerwriterprocessor[]
//    @Bean
//    public Neo4jItemReader<SkillResponse> reader() {
//        return new FlatFileItemReaderBuilder<Person>()
//            .name("personItemReader")
//            .resource(new ClassPathResource("sample-data.csv"))
//            .delimited()
//            .names(new String[]{"firstName", "lastName"})
//            .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
//                setTargetType(Person.class);
//            }})
//            .build();
//    }
//
//    @Bean
//    public PersonItemProcessor processor() {
//        return new PersonItemProcessor();
//    }
//
//    @Bean
//    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
//        return new JdbcBatchItemWriterBuilder<Person>()
//            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//            .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
//            .dataSource(dataSource)
//            .build();
//    }
//    // end::readerwriterprocessor[]
//
//    // tag::jobstep[]
//    @Bean
//    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
//        return jobBuilderFactory.get("importUserJob")
//            .incrementer(new RunIdIncrementer())
//            .listener(listener)
//            .flow(step1)
//            .end()
//            .build();
//    }
//
//    @Bean
//    public Step step1(JdbcBatchItemWriter<Person> writer) {
//        return stepBuilderFactory.get("step1")
//            .<Person, Person> chunk(10)
//            .reader(reader())
//            .processor(processor())
//            .writer(writer)
//            .build();
//    }
//    // end::jobstep[]
//}
