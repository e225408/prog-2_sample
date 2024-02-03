package jp.ac.uryukyu.ie.e225408;

/**
 * カードを用意するクラス。
 */
public class Card {
    /**
     * カードのスート（ハート、ダイヤ、クラブ、スペード）
     */
    private String suit;
    /**
     * カードのランク（A、2、3、...、J、Q、K）
     */
    private String rank;

    /**
     * Cardクラスのコンストラクタ。
     * @param suit カードのスートを表す文字列。
     * @param rank カードのランクを表す文字列。
     */
    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * このカードのスートを返します。
     * @return スートを表す文字列
     */
    public String getSuit() {
        return this.suit;
    }

    /**
     * このカードのランクを返します。
     * @return ランクを表す文字列
     */
    public String getRank() {
        return this.rank;
    }

}