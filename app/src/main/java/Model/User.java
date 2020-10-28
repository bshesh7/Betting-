package Model;
public class User {

    private String isMother;
    private String name;
    private String email;
    private String username;
    private String bio;
    private String imageurl;
    private String id;
    private String Guessed_date;

    public User() {
    }

    public User(String name, String email, String username, String bio, String imageurl, String id,String isMother,String date) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.bio = bio;
        this.imageurl = imageurl;
        this.id = id;
        this.isMother = isMother;
        this.Guessed_date= date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDate(String date) {
        this.Guessed_date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getId() {
        return id;
    }
    public String getIsMother() {
        return isMother;
    }
    public void setIsMother(String isMother) { this.isMother = isMother;}


    public void setId(String id) {
        this.id = id;
    }
    public String getDate() {
        return Guessed_date;
    }

}
