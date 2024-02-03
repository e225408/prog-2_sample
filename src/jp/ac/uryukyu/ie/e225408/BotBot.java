package jp.ac.uryukyu.ie.e225408;

/**
 * ポーカーゲームのボットプレイヤーを表現するクラス。
 * Playerクラスを継承しています。
 */
public class BotBot extends GamePlayer{
    
    /**
     * Botクラスのコンストラクタ。
     * @param name ボットプレイヤーの名前。
     */    
    public BotBot(String name) {
        super(name);
    }

    /**
     * ボットプレイヤーのスコアファイルのパスを返すメソッド。
     * @return ボットプレイヤーのスコアファイルのパス。
     */    
    @Override
    public String getPath() {
        String currentDir = System.getProperty("user.dir");
        String botScorePath;
       // アプリケーションがJARファイルとして実行されているかどうかを判断
       if (BotBot.class.getResource("Bot.class").toString().startsWith("jar:")) {
        botScorePath = currentDir + "/app/src/main/resources/botScore.txt";
    } else {
        botScorePath = currentDir + "/src/main/resources/botScore.txt";
    }
        return botScorePath;
    }
}
