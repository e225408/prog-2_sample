package jp.ac.uryukyu.ie.e225408;

import java.util.ArrayList;

/**
 * ポーカーにおける手札を評価するメソッド。
 * ポーカーの役を表す定数とスートを表す定数を持ちます。
 */
public class Hand {
    /**
     * ロイヤルフラッシュを表す定数。
     */
    public static final String ROYAL_FLUSH = "Royal Flush";
    /**
     * ストレートフラッシュを表す定数。
     */
    public static final String STRAIGHT_FLUSH = "Straight Flush";
    /**
     * フォーカードを表す定数。
     */
    public static final String FOUR_OF_A_KIND = "Four of a Kind";
    /**
     * フルハウスを表す定数。
     */
    public static final String FULL_HOUSE = "Full House";
    /**
     * フラッシュを表す定数。
     */
    public static final String FLUSH = "Flush";
    /**
     * ストレートを表す定数。
     */
    public static final String STRAIGHT = "Straight";
    /**
     * スリーカードを表す定数。
     */
    public static final String THREE_OF_A_KIND = "Three of a Kind";
    /**
     * ツーペアを表す定数。
     */
    public static final String TWO_PAIR = "Two Pair";
    /**
     * ワンペアを表す定数。
     */
    public static final String ONE_PAIR = "One Pair";
    /**
     * ハイカードを表す定数。
     */
    public static final String HIGH_CARD = "High Card";
    /**
     * スペードのスートを表す定数。
     */
    private static final String SPADES = "Spades";
    /**
     * ハートのスートを表す定数。
     */
    private static final String HEARTS = "Hearts";
    /**
     * ダイヤモンドのスートを表す定数。
     */
    private static final String DIAMONDS = "Diamonds";
    /**
     * クラブのスートを表す定数。
     */
    private static final String CLUBS = "Clubs";

    /**
     * プレイヤーの手札を評価します。
     * @param player 評価するPlayerクラスのオブジェクト。
     * @param game 現在のGameクラスのオブジェクト。
     * @return プレイヤーの手札の役を表す文字列。
     */    
    public static String evaluateHand(GamePlayer player, Game game) {
        if (isRoyalFlush(getAllCards(player, game))) {
            return ROYAL_FLUSH;
        } else if (isStraightFlush(getAllCards(player, game))) {
            return STRAIGHT_FLUSH;
        } else if (isFourOfAKind(getAllCards(player, game))) {
            return FOUR_OF_A_KIND;
        } else if (isFullHouse(getAllCards(player, game))) {
            return FULL_HOUSE;
        } else if (isFlush(getAllCards(player, game))) {
            return FLUSH;
        } else if (isStraight(getAllCards(player, game))) {
            return STRAIGHT;
        } else if (isThreeOfAKind(getAllCards(player, game))) {
            return THREE_OF_A_KIND;
        } else if (isTwoPair(getAllCards(player, game))) {
            return TWO_PAIR;
        } else if (isOnePair(getAllCards(player, game))) {
            return ONE_PAIR;
        } else {
            return HIGH_CARD;
        }
    }

    /**
     * プレイヤーの手札と場にあるカードをまとめたリストを生成するメソッド。
     * @param player 手札を取得するプレイヤーのオブジェクト。
     * @param game 現在のゲームのオブジェクト。
     * @return プレイヤーの手札と場にあるカードをまとめたリスト。
     */    
    public static ArrayList<Card> getAllCards(GamePlayer player,Game game) {
        ArrayList<Card> allCards = new ArrayList<Card>();
        allCards.addAll(player.getHand());
        allCards.addAll(game.getCommunityCard());
        return allCards;
    }

