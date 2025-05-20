package assets;

public class l {
    public static int getlangnum(String langcod,boolean dothrow) {
        if (Main.lang.equals("zh")){
            return 0;
        } else{
            return 1;
        }
    }
    public String[][] ls = new String[][]{
            new String[]{
                    "此程式需要使用JDK執行",
                    "編譯",
                    "執行",
                    "儲存",
                    "加載",
                    "設定...",
                    "基本",
                    "已套用變更",
                    "連結",
                    "作者的Youtube頻道",
                    "此程式嘗試在您的瀏覽器打開一個網頁(%s)，您要允許此操作嗎?",
                    "停止"
            },
            new String[]{
                    "JDK is required.",
                    "Compile",
                    "Run",
                    "Save",
                    "Load",
                    "Settings...",
                    "Basic",
                    "Changes was applied.",
                    "Links",
                    "Author's Youtube channel",
                    "This app is try to open a website in your browser(%s), are you accept the action?",
                    "Stop"
            }
    };
}
