package cookbook.view;

/**
 * A subsection of a section in the help page.
 */
public class HelpSubsection {

  private String title;
  private String text;

  public HelpSubsection(String title, String text) {
    this.title = title;
    this.text = text;
  }

  public String getTitle() {
    return title;
  }

  public String getText() {
    return text;
  }

  /**
   * Check if all the given space separated keywords can be found in the subsection.
   *
   * @param keywords the keywords to search for
   * @return true if all the keywords exist in the subsection, otherwise false
   */
  public boolean containsAllKeywords(String keywords) {
    String[] searchKeywords = keywords.split(" ");
    for (String keyword : searchKeywords) {
      if (!getTitle().toLowerCase().contains(keyword.toLowerCase())
          && !getText().toLowerCase().contains(keyword.toLowerCase())) {
        return false;
      }
    }
    return true;
  }

}
