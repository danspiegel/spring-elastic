package br.com.elastic.example.elasticapp.controller;

import br.com.elastic.example.elasticapp.domain.Pessoa;
import br.com.elastic.example.elasticapp.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaRepository repository;

    @GetMapping("/{nome}")
    public ResponseEntity<List<Pessoa>> findPessoas(@PathVariable String nome) {
        List<Pessoa> pessoas = repository.findPessoa(nome);
        return ResponseEntity.ok().body(pessoas);
    }

    @PostMapping
    public ResponseEntity<Void> criarPessoa(@RequestBody Pessoa pessoa) {
        repository.savePessoa(pessoa);
        return ResponseEntity.ok().build();
    }

}
