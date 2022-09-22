package com.pharmacy.service.service.implementation;

import com.pharmacy.service.dto.CategoryDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.dto.ProductDto;
import com.pharmacy.service.exception.ResourceException;
import com.pharmacy.service.model.Category;
import com.pharmacy.service.model.Product;
import com.pharmacy.service.repository.ProductRepository;
import com.pharmacy.service.service.CategoryService;
import com.pharmacy.service.service.ItemService;
import com.pharmacy.service.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDto getProduct(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty())
            throw new ResourceException("Product", "ID", productId, ResourceException.ErrorType.FOUND);
        Product product = optionalProduct.get();
        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getProducts(Integer pageNumber, Integer pageSize) {
        Pageable requestedPage = PageRequest.of(pageNumber, pageSize);
        Page<Product> productPage = productRepository.findAll(requestedPage);
        List<Product> allProducts = productPage.getContent();
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : allProducts) productDtoList.add(modelMapper.map(product, ProductDto.class));
        PageableResponse<ProductDto> pageableProductResponse = new PageableResponse<>();
        return pageableProductResponse.setResponseData(productDtoList, productPage);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {

        productDto.setProductName(productDto.getProductName().toLowerCase());
        productDto.setProprietaryName(productDto.getProprietaryName().toLowerCase());
        productDto.setDescription(productDto.getDescription().toLowerCase());
        productDto.setDosageForm(productDto.getDosageForm().toLowerCase());

        Set<CategoryDto> categoryDtoSet = productDto.getCategorySet();
        Product product = modelMapper.map(productDto, Product.class);
        Set<Category> categorySet = new HashSet<>();
        for (CategoryDto categoryDto : categoryDtoSet) {
//            categorySet.add(categoryService.getCategoryByName(categoryDto.getCategoryName()));
            categorySet.add(modelMapper.map(categoryDto, Category.class));
        }
        try {
            product.setCategorySet(categorySet);
            return modelMapper.map(productRepository.save(product), ProductDto.class);
        } catch (Exception e) {
            throw new ResourceException("Product", "product", product, ResourceException.ErrorType.CREATED, e);
        }
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<Boolean> createProducts(List<Product> productList) {
        try {
            List<Product> productList1 = productRepository.saveAll(productList);
            while (true) {
                if (productList1.size() == productList.size()) {
                    return CompletableFuture.completedFuture(true);
                }
            }
        } catch (Exception e) {
            throw new ResourceException("Product", "product list", productList, ResourceException.ErrorType.CREATED, e);
        }
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        productDto.setProductName(productDto.getProductName().toLowerCase());
        productDto.setProprietaryName(productDto.getProprietaryName().toLowerCase());
        productDto.setDescription(productDto.getDescription().toLowerCase());
        productDto.setDosageForm(productDto.getDosageForm().toLowerCase());

        Product newProduct = modelMapper.map(productDto, Product.class);
        Optional<Product> optionalProduct = productRepository.findById(newProduct.getProductId());
        if (optionalProduct.isEmpty())
            throw new ResourceException("Product", "product", productDto, ResourceException.ErrorType.FOUND);
        Set<Category> categorySet = new HashSet<>();
        for (Category category : newProduct.getCategorySet()) {
            categorySet.add(categoryService.getCategoryByName(category.getCategoryName()));
        }
        try {
            newProduct.setCategorySet(categorySet);
            return modelMapper.map(productRepository.save(newProduct), ProductDto.class);
        } catch (Exception e) {
            throw new ResourceException("Product", "product", productDto, ResourceException.ErrorType.UPDATED, e);
        }
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<Boolean> updateProducts(List<Product> productList) {
        try {
            productRepository.saveAll(productList);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            throw new ResourceException("Product", "product list", productList, ResourceException.ErrorType.UPDATED, e);
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty())
            throw new ResourceException("Product", "ID", productId, ResourceException.ErrorType.FOUND);
        try {
            productRepository.deleteById(productId);
        } catch (Exception e) {
            throw new ResourceException("Product", "ID", productId, ResourceException.ErrorType.DELETED, e);
        }
    }

    @Override
    public boolean checkIfExists(Long productId) {
        return productRepository.existsById(productId);
    }

    @Override
    public void updateQuantity(Long productId, Long quantity) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty())
            throw new ResourceException("Product", "ID", productId, ResourceException.ErrorType.FOUND);
        Product updatedProduct = optionalProduct.get();
        updatedProduct.setQuantity(updatedProduct.getQuantity() + quantity);
        try {
            log.info(productRepository.save(updatedProduct).toString());
        } catch (Exception e) {
            throw new ResourceException("Product", "product", updatedProduct, ResourceException.ErrorType.UPDATED, e);
        }
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepository.findAllByCategorySetContains(category);
    }

    @Override
    public void deleteByCategory(Category category) {
        List<Product> productList = findByCategory(category);
        Set<Category> categorySet;
        for (Product product : productList) {
            categorySet = product.getCategorySet();
            if (categorySet.size() > 1) {
                categorySet.remove(category);
                product.setCategorySet(categorySet);
                try {
                    productRepository.save(product);
                } catch (Exception e) {
                    throw new ResourceException("Product", "product", product, ResourceException.ErrorType.UPDATED, e);
                }
            } else {
                deleteProduct(product.getProductId());
            }
        }
    }
}
