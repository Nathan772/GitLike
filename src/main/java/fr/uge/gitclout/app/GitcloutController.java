package fr.uge.gitclout.app;

import fr.uge.gitclout.app.json.JSONData;
import fr.uge.gitclout.app.json.JSONData.JSONRepository;
import fr.uge.gitclout.app.json.JSONData.JSONUrl;
import fr.uge.gitclout.app.json.JSONResponse;
import fr.uge.gitclout.contribution.ContributionsService;
import fr.uge.gitclout.contribution.db.ContributionDetailRepository;
import fr.uge.gitclout.contribution.db.ContributionRepository;
import fr.uge.gitclout.repository.RepositoryManager;
import fr.uge.gitclout.repository.db.RepositoryRepository;
import fr.uge.gitclout.repository.remote.GitRemoteRepositoryManager;
import fr.uge.gitclout.tag.db.Tag;
import fr.uge.gitclout.tag.db.TagRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.sse.Event;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.reactivestreams.Publisher;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * The controller for the Gitclout application.
 */
@Controller("/api")
public class GitcloutController {

  @Inject
  private RepositoryManager repositoryManager;

  @Inject
  private TagRepository tagRepository;
  @Inject
  private RepositoryRepository repositoryRepository;
  @Inject
  private ContributionRepository contributionRepository;
  @Inject
  private ContributionDetailRepository contributionDetailRepository;

  /**
   * Checks if a remote repository exists and is accessible.
   *
   * @param url The URL of the remote repository.
   * @return A JSON response containing the URL of the remote repository.
   */
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

  /**
   * Retrieves information about a remote repository remotely.
   *
   * @param url The URL of the remote repository.
   * @return A JSON response containing information about the remote repository.
   */
  @Get(uri = "/repository-info", produces = "application/json")
  public HttpResponse<JSONResponse> getRepositoryInfo(String url) {
    GitRemoteRepositoryManager gitRepositoryManager = new GitRemoteRepositoryManager(url);
    try {
      JSONRepository repositoryInfos = gitRepositoryManager.getRepositoryInfos();
      return HttpResponse.ok(new JSONResponse("Retrieved information for repository '" + url + "'.", "success", repositoryInfos));
    } catch (GitAPIException | URISyntaxException e) {
      var message = "Failed to access repository information for '" + url + "'. This could be due to network issues, access permissions, or the repository does not exist.";
      return HttpResponse.notFound(new JSONResponse(message, "error", new JSONUrl(url)));
    }
  }

  /**
   * Analyzes the tags of a remote repository. This method creates an SSE stream of JSON responses containing information about the tags of the remote repository.
   *
   * @param url The URL of the remote repository.
   * @return A Flux of JSON responses containing information about the tags of the remote repository.
   */
  @ExecuteOn(TaskExecutors.IO)
  @Get(uri = "/analyze-tags", produces = MediaType.TEXT_EVENT_STREAM)
  public Publisher<Event<JSONResponse>> analyzeTags(String url) {
    try {
      var repository = repositoryManager.resolveRepository(url);
      var contributionsService = new ContributionsService(tagRepository, repositoryRepository, contributionRepository, contributionDetailRepository);
      return contributionsService.getContributions(repository);
    } catch (GitAPIException | URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves the history of analyzed repositories.
   *
   * @return A JSON response containing the history of analyzed repositories.
   */
  @Get(uri = "/history", produces = "application/json")
  public HttpResponse<JSONResponse> getHistory() {
    var repositories = repositoryRepository.findAll();
    var jsonRepositories = repositories.stream()
            .map(repository -> new JSONRepository(repository.getRepositoryName(), repository.getRepositoryURL(),
                    tagRepository.findByRepositoryRepositoryURL(repository.getRepositoryURL()).stream().map(Tag::getTagName).toList()))
            .toList();
    return HttpResponse.ok(new JSONResponse("Retrieved history.", "success", new JSONData.JSONHistory(jsonRepositories)));
  }

  /**
   * Deletes a repository from the history of analyzed repositories.
   *
   * @param url The URL of the remote repository.
   * @return A JSON response containing the URL of the deleted repository.
   */
  @Transactional
  @Delete(uri = "/history", produces = "application/json")
  public HttpResponse<JSONResponse> deleteHistory(String url) {
    var repository = repositoryRepository.findByRepositoryURL(url);
    if (repository.isEmpty()) {
      var message = "Unable to find repository '" + url + "' in history.";
      return HttpResponse.notFound(new JSONResponse(message, "error", new JSONUrl(url)));
    }
    repositoryRepository.delete(repository.get());
    return HttpResponse.ok(new JSONResponse("Deleted repository '" + url + "' from history.", "success", new JSONUrl(url)));
  }
}