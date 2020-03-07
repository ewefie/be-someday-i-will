package pl.fairit.somedayiwill.googlebooksapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumeInfo {
    private String title;
    private String publisher;
    private String publishedDate;
    private String[] authors;
    private String description;
    private int pageCount;
    private int printType;
    private String[] categories;
    private Map<String, String> imageLinks = new HashMap<String, String>();
    private String language;
    private String previewLink;
}