package com.example.felix.lab_googlefirebaseactivity;

public class Bands {

    private Integer id;

    public Bands(Integer id, String band, String genre, String album, String label) {
        this.id = id;
        this.band = band;
        this.genre = genre;
        this.album = album;
        this.label = label;

    }

    private String band;
    private String genre;
    private String album;
    private String label;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public String getBand() {
        return band;
    }
    public String getGenre() {
        return genre;
    }
    public String getAlbum() {return album;}
    public String getLabel() {return label;}


    @Override
    public String toString() {
        return "Bands{" +
                "id = " + id +
                ", band = '" + band + '\'' +
                ", genre = '" + genre + '\'' +
                ", album = '" + album + '\'' +
                ", label = '" + label + '\'' +
                '}';
    }
}
