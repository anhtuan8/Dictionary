import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class appController implements Initializable {
    Dictionary dict = new Dictionary();

    @FXML
    private ListView<Word> wordList = new ListView<Word>(dict.dictionary);
    @FXML
    WebView webView = new WebView();
    @FXML
    TextField search = new TextField();
    @FXML
    Button searchButton = new Button();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //TODO: Adding word target to wordList from file
//        loadData("ev");
        loadData("ev");
//        DictionaryManagement.insertFromFile();
        wordList.getItems().addAll(dict.dictionary);

    }

    @FXML
    public void handle(MouseEvent event) {
        WebEngine engine = webView.getEngine();
        String s = wordList.getSelectionModel().getSelectedItems().get(0).getWord_explain();
        engine.loadContent(s);
    }

    @FXML
    public void keyhandle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            WebEngine engine = webView.getEngine();
            engine.loadContent(search.getText());
            searchWord(search.getText());
        }
    }

    @FXML
    public void startSearch(ActionEvent event) {
        searchButton.setFocusTraversable(false);
        String target = search.getText();
        searchWord(target);
    }

    public void loadData(String language) {
        DictionaryManagement.dictsize = 0;
        BufferedReader br = null;
        try {
            if (language.equalsIgnoreCase("ev")) {
                br = new BufferedReader(new FileReader("C:\\Users\\OS\\Desktop\\E_V.txt"));
            }
            if (language.equalsIgnoreCase("custom")) {
                br = new BufferedReader(new FileReader("C:\\Users\\OS\\Desktop\\dictionary.txt"));
            }
            br.readLine();
            String s = br.readLine();
            while (s != null) {
                Word word = new Word();
                word.setWord_target(s.split("<html>")[0]);
                word.setWord_explain("<html>" + s.split("<html>")[1]);
                dict.dictionary.add(word);
                s = br.readLine();
                DictionaryManagement.dictsize++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Binary search
    public void searchWord(String target) {
        WebEngine engine = webView.getEngine();
        int n = DictionaryManagement.dictsize;
        int left = 0, right = n-1;
        while(left <=right){
            int mid = (left + right) / 2;
            if(dict.dictionary.get(mid).getWord_target().equalsIgnoreCase(target)){
                String s = dict.dictionary.get(mid).getWord_explain();
                engine.loadContent(s);
                return;
            }
            if(dict.dictionary.get(mid).getWord_target().compareToIgnoreCase(target) > 0){
                right = mid -1;
            }
            if(dict.dictionary.get(mid).getWord_target().compareToIgnoreCase(target) < 0){
                left = mid + 1;
            }
        }
        engine.loadContent("Not found.");

    }

}