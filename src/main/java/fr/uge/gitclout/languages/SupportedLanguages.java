package fr.uge.gitclout.languages;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.uge.gitclout.contribution.ContributionType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Helper class to read the languages.json file
 */
public class SupportedLanguages {
  /**
   * Record to read "filetypes" object from languages.json
   *
   * @param filetypes
   */
  private record FileTypes(Map<String, FileTypeInfo> filetypes) {
  }

  /**
   * Record to read information about a filetype from languages.json
   *
   * @param type
   * @param language
   * @param regexComments
   */
  private record FileTypeInfo(ContributionType type, String language, String regexComments) {
  }

  private final Map<String, FileTypeInfo> fileTypes;


  public SupportedLanguages() {
    ObjectMapper mapper = new ObjectMapper();
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("languages.json")) {
      if (inputStream == null) {
        throw new RuntimeException("languages.json not found");
      }
      FileTypes fileTypesData = mapper.readValue(inputStream, FileTypes.class);
      fileTypes = fileTypesData.filetypes();
    } catch (IOException e) {
      throw new RuntimeException("Failed to load language types", e);
    }
  }

  /**
   * Returns the file type of file
   *
   * @param path path of the file
   * @return the file type
   */
  private String fileType(String path) {
    var file = path.split("/");
    var fileName = file[file.length - 1];
    if (fileTypes.containsKey(fileName)) {
      return fileName;
    }
    int lastIndexOfDot = fileName.lastIndexOf(".");
    if (lastIndexOfDot > 0) {
      return fileName.substring(lastIndexOfDot);
    }
    return "";
  }

  /**
   * Returns true if the file is supported
   *
   * @param path path of the file
   * @return true if the file is supported
   */
  public boolean isSupported(String path) {
    var extension = fileType(path);
    return fileTypes.containsKey(extension);
  }

  /**
   * Returns the type of the file
   *
   * @param path path of the file
   * @return the type of the file
   */
  public ContributionType getType(String path) {
    var extension = fileType(path);
    return isSupported(extension) ? fileTypes.get(extension).type() : ContributionType.OTHER;
  }

  /**
   * Returns the language of the file
   *
   * @param path path of the file
   * @return the language of the file
   */
  public String getLanguage(String path) {
    var extension = fileType(path);
    return isSupported(extension) ? fileTypes.get(extension).language() : extension;
  }

  /**
   * Returns the regex to match comments in the file
   *
   * @param path path of the file
   * @return the regex to match comments in the file
   */
  public Pattern getCommentRegex(String path) {
    var extension = fileType(path);
    return isSupported(extension) && fileTypes.get(extension).regexComments() != null
            ? Pattern.compile(fileTypes.get(extension).regexComments())
            : null;
  }
}
