package fr.uge.gitclout.app;

import fr.uge.gitclout.git.GitManager;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@Controller("/api")
public class GitcloutController {
  @Inject
  private ApplicationUtils applicationUtils;

  @Post(uri = "/repository", produces = "application/json")
  public Mono<Map<String, Object>> postRepository(String url) {
    return Mono.fromCallable(() -> {
      var repo = applicationUtils.processRepository(url);
      return Map.of("data", Map.of("url", url, "repo", repo), "message", "Repository " + url + "saved", "status", "ok");
    }).subscribeOn(Schedulers.boundedElastic());
  }

  @Get(uri = "/{repositoryURL}/tags", produces = "application/json")
  public Mono<Map<String, Object>> getTags(String repositoryURL) {
    return Mono.fromCallable(() -> {
      var repo = applicationUtils.processRepository(repositoryURL);
      var tags = applicationUtils.processTags(repo);
      return Map.of("data", tags, "message", "Tags found for repository " + repositoryURL, "status", "ok");
    }).subscribeOn(Schedulers.boundedElastic());
  }

  @Get(uri = "/tags/{repositoryId}", produces = "application/json")
  public Mono<Map<String, Object>> getTagsById(long repositoryId) {
    return Mono.fromCallable(() -> {
      var repo = applicationUtils.getRepositoryById(repositoryId);
      var tags = applicationUtils.processTags(repo);
      return Map.of("data", Map.of("tags", tags, "repo", repo), "message", "Tags found for repository " + repo.getURL(), "status", "ok");
    }).subscribeOn(Schedulers.boundedElastic());
  }

  @Get(uri = "/{repositoryURL}/contributions", produces = "application/json")
  public Mono<Map<String, Object>> getContributions(String repositoryURL) {
    return Mono.fromCallable(() -> {
      var repo = applicationUtils.processRepository(repositoryURL);
      var url = repo.getURL();
      var git = new GitManager(url);
      var contributions = git.parseEveryFileFromCurrentRepoAndTransformItIntoContribution();
      return Map.of("data", contributions, "message", "Contributions found for repository " + repositoryURL, "status", "ok");
    }).subscribeOn(Schedulers.boundedElastic());
  }
}