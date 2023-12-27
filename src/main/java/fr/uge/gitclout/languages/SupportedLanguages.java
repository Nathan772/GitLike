package fr.uge.gitclout.languages;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.gitclout.contribution.ContributionType;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SupportedLanguages {

  private Map<String, SupportedFiles> fileTypes;
  //private ArrayList<SupportedFiles> fileTypes;

  public SupportedLanguages() {
    var path = "languages.json";
    ObjectMapper mapper = new ObjectMapper();
    try {
      FileTypes fileTypesData = mapper.readValue(new File(path), FileTypes.class);
      fileTypes = fileTypesData.getExtensions();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * precise if an extension is supported or not
   * @param extension
   * the extension you want to test
   * @return
   * true if the extension is supported, false otherwise.
   */
  public boolean isSupported(String extension) {
    return fileTypes.containsKey(extension);
  }

  public ContributionType getType(String extension) {
    if (!isSupported(extension)) {
      return ContributionType.OTHER;
    }
    return fileTypes.get(extension).getType();
  }

  /*
   * enables to retrieve a File based on its extension
   * @param extension
   * return the File the extension represents

  public Optional<SupportedFiles> getByExtension(String extension){
    for(var document:fileTypes){
      if(document.extension().equals(extension)){
        return Optional.of(document);
      }
    }
   return Optional.empty();
  }*/

  /**
   * this method enables to retrieve a language name based on their extension
   * @param extension
   * the extension of the language
   * @return
   * the language with this extension
   */
  public String getLanguage(String extension) {
    Objects.requireNonNull(extension);
    if (!isSupported(extension)) {
      return extension;
    }
    return fileTypes.get(extension).name();
  }

  public Map<String,SupportedFiles>  getFileTypes() {
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
