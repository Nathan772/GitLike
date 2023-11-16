package fr.uge.gitclout.git;

import fr.uge.gitclout.model.Repository;
import fr.uge.gitclout.model.Tag;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GitManager {
  private Git git;
  private final String remoteURL;
  private final Path localPath;
  private final String projectName;
  private final Repository repository;

  private static final String PATH = "target/tmp";

  public GitManager(String remoteURL, String localPath) throws IOException {
    Objects.requireNonNull(remoteURL);
    Objects.requireNonNull(localPath);
    this.remoteURL = remoteURL;
    this.projectName = createProjectName(remoteURL);
    this.localPath = Path.of(localPath);
    if (new File(localPath).exists()) {
      git = Git.open(new File(this.localPath.toString()));
    }
    this.repository = new Repository(projectName, remoteURL, this.localPath.toString());
  }

  public GitManager(String remoteURL) throws IOException {
    this(remoteURL, Path.of(PATH).resolve(createProjectName(remoteURL)).toString());
  }

  private static String createProjectName(String remoteURL) {
    if (remoteURL.endsWith(".git")) {
      return remoteURL.substring(remoteURL.lastIndexOf("/") + 1, remoteURL.lastIndexOf("."));
    }
    return remoteURL.substring(remoteURL.lastIndexOf("/") + 1);
  }

  public static GitManager fromRepository(Repository repository) throws IOException {
    return new GitManager(repository.getURL(), repository.getLocalPath());
  }

  public boolean isCloned() {
    return new File(localPath.toString()).exists();
  }

  public Repository cloneRepository() throws GitAPIException {
    git = Git.cloneRepository()
            .setURI(remoteURL)
            .setDirectory(new File(localPath.toString()))
            .call();
    return repository;
  }

  private Date getTagDate(Ref ref) throws IOException {
    Objects.requireNonNull(ref);
    return git.getRepository().parseCommit(ref.getObjectId()).getAuthorIdent().getWhen();
  }

  public List<Tag> getTags() throws GitAPIException {
    return git.tagList().call().stream().map(ref -> {
      try {
        var name = ref.getName().substring(ref.getName().lastIndexOf("/") + 1);
        return new Tag(name, ref.getName(), getTagDate(ref), repository);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }).toList();
  }

  public void close() {
    git.close();
  }

  public static void main(String[] args) throws IOException, GitAPIException {
    var git = new GitManager("https://gitlab.ow2.org/asm/asm.git", "target/tmp");
    System.out.println(git.getTags());
    git.close();
  }
}