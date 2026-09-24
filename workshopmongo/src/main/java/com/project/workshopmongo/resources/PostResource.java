package com.project.workshopmongo.resources;


import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.workshopmongo.domain.Post;
import com.project.workshopmongo.dto.PostDTO;
import com.project.workshopmongo.services.PostService;


@RestController 
@RequestMapping(value = "/posts")
public class PostResource {
    
    @Autowired 
    private PostService service;

    @GetMapping 
    public ResponseEntity<List<PostDTO>> findAll(){ // Buscar todos os posts
        List<Post> list = service.findAll();
        List<PostDTO> listDto = list.stream().map(x -> new PostDTO(x)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @GetMapping(value = "/{id}") // Buscar post por ID
    public ResponseEntity<PostDTO> findById(@PathVariable String id){
        Post obj = service.findById(id);
        return ResponseEntity.ok().body(new PostDTO(obj));
    }   

    @PutMapping (value="/{id}")
 	public ResponseEntity<Void> update(@RequestBody PostDTO objDto, @PathVariable String id) {
		Post obj = service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}

    @DeleteMapping(value = "/{id}") // Excluir post por ID
    public ResponseEntity<Void> delete(@PathVariable String id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }  
    
    @PostMapping // Inserir post
    public ResponseEntity<Void> insert(@RequestBody PostDTO objDTO){
        Post obj = service.fromDTO(objDTO);
        obj = service.insert(obj);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }  
}
