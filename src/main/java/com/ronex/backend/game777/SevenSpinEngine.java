package com.ronex.backend.game777;

import java.util.List;
import java.util.Random;

public class SevenSpinEngine {

    private static final List<String> SYMBOLS =
            List.of("7", "🍒", "🍋", "🔔", "⭐");

    private static final Random random = new Random();

    public static SpinResult spin() {

        int r = random.nextInt(100); // 0–99

        if (r < 40) {
            // 40% → NO MATCH
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

        } else if (r < 80) {
            // 40% → DOUBLE MATCH
            String m = randomSymbol();
            String o;
            do {
                o = randomSymbol();
            } while (o.equals(m));

            List<List<String>> patterns = List.of(
                    List.of(m, m, o),
                    List.of(m, o, m),
                    List.of(o, m, m)
            );

            return new SpinResult(
                    patterns.get(random.nextInt(patterns.size())),
                    SevenResultType.DOUBLE_MATCH
            );

        } else if (r < 95) {
            // 15% → TRIPLE (non-7)
            String s;
            do {
                s = randomSymbol();
            } while (s.equals("7"));

            return new SpinResult(
                    List.of(s, s, s),
                    SevenResultType.TRIPLE_MATCH
            );

        } else {
            // 5% → 7-7-7 JACKPOT
            return new SpinResult(
                    List.of("7", "7", "7"),
                    SevenResultType.JACKPOT
            );
        }
    }

    private static String randomSymbol() {
        return SYMBOLS.get(random.nextInt(SYMBOLS.size()));
    }

    // INNER RESULT CLASS
    public static class SpinResult {
        public final List<String> symbols;
        public final SevenResultType type;

        public SpinResult(List<String> symbols, SevenResultType type) {
            this.symbols = symbols;
            this.type = type;
        }
    }
}