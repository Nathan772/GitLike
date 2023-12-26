package fr.uge.gitclout.languages;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.gitclout.contribution.ContributionType;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class SupportedLanguages {

  private Map<String, FileTypeInfo> fileTypes;

  public SupportedLanguages() {
    var path = "languages.json";
    ObjectMapper mapper = new ObjectMapper();
    try {
      FileTypes fileTypesData = mapper.readValue(
              new File(path),
              FileTypes.class
      );
      fileTypes = fileTypesData.getExtensions();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean isSupported(String extension) {
    return fileTypes.containsKey(extension);
  }

  public ContributionType getType(String extension) {
    if (!isSupported(extension)) {
      return ContributionType.OTHER;
    }
    return fileTypes.get(extension).getType();
  }

  public String getLanguage(String extension) {
    if (!isSupported(extension)) {
      return extension;
    }
    return fileTypes.get(extension).getLanguage();
  }

  public Map<String, FileTypeInfo> getFileTypes() {
    return fileTypes;
  }

  public static class FileTypes {

    private Map<String, FileTypeInfo> extensions;

    public Map<String, FileTypeInfo> getExtensions() {
      return extensions;
    }


    public void setExtensions(Map<String, FileTypeInfo> extensions) {
      this.extensions = extensions;
    }
  }

  public static class FileTypeInfo {

    private ContributionType type;
    private String language;

    public ContributionType getType() {
      return type;
    }

    public void setType(ContributionType type) {
      this.type = type;
    }

    public String getLanguage() {
      return language;
    }

    public void setLanguage(String language) {
      this.language = language;
    }

    @Override
    public String toString() {
      return "Type: " + type + ", Language: " + language;
    }
  }
}
