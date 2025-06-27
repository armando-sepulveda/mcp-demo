package mx.regional.next.mcp_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import mx.regional.next.mcp_demo.config.McpConfiguration;

@SpringBootApplication
@Import(McpConfiguration.class)
public class McpDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpDemoApplication.class, args);
	}

}