    /**
     * 手札がロイヤルフラッシュであるかどうかを判定するメソッド。
     * @param allCards 判定するカードのリスト。
     * @return 手札がロイヤルフラッシュである場合はtrue、そうでない場合はfalse。
     */    
    private static boolean isRoyalFlush(ArrayList<Card> allCards) {
        if (allCards.size() < 5) {
            return false;
        }
    
        // 各スートについて調べる
        for (String suit : new String[]{SPADES, HEARTS, DIAMONDS, CLUBS}) {
            // 指定したスートのカードを抽出
            ArrayList<Card> sameSuitCards = new ArrayList<>();
            for (Card card : allCards) {
                if (card.getSuit().equals(suit)) {
                    sameSuitCards.add(card);
                }
            }
    
            // 同じスートのカードが5枚以上存在し、それらが10からエースの連続したランクを持つかどうかを調べる
            if (sameSuitCards.size() >= 5) {
                boolean has10 = false, hasJ = false, hasQ = false, hasK = false, hasA = false;
                for (Card card : sameSuitCards) {
                    switch (card.getRank()) {
                        case "10":
                            has10 = true;
                            break;
                        case "J":
                            hasJ = true;
                            break;
                        case "Q":
                            hasQ = true;
                            break;
                        case "K":
                            hasK = true;
                            break;
                        case "A":
                            hasA = true;
                            break;
                    }
                }
                if (has10 && hasJ && hasQ && hasK && hasA) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 手札がストレートフラッシュであるかどうかを判定するメソッド。
     * @param allCards 判定するカードのリスト。
     * @return 手札がストレートフラッシュである場合はtrue、そうでない場合はfalse。
     */    
    private static boolean isStraightFlush(ArrayList<Card> allCards) {
        if (isFlush(allCards) && (isStraight(allCards))) {
            return true;
        }
        return false;
    }

    /**
     * 手札がフォーカードであるかどうかを判定するメソッド。
     * @param allCards 判定するカードのリスト。
     * @return 手札がフォーカードである場合はtrue、そうでない場合はfalse。
     */    
    private static boolean isFourOfAKind(ArrayList<Card> allCards) {
        if (allCards.size() < 4) {
            return false;
        }

        for (String rank : new String[]{"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"}) {
            ArrayList<Card> sameRankCards = new ArrayList<>();
            for (Card card : allCards) {
                if (card.getRank().equals(rank)) {
                    sameRankCards.add(card);
                }
            }
            // 同じランクのカードが4枚存在するかどうかを調べる
            if (sameRankCards.size() == 4) {
                return true;
            }
        } 
        return false;
    }

    /**
     * 手札がフルハウスであるかどうかを判定するメソッド。
     * @param allCards 判定するカードのリスト。
     * @return 手札がフルハウスである場合はtrue、そうでない場合はfalse。
     */    
    private static boolean isFullHouse(ArrayList<Card> allCards) {
        if (allCards.size() < 5) {
            return false;
        }

        for (String rank1 : new String[]{"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"}) {
            int count1 = 0;
            for (Card card : allCards) {
                if (card.getRank().equals(rank1)) {
                    count1++;
                }
            }   
            if (count1 == 3) {
                for (String rank2 : new String[]{"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"}) {
                    if (!rank2.equals(rank1)) {
                        int count2 = 0;
                        for (Card card : allCards) {
                            if (card.getRank().equals(rank2)) {
                                count2++;
                            }
                        }
                        if (count2 == 2) {
                            return true;
                        }
                    }
                }
            }
        }   
        return false;  
    }

    /**
     * 手札がフラッシュであるかどうかを判定するメソッド。
     * @param allCards 判定するカードのリスト。
     * @return 手札がフラッシュである場合はtrue、そうでない場合はfalse。
     */    
    private static boolean isFlush(ArrayList<Card> allCards) {     
        if (allCards.size() < 5) {
            return false;
        }   
        // 各スートのカードの数を格納する配列を用意
        int[] suitCounts = new int[4];
        
        // 手札のすべてのカードのスートを確認
        for (Card card : allCards) {
            switch (card.getSuit()) {
                case SPADES:
                    suitCounts[0]++;
                    break;
                case HEARTS:
                    suitCounts[1]++;
                    break;
                case DIAMONDS:
                    suitCounts[2]++;
                    break;
                case CLUBS:
                    suitCounts[3]++;
                    break;
            }
        }

        // 同じスートのカードが5枚以上あるかを判定
        for (int count : suitCounts) {
            if (count >= 5) {
                return true;
            }
        }

        return false;
    }

    /**
     * 手札がストレートであるかどうかを判定するメソッド。
     * @param allCards 判定するカードのリスト。
     * @return 手札がストレートである場合はtrue、そうでない場合はfalse。
     */    
    public static boolean isStraight(ArrayList<Card> allCards) {
        if (allCards.size() < 5) {
            return false;
        }
        // 各ランクのカードの数を格納する配列を用意
        int[] rankCounts = new int[15];  // ランクはA（1）から13、Aを14としても扱う

        // 手札のすべてのカードのランクを確認
        for (Card card : allCards) {
            String rank = card.getRank();
            int rankValue;
            switch (rank) {
                case "A":
                    rankCounts[1]++;
                    rankCounts[14]++;  // エースを14としてもカウント
                    break;
                case "J":
                    rankValue = 11;
                    rankCounts[rankValue]++;
                    break;
                case "Q":
                    rankValue = 12;
                    rankCounts[rankValue]++;
                    break;
                case "K":
                    rankValue = 13;
                    rankCounts[rankValue]++;
                    break;
                default:
                    rankValue = Integer.parseInt(rank);
                    rankCounts[rankValue]++;
                    break;
            }
        }

        // 連続したランクのカードが5枚以上あるかを判定
        for (int i = 1; i <= 10; i++) {
            if (rankCounts[i] >= 1 && rankCounts[i + 1] >= 1 && rankCounts[i + 2] >= 1 && rankCounts[i + 3] >= 1 && rankCounts[i + 4] >= 1) {
                return true;
            }
        }

        return false;
    }

    /**
     * 手札がスリーカードであるかどうかを判定するメソッド。
     * @param allCards 判定するカードのリスト。
     * @return 手札がスリーカードである場合はtrue、そうでない場合はfalse。
     */    
    private static boolean isThreeOfAKind(ArrayList<Card> allCards) {
        if (allCards.size() < 3) {
            return false;
        }

        for (String rank : new String[]{"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"}) {
            int sameRankCount = 0;
            for (Card card : allCards) {
                if (card.getRank().equals(rank)) {
                    sameRankCount++;
                }
            }   
            // 同じランクのカードが3枚存在するかどうかを調べる
            if (sameRankCount == 3) {
                return true;
            }
        } 
        return false;
    }

    /**
     * 手札がツーペアであるかどうかを判定するメソッド。
     * @param allCards 判定するカードのリスト。
     * @return 手札がツーペアである場合はtrue、そうでない場合はfalse。
     */
    private static boolean isTwoPair(ArrayList<Card> allCards) {
        if (allCards.size() < 4) {
            return false;
        }

        int[] rankCount = new int[14];

        // 各カードのランクの出現回数を数える
        for (Card card : allCards) {
            String rank = card.getRank();
            int rankValue;
            switch (rank) {
                case "A":
                    rankValue = 1;
                    break;
                case "J":
                    rankValue = 11;
                    break;
                case "Q":
                    rankValue = 12;
                    break;
                case "K":
                    rankValue = 13;
                    break;
                default:
                    rankValue = Integer.parseInt(rank);
                    break;    
            }
            rankCount[rankValue]++;
        }

        // ペア（同じランクのカードが2枚）の数を数える
        int pairCount = 0;
        for (int count : rankCount) {
            if (count == 2) {
                pairCount++;
            }
        }
    
        // ペアが2つある場合はtrueを返す
        return pairCount == 2;    
    }

    /**
     * 手札がワンペアであるかどうかを判定するメソッド。
     * @param allCards 判定するカードのリスト。
     * @return 手札がワンペアである場合はtrue、そうでない場合はfalse。
     */
    private static boolean isOnePair(ArrayList<Card> allCards) {
        if (allCards.size() < 2) {
            return false;
        }

        int[] rankCount = new int[14];

        // 各カードのランクの出現回数を数える
        for (Card card : allCards) {
            String rank = card.getRank();
            int rankValue;
            switch (rank) {
                case "A":
                    rankValue = 1;
                    break;
                case "J":
                    rankValue = 11;
                    break;
                case "Q":
                    rankValue = 12;
                    break;
                case "K":
                    rankValue = 13;
                    break;
                default:
                    rankValue = Integer.parseInt(rank);
                    break;    
            }
            rankCount[rankValue]++;
        }
        
        // ペア（同じランクのカードが2枚）の数を数える
        int pairCount = 0;
        for (int count : rankCount) {
            if (count == 2) {
                pairCount++;
            }
        }
    
        // ペアが1つある場合はtrueを返す
        return pairCount == 1;    
    }
}