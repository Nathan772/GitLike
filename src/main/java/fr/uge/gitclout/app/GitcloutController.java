package fr.uge.gitclout.app;

import fr.uge.gitclout.app.json.JSONData;
import fr.uge.gitclout.app.json.JSONData.JSONRepository;
import fr.uge.gitclout.app.json.JSONData.JSONUrl;
import fr.uge.gitclout.app.json.JSONResponse;
import fr.uge.gitclout.contribution.ContributionsService;
import fr.uge.gitclout.repository.RepositoryManager;
import fr.uge.gitclout.repository.remote.GitRemoteRepositoryManager;
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

import java.io.IOException;
import java.net.URISyntaxException;

@Controller("/api")
public class GitcloutController {

  @Inject
  private RepositoryManager repositoryManager;

  @Post(uri = "/check-repository", produces = "application/json")
  public HttpResponse<JSONResponse> checkRepository(String url) {
    GitRemoteRepositoryManager gitRepositoryManager = new GitRemoteRepositoryManager(url);
    if (gitRepositoryManager.doesRemoteRepositoryExists()) {
      var message = "The repository at '" + url + "' was found and is accessible.";
      return HttpResponse.ok(new JSONResponse(message, "success", new JSONData.JSONUrl(url)));
    }
    var message = "Unable to find or access the repository at '" + url + "'. Please check the URL for typos or access restrictions.";
    return HttpResponse.notFound(new JSONResponse(message, "error", new JSONUrl(url)));
  }

  @Get(uri = "/repository-info", produces = "application/json")
  public HttpResponse<JSONResponse> getRepositoryInfo(String url) {
    GitRemoteRepositoryManager gitRepositoryManager = new GitRemoteRepositoryManager(url);
    if (!gitRepositoryManager.doesRemoteRepositoryExists()) {
      var message = "Unable to find or access the repository at '" + url + "'. Please check the URL for typos or access restrictions.";
      return HttpResponse.notFound(new JSONResponse(message, "error", new JSONUrl(url)));
    }
    try {
      JSONRepository repositoryInfos = gitRepositoryManager.getRepositoryInfos();
      return HttpResponse.ok(new JSONResponse("Retrieved information for repository '" + url + "'.", "success", repositoryInfos));
    } catch (URISyntaxException e) {
      var message = "The URL '" + url + "' is incorrectly formatted. Please verify the URL syntax.";
      return HttpResponse.badRequest(new JSONResponse(message, "error", new JSONUrl(url)));
    } catch (GitAPIException e) {
      var message = "Failed to access repository information for '" + url + "'. This could be due to network issues, access permissions, or the repository does not exist.";
      return HttpResponse.notFound(new JSONResponse(message, "error", new JSONUrl(url)));
    }
  }

  @ExecuteOn(TaskExecutors.IO)
  @Get(uri = "/analyze-tags", produces = MediaType.TEXT_EVENT_STREAM)
  public Publisher<Event<JSONResponse>> analyzeTags(String url) {
    try {
      var repository = repositoryManager.resolveRepository(url);
      var contributionsService = new ContributionsService();
      return contributionsService.getContributions(repository);
    } catch (GitAPIException e) {
      throw new RuntimeException(e);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}