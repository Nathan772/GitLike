package fr.uge.gitclout.contributions;

import java.util.HashMap;
import java.util.Map;

public class Contributions{

  private final Map<String, Map<ContributionType, ContributionInfos>> contributions = new HashMap<>();
  public void addAuthor(String author) {
    contributions.put(author, new HashMap<>());
  }

  public void addContribution(String author, ContributionType type, ContributionInfos infos) {
    contributions.get(author).put(type, infos);
  }

  public ContributionInfos getContributionInfos(String author, ContributionType type) {
    return contributions.get(author).get(type);
  }

  public Map<String, Map<ContributionType, ContributionInfos>> getContributions() {
    return contributions;
  }
}
