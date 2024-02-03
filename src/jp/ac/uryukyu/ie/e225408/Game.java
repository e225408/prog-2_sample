package jp.ac.uryukyu.ie.e225408;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * ポーカーゲームに必要なアクションを制御するクラス。
 * デッキ、コミュニティカード、ポット、現在のターンポット、プレイヤー、ボット、ゲームコントローラーを管理する。
 */
public class Game {
    /**
     * Deckクラスのオブジェクト。
     */
    private Deck deck;
    /**
     * ポーカーで場に出るコミュニティカードのArrayList。
     */
    private ArrayList<Card> communityCard;
    /**
     * 賭けた合計Scoreを表すint。
     */
    private int pot;
    /**
     * そのターンにおけるScoreを表すint。
     */
    private int currentTurnPot;
    /**
     * Playerクラスのオブジェクト。
     */
    private Player player;
    /**
     * Botクラスのオブジェクト。
     */
    private Bot bot;
    /**
     * GameControllerクラスのオブジェクト。
     */
    private GameController gameController;

    /**
     * Gameクラスのコンストラクタ。
     * デッキを生成し、シャッフルする。
     * @param _player Playerクラスのオブジェクト。
     * @param _bot　Botクラスのオブジェクト。
     * @param _gameController　GameControllerクラスのオブジェクト。
     */
    public Game(Player _player, Bot _bot, GameController _gameController) {
        this.deck = new Deck();
        this.communityCard = new ArrayList<Card>();
        this.gameController = _gameController;
        this.player = _player;
        this.bot = _bot;

        deck.shuffle();
    }

    /**
     * プレイヤーに手札を追加するメソッド。
     * @param player プレイヤーのオブジェクト。
     */
    public void addHand(Player player) {
        player.setHand(deck.deal());
        player.setHand(deck.deal());
    }

    /**
     * コミュニティカードを返すメソッド。
     * @return コミュニティカードのArrayList。
     */
    public ArrayList<Card> getCommunityCard() {
        return communityCard;
    }    

    /**
     * コミュニティカードを表示するメソッド。
     */    
    public void showCommunityCard() {
        System.out.println("--------------------------------");
        System.out.println("Now Community card is : (Now pot is " + pot + ".)");
        for (Card card : communityCard) {
            System.out.println(card.getSuit() + " " + card.getRank());
        }
        System.out.println();
    }

    /**
     * フロップ（最初の3枚のコミュニティカード）を生成するメソッド。
     */
    public void frop() {
        for (int i=0; i<3; i++) {
            Card card = deck.deal();
            communityCard.add(new Card(card.getSuit(), card.getRank()));
        }
    }

    /**
     * ターンとリバーでのカード(1枚のコミュニティーカード)を生成するメソッド。
     */
    public void turnAndRiver() {
        Card card = deck.deal();
        communityCard.add(new Card(card.getSuit(), card.getRank()));
    }

    /**
     * プレイヤーがベットするためのメソッド。
     * @param _player プレイヤーのオブジェクト。
     * @param amount ベットする金額。
     */
    public void bet(Player _player, Integer amount) {
        if (amount == null) {
            System.out.println("Enter the amount you want to bet:");
        }
        int betAmount = (amount != null) ? amount : new Scanner(System.in).nextInt();
    
        // プレイヤーのスコアがベット額以上であることを確認
        if (_player.getScore() >= betAmount) {
            _player.setScore(_player.getScore() - betAmount);  // プレイヤーのスコアからベット額を差し引く
            this.pot += betAmount;  // ポットにベット額を追加
            this.currentTurnPot += betAmount; //このターンだけのベット額を追加
            System.out.println(_player.getName() + " bets " + betAmount + ".\n");
        } else {
            System.out.println("You don't have enough score to bet that amount. Your current score is " + _player.getScore() + ".\n");
            playerAction();
        }
    }    

    /**
     * プレイヤーがコールするためのメソッド。
     * @param _player プレイヤーのオブジェクト。
     */
    public void call(Player _player) {
        if (_player.getScore() >= currentTurnPot) {
            _player.setScore(_player.getScore() - currentTurnPot);  // プレイヤーのスコアから追加のコール額を差し引く
            this.pot += currentTurnPot;  // ポットに追加のコール額を追加
            currentTurnPot = 0; //ここで、ターンは変わるので0にリセット
            System.out.println(_player.getName() + " calls.\n");
        } else {
            System.out.println("You don't have enough score to call. Your current score is " + _player.getScore() + ".\n");
            playerAction();
        }
    }

    /**
     * プレイヤーがチェックするためのメソッド。
     * @param _player プレイヤーのオブジェクト。
     */    
    public void check(Player _player) {
        System.out.println(_player.getName() + " checks.\n");
    }    

    /**
     * プレイヤーがフォールドするためのメソッド。
     * @param _player プレイヤーのオブジェクト。
     */    
    public void fold(Player _player) {
        System.out.println(_player.getName() + " folds.\n");
        _player.setStatus(false);  // プレイヤーのステータスをfalseに設定してゲームから撤退
    }    

