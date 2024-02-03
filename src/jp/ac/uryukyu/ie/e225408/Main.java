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
        Player player1 = new Player("Player");
        Bot bot1 = new Bot("Bot");

        GameController gameController = new GameController(player1, bot1);
        gameController.startGame();
    }

}
