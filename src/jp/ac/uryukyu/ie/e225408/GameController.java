package jp.ac.uryukyu.ie.e225408;

import java.util.Scanner;

/**
 * ポーカーゲーム全体の制御を担当するクラス。
 * ゲーム、プレイヤー、ボットのクラスを管理します。
 */
public class GameController {
    /**
     * Gameクラスのオブジェクト。
     */
    private Game game;
    /**
     * Playerクラスのオブジェクト。
     */
    private Player player1;
    /**
     * Botクラスのオブジェクト。
     */
    private Bot bot1;

    /**
     * GameControllerクラスのコンストラクタ。
     * @param player Playerクラスのオブジェクト。
     * @param bot Botクラスのオブジェクト。
     */    
    public GameController(Player player, Bot bot) {
        this.player1 = player;
        this.bot1 = bot;
        this.game = new Game(player1, bot1, this);
    }

    /**
     * ゲームを開始するメソッド。
     * プレイヤーとボットに手札を配り、各ターンでアクションを実行し、勝者まで判定します。
     */    
    public void startGame() {
        game.addHand(player1);
        game.addHand(bot1);

        for (int i = 0; i < 4; i++) {
            player1.showHand();
            game.playerAction();
            game.checkWinner();
            game.botAction();
            game.checkWinner();

            if (i == 0) {
                game.frop();
            } else if(i < 3){
                game.turnAndRiver();
            }
            if (i < 3) {
                game.showCommunityCard();
            } else {
                System.out.println("--------------------------------");
                System.out.println("Showdown:\n");
            }
        }

        System.out.println("Player's hand is :" + HandEvaluator.evaluateHand(player1, game));
        System.out.println("Bot's hand is :" + HandEvaluator.evaluateHand(bot1, game) + "\n");
        System.out.println(game.determineWinner() + "\n");
        player1.showScore();
        bot1.showScore();
        player1.saveScore();
        bot1.saveScore();

        continueAction();
    }

    /**
     * ゲームをリセットするメソッド。
     * プレイヤーとボットのデータをリセットし、新しいゲームを開始します。
     */
    public void resetGame() {
        player1.resetData();
        bot1.resetData();
        game = new Game(player1, bot1, this);

    }

    /**
     * プレイヤーにゲームを続けるか終了するかを選択するメソッド。
     */
    public void continueAction() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("CONTINUE?\n1. Continue\n2. Exit from the game\n");
            int action = scanner.nextInt();

            if (action == 1) {
                System.out.println("Continue! Let's go next game!\n----------------------------------------------------------------\n");
                resetGame();
                startGame();
            }else if (action == 2) {
                System.out.println("Game Over! Thank you for playing.");
                System.exit(0);  // ゲームを終了
            }else {
                System.err.println("Invalid action. Choose a number between 1 or 2.\n");
                continueAction();
            }
        }

    }

}