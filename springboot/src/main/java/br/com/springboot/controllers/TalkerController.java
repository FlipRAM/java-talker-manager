package br.com.springboot.controllers;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.models.Talker;
import br.com.springboot.repository.TalkerRepository;

@RestController
@RequestMapping("/talker")
public class TalkerController {
  @Autowired
  private TalkerRepository talkerRepository;

  @GetMapping("")
  public ResponseEntity<List<Talker>> list() {
    return new ResponseEntity<List<Talker>>(this.talkerRepository.findAll(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Talker> getTalkerById(@PathVariable("id") Long id) {
    Optional<Talker> talkerFind = this.talkerRepository.findById(id);

    if(talkerFind.isPresent()) {
      return new ResponseEntity<Talker>(talkerFind.get(), HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @PostMapping("")
  public ResponseEntity<Talker> saveTalker(@RequestBody Talker talker) {

    if(talker.getName() != null && talker.getAge() != null && talker.getWatchedAt() != null && talker.getRate() != null) {
      return new ResponseEntity<Talker>(this.talkerRepository.save(talker), HttpStatus.CREATED);
    }

    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @Transactional
  @PutMapping("/{id}")
  public ResponseEntity<Optional<Talker>> updateTalker(@RequestBody Talker talkerUpdated, @PathVariable("id") Long id) {
    return new ResponseEntity<Optional<Talker>>(
      this.talkerRepository.findById(id).map(talker -> {
        talker.setName(talkerUpdated.getName());
        talker.setAge(talkerUpdated.getAge());
        talker.setWatchedAt(talkerUpdated.getWatchedAt());
        talker.setRate(talkerUpdated.getRate());
        return talkerRepository.save(talker);
      }),
      HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Talker> deleteTalker(@PathVariable("id") Long id) {
    this.talkerRepository.deleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/search")
  public ResponseEntity<List<Talker>> getBySubstringOfName(@RequestParam("q") String q) {
    System.out.println(this.talkerRepository.findByNameContaining(q));
    return new ResponseEntity<List<Talker>>(this.talkerRepository.findByNameContaining("%"+ q + "%"), HttpStatus.OK);
  }
}
