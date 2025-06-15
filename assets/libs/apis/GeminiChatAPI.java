package assets.libs.apis;

import assets.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class GeminiChatAPI {

    private String apiKey;
    private String model;
    private List<ChatMessage> conversationHistory;
    private int maxTokens;
    private double temperature;


    public GeminiChatAPI(String apiKey) {
        this(apiKey, "gemini-2.0-flash-exp");
    }


    public GeminiChatAPI(String apiKey, String model) {
        this.apiKey = apiKey;
        this.model = model;
        this.conversationHistory = new ArrayList<>();
        this.maxTokens = 8192;
        this.temperature = 0.7;
    }

    public ChatResponse sendMessage(String message) {
        return sendMessage(message, false);
    }

    public ChatResponse sendMessage(String message, boolean keepHistory) {
        try {

            if (keepHistory) {
                conversationHistory.add(new ChatMessage("user", message));
            }

            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/"
                    + model + ":generateContent?key=" + apiKey;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);


            String jsonRequest = buildJsonRequest(message, keepHistory);


            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8)) {
                writer.write(jsonRequest);
                writer.flush();
            }


            int responseCode = connection.getResponseCode();
            BufferedReader reader;

            if (responseCode == 200) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            if (responseCode != 200) {
                return new ChatResponse(false, null, "API Error: " + response.toString(), responseCode);
            }


            String content = parseJsonResponse(response.toString());


            if (keepHistory && content != null) {
                conversationHistory.add(new ChatMessage("model", content));
            }

            return new ChatResponse(true, content, null, responseCode);

        } catch (Exception e) {
            return new ChatResponse(false, null, "Requestion failure: " + e.getMessage(), -1);
        }
    }


    private String buildJsonRequest(String message, boolean includeHistory) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"contents\": [\n");


        if (includeHistory && !conversationHistory.isEmpty()) {
            for (int i = 0; i < conversationHistory.size(); i++) {
                ChatMessage msg = conversationHistory.get(i);
                json.append("    {\n");
                json.append("      \"role\": \"").append(msg.getRole()).append("\",\n");
                json.append("      \"parts\": [\n");
                json.append("        {\n");
                json.append("          \"text\": \"").append(escapeJson(msg.getContent())).append("\"\n");
                json.append("        }\n");
                json.append("      ]\n");
                json.append("    }");
                if (i < conversationHistory.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append(",\n");
        }


        json.append("    {\n");
        json.append("      \"role\": \"user\",\n");
        json.append("      \"parts\": [\n");
        json.append("        {\n");
        json.append("          \"text\": \"").append(escapeJson(message)).append("\"\n");
        json.append("        }\n");
        json.append("      ]\n");
        json.append("    }\n");

        json.append("  ],\n");
        json.append("  \"generationConfig\": {\n");
        json.append("    \"temperature\": ").append(temperature).append(",\n");
        json.append("    \"maxOutputTokens\": ").append(maxTokens).append("\n");
        json.append("  }\n");
        json.append("}");

        return json.toString();
    }


    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }


    private String parseJsonResponse(String jsonResponse) {
        try {
            String searchPattern = "\"text\"";
            int textIndex = jsonResponse.indexOf(searchPattern);

            if (textIndex == -1) {
                return "Failed to find result";
            }

            int colonIndex = jsonResponse.indexOf(":", textIndex);
            if (colonIndex == -1) {
                return Main.LT.translateWithArray("Invalid JSON format.",Main.language);
            }

            // 跳過空格和引號
            int startIndex = colonIndex + 1;
            while (startIndex < jsonResponse.length() &&
                    (jsonResponse.charAt(startIndex) == ' ' ||
                            jsonResponse.charAt(startIndex) == '\t' ||
                            jsonResponse.charAt(startIndex) == '\n')) {
                startIndex++;
            }

            if (startIndex >= jsonResponse.length() || jsonResponse.charAt(startIndex) != '"') {
                return "Invalid JSON format";
            }

            startIndex++;

            StringBuilder result = new StringBuilder();
            for (int i = startIndex; i < jsonResponse.length(); i++) {
                char c = jsonResponse.charAt(i);
                if (c == '"' && (i == 0 || jsonResponse.charAt(i-1) != '\\')) {
                    break;
                } else if (c == '\\' && i + 1 < jsonResponse.length()) {
                    char nextChar = jsonResponse.charAt(i + 1);
                    switch (nextChar) {
                        case 'n': result.append('\n'); i++; break;
                        case 't': result.append('\t'); i++; break;
                        case 'r': result.append('\r'); i++; break;
                        case '\\': result.append('\\'); i++; break;
                        case '"': result.append('"'); i++; break;
                        default: result.append(c); break;
                    }
                } else {
                    result.append(c);
                }
            }

            return result.toString();

        } catch (Exception e) {
            return "Failed to parse result: " + e.getMessage();
        }
    }


    public void clearHistory() {
        conversationHistory.clear();
    }


    public List<ChatMessage> getHistory() {
        return new ArrayList<>(conversationHistory);
    }


    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public void setTemperature(double temperature) {
        this.temperature = Math.max(0.0, Math.min(2.0, temperature));
    }

    public void setModel(String model) {
        this.model = model;
    }


    public String getModel() { return model; }
    public int getMaxTokens() { return maxTokens; }
    public double getTemperature() { return temperature; }
}
