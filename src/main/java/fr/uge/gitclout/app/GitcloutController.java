package fr.uge.gitclout.app;

import fr.uge.gitclout.GitManager.GitManager;
import fr.uge.gitclout.app.json.*;
import fr.uge.gitclout.contributions.ContributionManager;
import fr.uge.gitclout.repository.RepositoryManager;
import fr.uge.gitclout.repository.infos.GitRepositoryInfosManager;
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
import java.util.Objects;

import static fr.uge.gitclout.Contribution.ContributionIntermediate.fromUserContributionToContributionIntermediate;

@Controller("/api")
public class GitcloutController {

  @Inject
  private RepositoryManager repositoryManager;
  private GitManager gitManager;

  private String projectURL;

  @Post(uri = "/check-repository", produces = "application/json")
  public HttpResponse<JSONResponse> checkRepository(String url) {
    GitRepositoryInfosManager gitRepositoryManager = new GitRepositoryInfosManager(url);
    if (gitRepositoryManager.doesRemoteRepositoryExists()) {
      var message = "The repository at '" + url + "' was found and is accessible.";
      //retrieve the url for later usage
      projectURL = url;
      return HttpResponse.ok(new JSONResponse(message, "success", new JSONUrl(url)));
    }
    var message = "Unable to find or access the repository at '" + url + "'. Please check the URL for typos or access restrictions.";
    return HttpResponse.notFound(new JSONResponse(message, "error", new JSONUrl(url)));
  }

  @Get(uri = "/repository-info", produces = "application/json")
  public HttpResponse<JSONResponse> getRepositoryInfo(String url) {
    GitRepositoryInfosManager gitRepositoryManager = new GitRepositoryInfosManager(url);
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
/* à supprimer
  export const getRepoInfos = async (repoURL: string) => {
    const response = await fetch(`${API_URL}/repository-info?url=${repoURL}`);
    console.log(response);
    console.log("blablabla viki blabla")
    return response.json();
  }

  export const getTagContributors = async (TagName: string) => {
    const response = await fetch(`${API_URL}/repository/tag/Contributors-info?url=${TagName}`);
    console.log(response);
    console.log("blablabla viki blabla")
    return response.json();
  }*/

  /**
   * This method enables to retrieve for one tag its contributors name.
   * @param tagName
   * the tag from the project you want to parse
   * @return
   * A response that contains the contributors name.
   */
  @Get(uri = "Contributors-fromTag", produces = "application/json")
  public HttpResponse<JSONResponse> getContributorsFromTagName(String tagName) {
    Objects.requireNonNull(tagName);
      try {
          gitManager = new GitManager(projectURL);
      } catch (IOException | GitAPIException e) {
        System.out.println("failure for loading gitManager data");
          throw new RuntimeException(e);
      }
      try {
      JSONContributors tagContributorsNames = gitManager.getContributorFromTag(tagName);
      return HttpResponse.ok(new JSONResponse("Retrieved information for tag '" + tagName + "'.", "success", tagContributorsNames));
    } catch (URISyntaxException e) {
      var message = "The TAG '" + tagName + "' is incorrectly formatted. Please verify the URL syntax.";
      return HttpResponse.badRequest(new JSONResponse(message, "error", new JSONUrl(tagName)));
    } catch (GitAPIException e) {
      var message = "Failed to access tag informations for '" + tagName + "'. This could be due to network issues, access permissions, or the Tag does not exist.";
      return HttpResponse.notFound(new JSONResponse(message, "error", new JSONUrl(tagName)));
    }
  }



  /**
   * This method enables to retrieve for one tag its contributors name.
   * @param tagName
   * the tag from the project you want to parse
   * @param contributorName
   * the name of the contributor for whom you want to search data.
   * @param contributorEmail
   * the mail of the contributor for whom you want to search data.
   * @return
   * A response that contains the contributors name.
   */
  @Get(uri = "Contributor-InfosFromTag", produces = "application/json")
  public HttpResponse<JSONResponse> getContributorContributionsFromTagName(String tagName, String contributorName, String contributorEmail) {
    Objects.requireNonNull(tagName); Objects.requireNonNull(contributorEmail); Objects.requireNonNull(contributorName);
    var contrib= gitManager.getContributionByUserAndTag(tagName, contributorName,contributorEmail);
      //System.out.println(" les contributions avant transformation "+contrib);
      var contributionFromUserForTag = fromUserContributionToContributionIntermediate(contrib);
      //System.out.println("on vérifie que les contributions associées au user ne sont pas vides, le contenu : "+contributionFromUserForTag);
      return HttpResponse.ok(new JSONResponse("Retrieved information for tag '" + tagName + "'.", "success",
              new JSONContribution(contributionFromUserForTag, contributorEmail,contributorName, tagName)));
  }














  @ExecuteOn(TaskExecutors.IO)
  @Get(uri = "/analyze-tags", produces = MediaType.TEXT_EVENT_STREAM)
  public Publisher<Event<JSONResponse>> analyzeTags(String url) {
    try {
      var repository = repositoryManager.resolveRepository(url);
      var gitTagManager = new GitTagManager(repository);
      var tags = gitTagManager.getTags();
      return Flux.generate(() -> 0, (i, emitter) -> {
        if (i < tags.size()) {
          var tag = tags.get(i);
          var contributionManager = new ContributionManager(repository, tag.getName());
          var contributions = contributionManager.getContributions();
          emitter.next(
                  Event.of(
                          new JSONResponse(
                                  "Analyzed tag '" + tag.getName() + "' for repository '" + url + "'.",
                                  "success",
                                  new JSONContributions(contributions.getContributions())
                          )
                  )
          );
        } else {
          // TODO: Save when all tags are analyzed
          emitter.complete();
        }
        return ++i;
      });


    } catch (GitAPIException e) {
      var message = "Failed to access repository information for '" + url + "'. This could be due to network issues, access permissions, or the repository does not exist.";
      return Flux.just(Event.of(new JSONResponse(message, "error", new JSONUrl(url))));
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

}