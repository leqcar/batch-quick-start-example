package quickstart;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class Application {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = new org.springframework.context.annotation.AnnotationConfigApplicationContext(
				BatchConfiguration.class);
		/*
		ConfigurableApplicationContext ctx = new org.springframework.context.support.ClassPathXmlApplicationContext(
				"classpath:/quickstart/app-config.xml");*/
		
		try {
			JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
			Job job = ctx.getBean("importPeopleJob", Job.class);

			/* JobExecution execution = */ jobLauncher.run(job, new JobParameters());

			List<Person> results = ctx.getBean(JdbcTemplate.class).query(
					"SELECT first_name, last_name FROM people", new RowMapper<Person>() {
						@Override
						public Person mapRow(ResultSet rs, int row) throws SQLException {
							return new Person(rs.getString(1), rs.getString(2));
						}
					});

			for (Person person : results) {
				System.out.println("Found <" + person + "> in the database.");
			}
		} finally {
			ctx.close();
		}
	}

}
