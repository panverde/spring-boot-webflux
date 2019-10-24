package com.joaquin.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.joaquin.webflux.app.models.dao.ProductoDao;
import com.joaquin.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductoDao dao;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		mongoTemplate.dropCollection("productos")
		.subscribe();
		
		Flux.just(new Producto("TV Panasonic", 435.54),
				new Producto("sony camara",177.89),
				new Producto("sony notebook",323.89),
				new Producto("mica comodad",132.89),
				new Producto("apple ipod",437.9))
				.flatMap(producto->
					{
						producto.setCreateAt(new Date());
						return dao.save(producto);
					}
					)
				.subscribe(producto->log.info("insert: "+ producto.getId()+" "+producto.getNombre()));
		
		
	}

}
