package indi.pet.producer;

import org.elasticsearch.node.NodeBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@SpringBootApplication
@EnableElasticsearchRepositories
@EnableSwagger2
public class ProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate(){
		NodeBuilder nodeBuilder=nodeBuilder();
		nodeBuilder.settings().put("path.home",System.getenv("ES_HOME"));
		return new ElasticsearchTemplate(nodeBuilder.local(false).node().client());
	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter(){
		return new ServerEndpointExporter();
	}
}
