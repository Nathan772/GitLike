package fr.uge.gitclout.contributions;

import fr.uge.gitclout.app.json.JSONContributions;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;
import java.util.ArrayList;

public class ContributionManager {
  private final Repository repository;

  public ContributionManager(Repository repository) {
    this.repository = repository;
  }

  public JSONContributions getContributions(String tagName) {
    ObjectId commitId = null;
    var result = new ArrayList<ContributionsPerAuthor>();
    try {
      commitId = repository.resolve(tagName);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try (RevWalk revWalk = new RevWalk(repository)) {
      RevCommit commit = revWalk.parseCommit(commitId);
      RevTree tree = commit.getTree();

      try (TreeWalk treeWalk = new TreeWalk(repository)) {
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        while (treeWalk.next()) {
          String path = treeWalk.getPathString();

          BlameCommand blameCommand = new BlameCommand(repository);
          blameCommand.setFilePath(path);
          BlameResult blameResult = blameCommand.call();
          var fileExtension = path.substring(path.lastIndexOf(".") + 1);

          if (blameResult == null || blameResult.getResultContents() == null) {
            continue;
          }

          for (int i = 0; i < blameResult.getResultContents().size(); i++) {
            String author = blameResult.getSourceAuthor(i).getName();
            if (result.stream().noneMatch(contributionsPerAuthor -> contributionsPerAuthor.author().equals(author))) {
              result.add(new ContributionsPerAuthor(author, new ArrayList<>()));
            }

            // check if the language is already in the list
            var contributionsPerAuthor = result.stream().filter(contributionsPerAuthor1 -> contributionsPerAuthor1.author().equals(author)).findFirst().get();
            if (contributionsPerAuthor.contributions().stream().noneMatch(contribution -> contribution.language().equals(fileExtension))) {
              contributionsPerAuthor.contributions().add(new Contribution(fileExtension, 0));
            }

            // increment the count of the language
            var contribution = contributionsPerAuthor.contributions().stream().filter(contribution1 -> contribution1.language().equals(fileExtension)).findFirst().get();
            contribution = new Contribution(contribution.language(), contribution.count() + 1);
            contributionsPerAuthor.contributions().removeIf(contribution1 -> contribution1.language().equals(fileExtension));
            contributionsPerAuthor.contributions().add(contribution);
          }

        }
      } catch (CorruptObjectException e) {
        throw new RuntimeException(e);
      } catch (IncorrectObjectTypeException e) {
        throw new RuntimeException(e);
      } catch (MissingObjectException e) {
        throw new RuntimeException(e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      } catch (GitAPIException e) {
        throw new RuntimeException(e);
      }
    } catch (IncorrectObjectTypeException e) {
      throw new RuntimeException(e);
    } catch (MissingObjectException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new JSONContributions(result);
  }
}
