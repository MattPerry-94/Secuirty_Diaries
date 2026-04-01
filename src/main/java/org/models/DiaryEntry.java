package org.models;

public class DiaryEntry {
    private String id;
    private String type;
    private String title;
    private String begin;
    private String end;
    private String content;

    public DiaryEntry(String id, String type, String titre, String dateDebut, String dateFin, String content){
        this.id = id;
        this.type = type;
        this.title = titre;
        this.begin = dateDebut;
        this.end = dateFin;
        this.content = content;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public String getTitle() { return title; }   // ✅ correspond à "title"
    public String getBegin() { return begin; }   // ✅ correspond à "begin"
    public String getEnd() { return end; }       // ✅ correspond à "end"
    public String getContent() { return content; }
}
