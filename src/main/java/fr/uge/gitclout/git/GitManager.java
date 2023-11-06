package fr.uge.gitclout.git;

import fr.uge.gitclout.model.Repository;
import fr.uge.gitclout.model.Tag;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevTag;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.io.File;

public class GitManager {
  private Git git;
  private final String remoteURL;
  private final String localPath;
  private final String projectName;
  //private final Repository repository;

  public GitManager(String remoteURL, String localPath) throws GitAPIException {
    Objects.requireNonNull(remoteURL);
    Objects.requireNonNull(localPath);
    this.remoteURL = remoteURL;
    this.localPath = localPath;
    this.projectName = remoteURL.substring(remoteURL.lastIndexOf("/") + 1, remoteURL.lastIndexOf("."));
    //this.repository = new Repository(projectName, remoteURL, localPath + "/" + projectName);
  }

  public Repository cloneRepository() throws GitAPIException {
    git = Git.cloneRepository()
            .setURI(remoteURL)
            .setDirectory(new File(localPath + "/" + projectName))
            .call();
    return new Repository(projectName, remoteURL, localPath + "/" + projectName);
  }

  private Date getTagDate(Ref ref) throws IOException {
    return git.getRepository().parseCommit(ref.getObjectId()).getAuthorIdent().getWhen();
  }

  public List<Tag> getTags() throws GitAPIException {
    return git.tagList().call().stream().map(ref -> {
      try {
        return new Tag(ref.getName(), getTagDate(ref), null);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }).toList();
  }

  public void close() {
    git.close();
  }

  public static void main(String[] args) throws GitAPIException, IOException {
    var gitManager = new GitManager("https://github.com/vuejs/core.git", "target/tmp/test");
    gitManager.cloneRepository();
    System.out.println(gitManager.getTags());
    gitManager.close();
  }
}