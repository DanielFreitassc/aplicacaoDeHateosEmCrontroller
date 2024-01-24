package br.com.danielfreitassc.backend.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.checkerframework.checker.units.qual.m;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import br.com.danielfreitassc.backend.dtos.ProductRecordDTO;
import br.com.danielfreitassc.backend.models.ProductEntity;
import br.com.danielfreitassc.backend.repositories.ProductRepository;
import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @PostMapping("/products")
    public ResponseEntity<ProductEntity> saveProduct(@RequestBody @Valid ProductRecordDTO productRecordDTO){
        var productEntity = new ProductEntity();
        BeanUtils.copyProperties(productRecordDTO, productEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productEntity));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductEntity>>  getAllProducts(){
        List<ProductEntity> productsList = productRepository.findAll(); // aqui vai o  productRepository.findAll() que estava em body
        if(!productsList.isEmpty()){ 
            for(ProductEntity product : productsList) {
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productsList); //removi productRepository.findAll(); de dentro do .body 
    }
    
    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id){
        Optional<ProductEntity> productOne = productRepository.findById(id);
        if(productOne.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        productOne.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Lista de Produtos")); // Aqui adicionei está linha para fazer link para a lista de todos os produtos
        return ResponseEntity.status(HttpStatus.OK).body(productOne.get());
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProductRecordDTO productRecordDTO) {
        Optional<ProductEntity> productOne = productRepository.findById(id);
        if(productOne.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não Encontrado");
        }
        var productEntity = productOne.get();
        BeanUtils.copyProperties(productRecordDTO, productEntity);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productEntity));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductEntity> productOne = productRepository.findById(id);
        if(productOne.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrao");
        }
        productRepository.delete(productOne.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto removido com sucesso");
    }
}
