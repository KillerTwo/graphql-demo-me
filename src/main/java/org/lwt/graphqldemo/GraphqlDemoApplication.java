package org.lwt.graphqldemo;

import java.util.Map;

import org.lwt.graphqldemo.entity.Author;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputFieldsContainer;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLInputType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.servlet.internal.ExecutionResultHandler;

@SpringBootApplication
@RestController
public class GraphqlDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphqlDemoApplication.class, args);
	}
	
	@GetMapping("/hello")
	public String hello(String query) {
		
		GraphQLObjectType authorType = GraphQLObjectType.newObject()
				.name("Author")
				.field(GraphQLFieldDefinition.newFieldDefinition()
						.name("name")
						.type(Scalars.GraphQLString)

						)
				.field(GraphQLFieldDefinition.newFieldDefinition()
						.name("age")
						.type(Scalars.GraphQLInt))
				.field(GraphQLFieldDefinition.newFieldDefinition()
						.name("id")
						.type(Scalars.GraphQLInt))
				.build();
		
		GraphQLInputType userType = GraphQLInputObjectType.newInputObject()
				.name("user")
				.field(GraphQLInputObjectField.newInputObjectField()
						.name("name")
						.type(Scalars.GraphQLString)

						)
				.build();
		
		GraphQLObjectType bookType = GraphQLObjectType.newObject()
				.name("Book")
				.field(GraphQLFieldDefinition.newFieldDefinition()
						.name("name")
						.type(Scalars.GraphQLString)
						)
				.field(GraphQLFieldDefinition.newFieldDefinition()
						.name("publisher")
						.type(Scalars.GraphQLString))
				.field(GraphQLFieldDefinition.newFieldDefinition()
						.name("id")
						.type(Scalars.GraphQLInt))
				.field(GraphQLFieldDefinition.newFieldDefinition()
						.name("author")
						.type(authorType))
				.build();
		
		GraphQLObjectType queryType = GraphQLObjectType.newObject()
				.name("Query")
				.field(GraphQLFieldDefinition.newFieldDefinition()
						.name("book")
						.type(bookType)
						.argument(GraphQLArgument
								.newArgument()
								.name("user")
								.type(userType)
								)
						.dataFetcher(new DataFetcher<Author>() {
							@Override
							public Author get(DataFetchingEnvironment environment) {
								Map<String, Object> user = environment.getArgument("user");
								System.err.println("user: " + user.get("name"));
								Author author = Author.builder()
										.id(1)
										.name("alice")
										.age(22)
										.build();
								return author;
							}
						})
						)
				.build();
		
		GraphQLSchema schema = GraphQLSchema.newSchema().query(queryType).build();
		
		GraphQL graphQL = GraphQL.newGraphQL(schema).build();
		
		ExecutionInput input = ExecutionInput.newExecutionInput().query(query).build();
		
		ExecutionResult result = graphQL.execute(input);
		
		Map<String, Object> map = result.toSpecification();
		
		return JSON.toJSONString(map);
	}
	
	static class User{
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}

}
