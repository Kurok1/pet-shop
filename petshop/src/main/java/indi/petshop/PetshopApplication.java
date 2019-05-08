package indi.petshop;

import org.elasticsearch.node.NodeBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@SpringBootApplication
@EnableElasticsearchRepositories
public class PetshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetshopApplication.class, args);
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
