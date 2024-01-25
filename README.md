

# Endpoint de GET ALL
### ANTES
```
@GetMapping("/products")
public ResponseEntity<List<ProductEntity>>  getAllProducts(){
    return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
}
```
### DEPOIS
```
@GetMapping("/products")
public ResponseEntity<List<ProductEntity>>  getAllProducts(){
List<ProductEntity> productsList = productRepository.findAll();
  if(!productsList.isEmpty()){ 
    for(ProductEntity product : productsList) {
        UUID id = product.getIdProduct();
        product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
    }
  }
    return ResponseEntity.status(HttpStatus.OK).body(productsList); 
}
```
# Enpoint de GET BY ID
### ANTES
```
@GetMapping("/products/{id}")
public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id){
    Optional<ProductEntity> productOne = productRepository.findById(id);
    if(productOne.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
    }
    return ResponseEntity.status(HttpStatus.OK).body(productOne.get());
}
```
### DEPOIS
```
@GetMapping("/products/{id}")
public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id){
    Optional<ProductEntity> productOne = productRepository.findById(id);
    if(productOne.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
    }
    productOne.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Lista de Produtos"));
        return ResponseEntity.status(HttpStatus.OK).body(productOne.get());
}
```
