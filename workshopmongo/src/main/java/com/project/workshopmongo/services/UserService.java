package com.project.workshopmongo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.workshopmongo.domain.User;
import com.project.workshopmongo.dto.UserDTO;
import com.project.workshopmongo.repository.UserRepository;
import com.project.workshopmongo.services.exception.ObjectNotFoundException;

@Service 
public class UserService {
    
    @Autowired 
    private UserRepository repo;

    public List<User> findAll(){ //findAll() é um método que retorna uma lista de todos os usuários do banco de dados. Ele utiliza o repositório UserRepository para buscar todos os usuários e retorná-los como uma lista.
        return repo.findAll();
    }

    public User findById(String id){
        Optional<User> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado")); 
    }

    public User insert(User obj){ //função insert() é um método que insere um novo usuário no banco de dados. Ele recebe um objeto User como parâmetro e utiliza o repositório UserRepository para salvar o usuário no banco de dados.
        return repo.insert(obj);
    }

    public void delete(String id){ //função delete() é um método que exclui um usuário do banco de dados. Ele recebe um objeto User como parâmetro e utiliza o repositório UserRepository para excluir o usuário do banco de dados.
        findById(id);
        repo.deleteById(id);
    }   

    public User fromDTO(UserDTO objdto){
            return new User(objdto.getId(), objdto.getName(), objdto.getEmail());
    }
}
