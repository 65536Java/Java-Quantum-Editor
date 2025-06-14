package assets.libs;

import assets.Main;

public class Settings {
    private static String language;
    public Settings(String language){
        setLanguage(language);
    }


    public void setLanguage(String language){
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }


}
