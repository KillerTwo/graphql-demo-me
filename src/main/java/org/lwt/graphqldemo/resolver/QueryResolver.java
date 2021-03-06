package org.lwt.graphqldemo.resolver;

import java.util.ArrayList;
import java.util.List;

import org.lwt.graphqldemo.entity.Author;
import org.lwt.graphqldemo.entity.Book;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

@Component
public class QueryResolver implements GraphQLQueryResolver {
	public List<Book> findBooks() {
        Author author = Author.builder()
                .id(1)
                .name("Bill Venners")
                .age(40)
                .build();
        Book b = Book.builder()
                .id(1)
                .name("scala编程第三版")
                .author(author)
                .publisher("电子工业出版社")
                .build();
        List<Book> bookList = new ArrayList<Book>();
        bookList.add(b);
        return bookList;
}
}