    /**
     * 全てのアクションが終わった時、勝者を決定するメソッド
     * プレイヤーとボットの手札を評価し、役の強さに基づいて勝者を決定します。
     * @return 勝者の名前と役、または「引き分け」を表す文字列。
     */    
    public String determineWinner() {
        String playerHand = HandEvaluator.evaluateHand(player, this);
        String botHand = HandEvaluator.evaluateHand(bot, this);

        // 役の強さを表すマップ(辞書型的なもの)
        Map<String, Integer> handStrengths = new HashMap<>();
        handStrengths.put(HandEvaluator.HIGH_CARD, 1);
        handStrengths.put(HandEvaluator.ONE_PAIR, 2);
        handStrengths.put(HandEvaluator.TWO_PAIR, 3);
        handStrengths.put(HandEvaluator.THREE_OF_A_KIND, 4);
        handStrengths.put(HandEvaluator.STRAIGHT, 5);
        handStrengths.put(HandEvaluator.FLUSH, 6);
        handStrengths.put(HandEvaluator.FULL_HOUSE, 7);
        handStrengths.put(HandEvaluator.FOUR_OF_A_KIND, 8);
        handStrengths.put(HandEvaluator.STRAIGHT_FLUSH, 9);
        handStrengths.put(HandEvaluator.ROYAL_FLUSH, 10);

        int playerStrength = handStrengths.get(playerHand);
        int botStrength = handStrengths.get(botHand);

        if (playerStrength > botStrength) {
            player.setScore(player.getScore() + pot);
            return player.getName() + " wins with " + playerHand;
        } else if (botStrength > playerStrength) {
            bot.setScore(bot.getScore() + pot);
            return bot.getName() + " wins with " + botHand;
        } else {
            player.setScore(player.getScore() + pot/2);
            bot.setScore(bot.getScore() + pot/2);
            return "It's a tie!";
        }

    }

    /**
     * フォールドアクションによる勝者を判定するメソッド。
     * プレイヤーまたはボットがフォールドした場合、もう一方のプレイヤーが勝者となります。
     */    
    public void checkWinner() {
        if (!player.getStatus()) {
            System.out.println("--------------------------------");
            System.out.println(bot.getName() + " wins because " + player.getName() + " has folded.\n");
            bot.setScore(bot.getScore() + pot);
            player.showScore();
            bot.showScore();
            player.saveScore();
            bot.saveScore();
            // 勝者をチェックした後にcontinueActionメソッドを呼び出す
            gameController.continueAction();            
        } else if (!bot.getStatus()) {
            System.out.println("--------------------------------");
            System.out.println(player.getName() + " wins because " + bot.getName() + " has folded.\n");
            player.setScore(player.getScore() + pot);
            player.showScore();
            bot.showScore();
            player.saveScore();
            bot.saveScore();
            // 勝者をチェックした後にcontinueActionメソッドを呼び出す
            gameController.continueAction();
        }
    }    
    
    /**
     * プレイヤーに行動を選択させるメソッド。
     * プレイヤーにはベット、コール、チェック、フォールド、スコアの確認のオプションが提供されます。
     */    
    public void playerAction() {
        try (Scanner scanner = new Scanner(System.in)) {
            if (currentTurnPot == 0) {
                System.out.println("Choose an action:\n1. Bet\n2. Check\n3. Fold\n4. Check Score");
            } else {
                System.out.println("Choose an action:\n1. Call\n2. Fold\n3. Check Score");
            }
            int action = scanner.nextInt();
            if (currentTurnPot == 0) {
                if (action == 1) {
                    bet(player, null);
                }
                else if (action == 2) {
                    check(player);
                } else if (action == 3) {
                    fold(player);
                } else if (action == 4) {
                    player.showScore();
                    playerAction();
                } else {
                    System.err.println("Invalid action. Choose a number between 1 and 4.\n"); playerAction();
                }
            } else {
                if (action == 1) {
                    call(player);
                } else if (action == 2) {
                    fold(player);
                } else if (action == 3) {
                    player.showScore();
                    playerAction();
                } else {
                    System.err.println("Invalid action. Choose a number between 1 and 3.\n"); playerAction();
                }
            }
        }
    }

    /**
     * ボットに行動を選択させます。
     * ボットにはベット、コール、チェック、フォールドのオプションが提供されます。
     */    
    public void botAction() {
        Random random = new Random();
        int action;
    
        if (currentTurnPot == 0) {
            action = random.nextInt(2) + 1;  // まだpotが０の時、ランダムに1（ベット）、2（チェック）を選択
        } else {
            action = random.nextInt(6) + 1;  // potにscoreが賭けられてる時、ランダムに1と2,3,4,5は（コール）または6（フォールド）を選択（Botがコールする確率を高めにするため）
        }
        
        if (player.getStatus() == true) {
        switch (action) {
            case 1:
                if (currentTurnPot == 0) {        
                    bet(bot, 1000);  // ベット額を1000に固定
                    playerAction();
                } else {
                    call(bot);
                }
                break;
            case 2:
                if (currentTurnPot == 0) {
                    check(bot);
                } else {
                    call(bot);
                }
                break;
            case 3:
            case 4:
            case 5:
                call(bot);
                break;
            case 6 :
                fold(bot);
                break;
            }
        }
    }
}