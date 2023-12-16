package fr.uge.gitclout.app;

import fr.uge.gitclout.app.json.JSONResponse;
import fr.uge.gitclout.repository.RepositoryManager;
import fr.uge.gitclout.repository.infos.GitRepositoryInfosManager;
import fr.uge.gitclout.repository.infos.RepositoryInfos;
import fr.uge.gitclout.repository.infos.URL;
import fr.uge.gitclout.tag.GitTagManager;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.sse.Event;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller("/api")
public class GitcloutController {

  @Inject
  private RepositoryManager repositoryManager;

  @Post(uri = "/check-repository", produces = "application/json")
  public HttpResponse<JSONResponse> checkRepository(String url) {
    GitRepositoryInfosManager gitRepositoryManager = new GitRepositoryInfosManager(url);
    if (gitRepositoryManager.doesRemoteRepositoryExists()) {
      var message = "The repository at '" + url + "' was found and is accessible.";
      return HttpResponse.ok(new JSONResponse(message, "success", new URL(url)));
    }
    var message = "Unable to find or access the repository at '" + url + "'. Please check the URL for typos or access restrictions.";
    return HttpResponse.notFound(new JSONResponse(message, "error", new URL(url)));
  }

  @Get(uri = "/repository-info", produces = "application/json")
  public HttpResponse<JSONResponse> getRepositoryInfo(String url) {
    GitRepositoryInfosManager gitRepositoryManager = new GitRepositoryInfosManager(url);
    if (!gitRepositoryManager.doesRemoteRepositoryExists()) {
      var message = "Unable to find or access the repository at '" + url + "'. Please check the URL for typos or access restrictions.";
      return HttpResponse.notFound(new JSONResponse(message, "error", new URL(url)));
    }
    try {
      RepositoryInfos repositoryInfos = gitRepositoryManager.getRepositoryInfos();
      return HttpResponse.ok(new JSONResponse("Retrieved information for repository '" + url + "'.", "success", repositoryInfos));
    } catch (URISyntaxException e) {
      var message = "The URL '" + url + "' is incorrectly formatted. Please verify the URL syntax.";
      return HttpResponse.badRequest(new JSONResponse(message, "error", new URL(url)));
    } catch (GitAPIException e) {
      var message = "Failed to access repository information for '" + url + "'. This could be due to network issues, access permissions, or the repository does not exist.";
      return HttpResponse.notFound(new JSONResponse(message, "error", new URL(url)));
    }
  }

  @ExecuteOn(TaskExecutors.IO)
  @Get(uri = "/analyze-tags", produces = MediaType.TEXT_EVENT_STREAM)
  public Publisher<Event<JSONResponse>> analyzeTags(String url) {
    try {
      var path = repositoryManager.resolveRepository(url);
      var gitTagManager = new GitTagManager(path);
      var tags = gitTagManager.getTags();
      return Flux.generate(() -> 0, (i, emitter) -> {
        if (i < tags.size()) {
          emitter.next(
                  Event.of(new JSONResponse("Analyzing tag '" + tags.get(i) + "' for repository '" + url + "'.", "success", new URL(url)))
          );
        } else {
          // TODO: Save when all tags are analyzed
          emitter.complete();
        }
        return ++i;
      });


    } catch (GitAPIException e) {
      var message = "Failed to access repository information for '" + url + "'. This could be due to network issues, access permissions, or the repository does not exist.";
      return Flux.just(Event.of(new JSONResponse(message, "error", new URL(url))));
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

}