package jp.ac.uryukyu.ie.e225408;

/**
 * Mainクラスは、ポーカーゲームを開始します。
 * プレイヤーとボットを作成し、ゲームコントローラーを通じてゲームを開始します。
 */
public class Main {
    /**
     * プログラムのエントリーポイントとなるメソッド。
     * @param args コマンドライン引数。
     */
    public static void main(String[] args) {
        GamePlayer player1 = new GamePlayer("Player");
        BotBot bot1 = new BotBot("Bot");

        Game2 gameController = new Game2(player1, bot1);
        gameController.startGame();
    }

}
