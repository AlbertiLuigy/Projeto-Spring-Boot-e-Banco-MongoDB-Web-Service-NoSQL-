package com.project.workshopmongo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.workshopmongo.domain.Post;

import com.project.workshopmongo.dto.PostDTO;

import com.project.workshopmongo.repository.PostRepository;
import com.project.workshopmongo.services.exception.ObjectNotFoundException;

@Service 
public class PostService {
    
    @Autowired 
    private PostRepository repo;

    public List<Post> findAll(){ // Buscar todos os posts
        return repo.findAll();
    }

    public Post findById(String id){ // Buscar post por ID
        Optional<Post> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado")); 
    }

    public Post insert(Post obj){ // Inserir um novo post
        return repo.insert(obj);
    }

    public Post update(Post obj){ // Atualizar post por ID
        Post newObj = findById(obj.getId());
        updateData(newObj, obj);
        return repo.save(newObj); 
    }
    
    private void updateData(Post newObj, Post obj) { // Atualizar dados do post
		newObj.setAuthor(obj.getAuthor());
		newObj.setBody(obj.getBody());
        newObj.setTitle(obj.getTitle());
        newObj.setDate(obj.getDate());
	}

    public void delete(String id){ // Excluir post por ID
        findById(id);
        repo.deleteById(id);
    }   

    public Post fromDTO(PostDTO objDTO){ // Converter DTO para entidade Post
        return new Post(objDTO.getId(), objDTO.getDate(), objDTO.getTitle(), objDTO.getBody(), objDTO.getAuthor());
    }
}
