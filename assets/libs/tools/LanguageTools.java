package assets.libs.tools;

public class LanguageTools {
    private static int lindex(String lang) {
        try {
            if(lang.contains("繁體中文")){
                return 0;
            }else if (lang.contains("English")){
                return 1;
            }
        }catch (Exception e){
            if("繁體中文".equals(lang)){
                return 0;
            }else if ("English".equals(lang)){
                return 1;
            }
        }
        return 1;
    }
    public String translateWithArray(String s, String language){
        int lindex = lindex(language);
        try {
            return ls[lindex][ArrayTools.search(ls[1], s)];
        }catch (Throwable throwable){
            throwable.fillInStackTrace();
        }
        return s;
    }
    private static final String[][] ls = new String[][]{
            new String[]{
                    "此程式需要使用JDK執行",
                    "編譯",
                    "執行",
                    "儲存",
                    "加載",
                    "選項...",
                    "清除紀錄",
                    "基本",
                    "連結",
                    "已套用變更",
                    "作者的Youtube頻道",
                    "此程式嘗試在您的瀏覽器打開一個網頁(%s)，您要允許此操作嗎?",
                    "停止",
                    "檢查更新",
                    "字形:",
                    "語言:",
                    "JSON格式錯誤",
                    "清除對話歷史",
                    "傳送訊息"
            },
            new String[]{
                    "JDK is required.",
                    "Compile",
                    "Run",
                    "Save",
                    "Load",
                    "Settings...",
                    "Clear history",
                    "Basic",
                    "Links",
                    "Changes was applied.",
                    "Author's Youtube channel",
                    "This app is try to open a website in your browser(%s), are you accept the action?",
                    "Stop",
                    "Check for updates",
                    "Font:",
                    "Languages",
                    "Invalid JSON format.",
                    "Clear history",
                    "Send message"
            }
    };
}
