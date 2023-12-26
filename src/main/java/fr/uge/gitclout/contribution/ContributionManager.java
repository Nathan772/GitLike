/*package fr.uge.gitclout.contribution;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ContributionManager {
  private final Repository repository;
  private final String tagName;
  private final Contributions contributions = new Contributions();
  private final Map<String, ContributionType> languagesTypes = new HashMap<>(); // TODO: move to a better place

  public ContributionManager(Repository repository, String tagName) {
    this.repository = repository;
    this.tagName = tagName;
  }

  private ObjectId resolveTagNameToCommitId() throws IOException {
    return repository.resolve(tagName);
  }


  private RevTree getCommitTree(ObjectId commitId) throws IOException {
    try (RevWalk revWalk = new RevWalk(repository)) {
      RevCommit commit = revWalk.parseCommit(commitId);
      return commit.getTree();
    }
  }

  private BlameResult executeBlameCommand(String path) throws GitAPIException {
    BlameCommand blameCommand = new BlameCommand(repository);
    blameCommand.setFilePath(path);
    return blameCommand.call();
  }

  private String getFileExtension(String path) {
    return path.substring(path.lastIndexOf(".") + 1);
  }

  private void updateAuthorContributions(String author, String fileExtension) {
    ContributionType type = languagesTypes.getOrDefault(fileExtension, ContributionType.OTHER);
    ContributionInfos contributionInfos = contributions.getContributions()
            .computeIfAbsent(author, k -> new HashMap<>())
            .computeIfAbsent(type, k -> new ContributionInfos());

    LanguageCount languageCount = contributionInfos.getLanguages()
            .computeIfAbsent(fileExtension, k -> new LanguageCount(fileExtension, 0, 0));

    languageCount.incrementCount();
    contributionInfos.addTotalCount(1);
  }


  private void updateContributions(BlameResult blameResult, int lineIndex, String fileExtension) {
    String author = blameResult.getSourceAuthor(lineIndex).getName();
    updateAuthorContributions(author, fileExtension);
  }


  private void processBlameResult(BlameResult blameResult, String path) {
    String fileExtension = getFileExtension(path);

    for (int i = 0; i < blameResult.getResultContents().size(); i++) {
      updateContributions(blameResult, i, fileExtension);
    }
  }


  private void processFile(TreeWalk treeWalk) throws IOException, GitAPIException {
    String path = treeWalk.getPathString();
    BlameResult blameResult = executeBlameCommand(path);
    if (blameResult == null || blameResult.getResultContents() == null) {
      return;
    }
    processBlameResult(blameResult, path);
  }


  private void analyzeContributions(RevTree tree) throws IOException, GitAPIException {
    try (TreeWalk treeWalk = new TreeWalk(repository)) {
      treeWalk.addTree(tree);
      treeWalk.setRecursive(true);
      while (treeWalk.next()) {
        processFile(treeWalk);
      }
    }
  }

  private void loadLanguageTypes() {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      Map<String, Object> map = objectMapper.readValue(new File("languages.json"),
              new TypeReference<Map<String, Object>>() {
              });

      for (Map.Entry<String, Object> entry : map.entrySet()) {
        String key = entry.getKey();
        String type = (String) ((Map<?, ?>) entry.getValue()).get("type");
        ContributionType contributionType = ContributionType.valueOf(type);
        languagesTypes.put(key, contributionType);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to load language types", e);
    }
  }


  public Contributions getContributions() {
    loadLanguageTypes();
    try {
      ObjectId commitId = resolveTagNameToCommitId();
      System.out.println(commitId);
      RevTree tree = getCommitTree(commitId);
      analyzeContributions(tree);
    } catch (IOException | GitAPIException e) {
      throw new RuntimeException(e);
    }
    System.out.println(tagName);
    System.out.println(contributions);
    return contributions;
  }

}
*/