package com.ronex.backend.game777;

import java.util.List;
import java.util.Random;

public class SevenSpinEngine {

    private static final List<String> SYMBOLS =
            List.of("7", "🍒", "🍋", "🔔", "⭐");

    private static final Random random = new Random();

    public static SpinResult spin() {

        int r = random.nextInt(100); // 0–99

        // -------------------------
        // 50% → THREE DIFFERENT
        // -------------------------
        if (r < 50) {

            String a, b, c;
            do {
                a = randomSymbol();
                b = randomSymbol();
                c = randomSymbol();
            } while (a.equals(b) || b.equals(c) || a.equals(c));

            return new SpinResult(
                    List.of(a, b, c),
                    SevenResultType.NO_MATCH
            );
        }

        // -------------------------
        // 32% → TWO SAME (DOUBLE)
        // -------------------------
        else if (r < 82) { // 50 + 32

            String match = randomSymbol();
            String other;
            do {
                other = randomSymbol();
            } while (other.equals(match));

            List<List<String>> patterns = List.of(
                    List.of(match, match, other),
                    List.of(match, other, match),
                    List.of(other, match, match)
            );

            return new SpinResult(
                    patterns.get(random.nextInt(patterns.size())),
                    SevenResultType.DOUBLE_MATCH
            );
        }

        // -------------------------
        // 15% → THREE SAME (NON-7)
        // -------------------------
        else if (r < 97) { // 82 + 15

            String s;
            do {
                s = randomSymbol();
            } while (s.equals("7"));

            return new SpinResult(
                    List.of(s, s, s),
                    SevenResultType.TRIPLE_MATCH
            );
        }

        // -------------------------
        // 3% → 7-7-7 JACKPOT
        // -------------------------
        else {

            return new SpinResult(
                    List.of("7", "7", "7"),
                    SevenResultType.JACKPOT
            );
        }
    }

    private static String randomSymbol() {
        return SYMBOLS.get(random.nextInt(SYMBOLS.size()));
    }

    // -------------------------
    // RESULT CLASS
    // -------------------------
    public static class SpinResult {
        public final List<String> symbols;
        public final SevenResultType type;

        public SpinResult(List<String> symbols, SevenResultType type) {
            this.symbols = symbols;
            this.type = type;
        }
    }
}