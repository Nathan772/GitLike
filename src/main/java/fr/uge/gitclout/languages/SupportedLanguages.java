package fr.uge.gitclout.languages;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.gitclout.contribution.ContributionType;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

public class SupportedLanguages {

  private final Map<String, FileTypeInfo> fileTypes;

  public SupportedLanguages() {
    var path = "languages.json";
    ObjectMapper mapper = new ObjectMapper();
    try {
      FileTypes fileTypesData = mapper.readValue(new File(path), FileTypes.class);
      fileTypes = fileTypesData.extensions();
    } catch (IOException e) {
      throw new RuntimeException("Failed to load language types", e);
    }
  }

  public boolean isSupported(String extension) {
    return fileTypes.containsKey(extension);
  }

  public ContributionType getType(String extension) {
    return isSupported(extension) ? fileTypes.get(extension).type() : ContributionType.OTHER;
  }

  public String getLanguage(String extension) {
    return isSupported(extension) ? fileTypes.get(extension).language() : extension;
  }

  public Pattern getCommentRegex(String extension) {
    return isSupported(extension) && fileTypes.get(extension).regexComments() != null
            ? Pattern.compile(fileTypes.get(extension).regexComments())
            : null;
  }

  private record FileTypes(Map<String, FileTypeInfo> extensions) {}

  private record FileTypeInfo(ContributionType type, String language, String regexComments) {}
}
