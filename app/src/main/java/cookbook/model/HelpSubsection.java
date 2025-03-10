package cookbook.model;

/**
 * A subsection of a section in the help page.
 */
public class HelpSubsection {
  int id;
  private String title;
  private String text;

  /**
   * Help Subsection constructor.
   *
   * @param id the id of the subsection
   * @param title the title of the subsection
   * @param text the text of the subsection
   */
  public HelpSubsection(int id, String title, String text) {
    this.id = id;
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
