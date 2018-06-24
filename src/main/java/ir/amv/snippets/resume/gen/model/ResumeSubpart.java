package ir.amv.snippets.resume.gen.model;

import java.util.List;

/**
 * @author Amir
 */
public class ResumeSubpart {

    private String title;
    private List<ResumeSubpartItem> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public List<ResumeSubpartItem> getItems() {
        return items;
    }

    public void setItems(final List<ResumeSubpartItem> items) {
        this.items = items;
    }
}
