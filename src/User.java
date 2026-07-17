public class User {
    private String username;
    private String password;
    private int highScore;
    private int lastLevel;

    //صدا ها
    private boolean bgmOn;
    private boolean sfxShotOn;
    private boolean sfxCrashOn;
    private boolean sfxGameOverOn;

    private String ownedPlane; //برای بخش فروشگاه

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.highScore = 0;
        this.lastLevel = 1;
        this.bgmOn = true;
        this.sfxCrashOn = true;
        this.sfxShotOn = true;
        this.sfxGameOverOn = true;
    }

    public String getUsername() {return username;}

    public String getPassword() {return password;}

    public int getHighScore() {return highScore;}
    public void setHighScore(int highScore){
        this.highScore = highScore; }

    public int getLastLevel() {return lastLevel;}
    public void setLastLevel(int lastLevel){
        this.lastLevel = lastLevel;
    }

    //-------------------------------
    public boolean isBgmOn(){
        return bgmOn;
    }
    public void setBgmOn(boolean bgmOn) {
        this.bgmOn = bgmOn;
    }
    //-------------------------------
    public boolean isSfxShotOn(){
        return sfxShotOn;
    }
    public void setSfxShotOn(boolean sfxShotOn) {
        this.sfxShotOn = sfxShotOn;
    }
    //-------------------------------
    public boolean isSfxCrashOn(){
        return sfxCrashOn;
    }
    public void setSfxCrashOn(boolean sfxCrashOn) {
        this.sfxCrashOn = sfxCrashOn;
    }
    //-------------------------------
    public boolean isSfxGameOverOn() {
        return sfxGameOverOn;
    }
    public void setSfxGameOverOn(boolean sfxGameOverOn) {
        this.sfxGameOverOn = sfxGameOverOn;
    }
    //-------------------------------
    public String getOwnedPlane(){
        return ownedPlane;
    }
    public void setOwnedPlane(String ownedPlane){
        this.ownedPlane = ownedPlane;
    }
}
