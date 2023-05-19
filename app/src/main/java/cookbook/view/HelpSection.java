package cookbook.view;

import java.util.ArrayList;

/**
 * A section of the help page.
 */
public class HelpSection {

  String title;
  ArrayList<HelpSubsection> subsections;

  public HelpSection(String title, ArrayList<HelpSubsection> subsections) {
    this.title = title;
    this.subsections = subsections;
  }

  public String getTitle() {
    return title;
  }

  public ArrayList<HelpSubsection> getSubsections() {
    return subsections;
  }

  /**
   * Get the subsections that contain all the given space separated keywords.
   *
   * @param keywords the keywords to search for
   * @return the subsections containing the keywords in their title and text
   */
  public ArrayList<HelpSubsection> getSubsectionsWithKeywords(String keywords) {
    ArrayList<HelpSubsection> results = new ArrayList<>();
    for (HelpSubsection subsection : subsections) {
      if (subsection.containsAllKeywords(keywords)) {
        results.add(subsection);
      }
    }
    return results;
  }

}
