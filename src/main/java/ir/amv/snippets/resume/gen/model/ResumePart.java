package ir.amv.snippets.resume.gen.model;

import java.util.List;

/**
 * @author Amir
 */
public class ResumePart {

    private String title;
    private String text;
    private List<ResumeSubpart> subparts;

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public List<ResumeSubpart> getSubparts() {
        return subparts;
    }

    public void setSubparts(final List<ResumeSubpart> subparts) {
        this.subparts = subparts;
    }
}
