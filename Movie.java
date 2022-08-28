import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Movie {
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    private LocalDate Release_Date; 
    private String Title;
    private String Overview;
    private Double Popularity;
    private Integer Vote_Count;
    private Double Vote_Average;
    private String Original_Language;
    private String Genre;
    private String Poster_Url;
    
    public Movie(String Release_Date, String Title, String Overview, Double Popularity, Integer Vote_Count,
            Double Vote_Average, String Original_Language, String Genre, String Poster_Url) throws ParseException {
        this.Release_Date = df.parse(Release_Date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.Title = Title;
        this.Overview = Overview;
        this.Popularity = Popularity; 
        this.Vote_Count = Vote_Count;
        this.Vote_Average = Vote_Average;
        this.Original_Language = Original_Language;
        this.Genre = Genre;
        this.Poster_Url = Poster_Url;
    }

    public LocalDate getRelease_Date() {
        return Release_Date;
    }

    public void setRelease_Date(String Release_Date) throws ParseException {
        this.Release_Date = df.parse(Release_Date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getOverview() { 
        return Overview;
    }

    public void setOverview(String Overview) {
        this.Overview = Overview;
    }

    public Double getPopularity() {
        return Popularity;
    }

    public void setPopularity(Double Popularity) {
        this.Popularity = Popularity;
    }

    public Integer getVote_Count() {
        return Vote_Count;
    }

    public void setVote_Count(Integer Vote_Count) {
        this.Vote_Count = Vote_Count;
    }

    public Double getVote_Average() {
        return Vote_Average;
    }

    public void setVote_Average(Double Vote_Average) {
        this.Vote_Average = Vote_Average;
    }

    public String getOriginal_Language() {
        return Original_Language;
    }

    public void setOriginal_Language(String Original_Language) {
        this.Original_Language = Original_Language;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String Genre) {
        this.Genre = Genre;
    }

    public String getPoster_Url() {
        return Poster_Url;
    }

    public void setPoster_Url(String Poster_Url) {
        this.Poster_Url = Poster_Url;
    }

    @Override
    public String toString() {
        return  Release_Date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")) + ", " + Title + 
                ", " + Overview + ", " + Popularity + ", " + Vote_Count + ", " + Vote_Average +
                ", " + Original_Language + ", " + Genre + ", " + Poster_Url;
    }

}
