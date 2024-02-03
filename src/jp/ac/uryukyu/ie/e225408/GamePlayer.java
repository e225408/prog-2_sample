package jp.ac.uryukyu.ie.e225408;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * ポーカーゲームのプレイヤーを表現するクラス。
 * プレイヤーの名前、手札、スコア、ステータス、現在のベット額を管理します。
 */
public class GamePlayer {
    /**
     * プレイヤーの名前を表すString。
     */
    private String name;
    /**
     * プレイヤーの手札を表すArrayList。
     */
    private ArrayList<Card> hand;
    /**
     * プレイヤーの手持ちスコアを表すint。
     */
    private int score;
    /**
     * プレイヤーの参加状況を表すboolean。
     */
    private boolean status;

    /**
     * Playerクラスのコンストラクタ。
     * @param _name プレイヤーの名前。
     */    
    public GamePlayer(String _name) {
        this.name = _name;
        this.hand = new ArrayList<Card>();
        this.status = true;

        loadScore();
    }

    /**
     * プレイヤーの名前を返すメソッド。
     * @return プレイヤーの名前。
     */
    public String getName() {
        return this.name;
    }

    /**
     * プレイヤーのステータスを設定するメソッド。
     * @param _status 設定するステータスの値,ブール値
     */    
    public void setStatus(boolean _status) {
        this.status = _status;
    }

    /**
     * プレイヤーのステータスを返すメソッド。
     * @return プレイヤーのステータスの値,ブール値。
     */
    public boolean getStatus() {
        return this.status;
    }

    /**
     * プレイヤーのハンドにカードを追加するメソッド。
     * @param card 追加するカードのオブジェクト。
     */
    public void setHand(Card card){
        hand.add(new Card(card.getSuit(), card.getRank()));
    }

    /**
     * プレイヤーの手札を返すメソッド。
     * @return プレイヤーのハンドを表すArrayList。
     */    
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * プレイヤーの手札を表示するメソッド。
     */
    public void showHand() {
        System.out.println("Your hand is :");
        for (Card card : this.hand) {
            System.out.println(card.getSuit() + " " +card.getRank());
        }
        System.out.println();
    }    
 
    /**
     * プレイヤーのスコアを設定するメソッド。
     * @param _score 設定するスコアの値。
     */    
    public void setScore(int _score) {
        this.score = _score;
    }

    /**
     * プレイヤーのスコアを返すメソッド。
     * @return プレイヤーのスコアの値。
     */    
    public int getScore() {
        return this.score;
    }

    /**
     * プレイヤーのスコアを表示するメソッド。
     */    
    public void showScore() {
        System.out.println(name + "'s current score is :" + this.score + "\n");
    }    

    /**
     * プレイヤーのスコアをファイルから読み込むメソッド。
     */    
    public void loadScore() {
        File file = new File(getPath());
        if (!file.exists()) {
            try {
                file.createNewFile();
                saveScore();
            } catch (IOException e) {
                System.out.println("Error creating new file: " + e.getMessage());
            }
        }

        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextInt()) {
                // scoreが0の時、10000にリセットする。
                this.score = scanner.nextInt();
                if (this.score == 0) {
                    this.score = 10000;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unexpected error: Score file not found after creation: " + e.getMessage());
        }
        saveScore();
    }

    /**
     * プレイヤーのスコアをファイルに保存するメソッド。
     */    
    public void saveScore() {
        try {
            // ファイルの現在の内容を読み込む
            List<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(getPath())));

            // 1行目を上書きする（スコアで）
            if (!lines.isEmpty()) {
                lines.set(0, String.valueOf(this.score));
            } else {
                lines.add(String.valueOf(this.score));
            }

            // 内容を再度ファイルに書き込む
            Files.write(Paths.get(getPath()), lines);
        } catch (IOException e) {

            System.out.println("Unable to save score: " + e.getMessage());
        }
    }

    /**
     * プレイヤーのスコアファイルのパスを返すメソッド。
     * @return プレイヤーのスコアファイルのパス。
     */
    public String getPath() {
        String currentDir = System.getProperty("user.dir");
        String playerScorePath;
       // アプリケーションがJARファイルとして実行されているかどうかを判断
       if (GamePlayer.class.getResource("Player.class").toString().startsWith("jar:")) {
        playerScorePath = currentDir + "/app/src/main/resources/playerScore.txt";
    } else {
        playerScorePath = currentDir + "/src/main/resources/playerScore.txt";
    }
        return playerScorePath;
    }

    /**
     * プレイヤーのデータ（ハンド）をリセットするメソッド。
     */    
    public void resetData() {
        setStatus(true);
        this.hand = new ArrayList<Card>();
    }
}
