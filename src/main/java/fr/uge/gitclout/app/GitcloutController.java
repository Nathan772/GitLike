package fr.uge.gitclout.app;

import fr.uge.gitclout.db.RepositoryRepository;
import fr.uge.gitclout.git.GitManager;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import org.eclipse.jgit.api.errors.GitAPIException;
import reactor.core.publisher.Mono;

@Controller("/api")
public class GitcloutController {

  //private final DatabaseManager databaseManager = new DatabaseManager();

  @Inject
  private RepositoryRepository repositoryRepository;

  @Post(uri = "/post-url", produces = "application/json")
  public Mono<String> postURL(String url) {
    try {
      var git = new GitManager(url, "target/tmp");
      var repo = git.cloneRepository();
      repositoryRepository.save(repo);
//      databaseManager.saveRepository(repo);
      return Mono.just(url);
    } catch (GitAPIException e) {
      return Mono.just("Error");
    }
  }

//  @Post(uri = "/post-url", produces = "application/json")
//  public Mono<String> postURL(String url) {
//    return Mono.fromCallable(() -> {
//      var git = new GitManager(url, "target/tmp");
//      var repo = git.cloneRepository();
//      databaseManager.saveRepository(repo);
//      return url;
//    }).subscribeOn(Schedulers.boundedElastic());
//  }
//
//  @Post(uri = "/post-url2", produces = "application/json")
//  public HttpResponse<String> postURL2(String url) {
//    try {
//      var git = new GitManager(url, "target/tmp");
//      var repo = git.cloneRepository();
//      repositoryRepository.save(repo);
//      return HttpResponse.ok(url);
//    } catch (GitAPIException e) {
//      return HttpResponse.serverError("Error");
//    }
//  }
}